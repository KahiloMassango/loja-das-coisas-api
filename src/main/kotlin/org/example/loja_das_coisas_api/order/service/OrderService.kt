package org.example.loja_das_coisas_api.order.service

import jakarta.transaction.Transactional
import org.example.loja_das_coisas_api.customer.repository.CustomerRepository
import org.example.loja_das_coisas_api.exception.DifferentStoreProductsException
import org.example.loja_das_coisas_api.exception.EntityNotFoundException
import org.example.loja_das_coisas_api.exception.InsufficientStockException
import org.example.loja_das_coisas_api.notification.model.NotificationTargetType
import org.example.loja_das_coisas_api.notification.service.NotificationService
import org.example.loja_das_coisas_api.order.dto.*
import org.example.loja_das_coisas_api.order.mapper.*
import org.example.loja_das_coisas_api.order.model.Order
import org.example.loja_das_coisas_api.order.model.OrderItem
import org.example.loja_das_coisas_api.order.model.OrderStatus
import org.example.loja_das_coisas_api.order.repository.OrderItemRepository
import org.example.loja_das_coisas_api.order.repository.OrderRepository
import org.example.loja_das_coisas_api.product.model.ProductItem
import org.example.loja_das_coisas_api.product.repository.ProductItemRepository
import org.example.loja_das_coisas_api.store.repository.StoreRepository
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val productItemRepository: ProductItemRepository,
    private val storeRepository: StoreRepository,
    private val orderItemRepository: OrderItemRepository,
    private val customerRepository: CustomerRepository,
    private val notificationService: NotificationService
) {

    @Transactional
    fun placeOrder(customerEmail: String, request: OrderDtoReq): CustomerOrderDtoRes {
        val user = customerRepository.findByEmail(customerEmail)!!

        val productItem = getProductItem(request.orderItems.first().productItemId)
        validateOrderItems(productItem.product.store.id!!, request.orderItems)

        val subtotal = request.orderItems.sumOf {
            it.quantity.toBigDecimal() * productItemRepository.findByIdAndDeletedFalse(it.productItemId).get().price
        }

        var order = Order(
            customer = user,
            store = productItem.product.store,
            productQty = request.orderItems.size,
            subTotal = subtotal,
            total = subtotal + request.deliveryFeeAmount,
            deliveryFee = request.deliveryFeeAmount,
            deliveryAddressName = request.deliveryAddressName,
            deliveryLatitude = request.latitude,
            deliveryLongitude = request.longitude,
            paymentType = request.paymentType,
        )
        order = orderRepository.saveAndFlush(order)

        val orderItems = request.orderItems.map { orderItemReq ->
            val updatedProductItem = getProductItem(orderItemReq.productItemId)
            updatedProductItem.stockQuantity -= orderItemReq.quantity
            productItemRepository.save(updatedProductItem)

            OrderItem(
                productName = updatedProductItem.product.name,
                order = order,
                productItem = updatedProductItem,
                quantity = orderItemReq.quantity,
                price = updatedProductItem.price,
            )
        }

        orderItemRepository.saveAllAndFlush(orderItems)

        notificationService.sendPushToUser(
            userId = user.user.id!!,
            title = "Novo Pedido",
            body = "Novo Pedido criado com sucesso. Agurdando Pagamento!",
            targetType = NotificationTargetType.ORDER,
            targetId = order.id
        )
        return order.toDtoRes(request.orderItems.size)
    }

    fun getCustomerOrderById(id: UUID, customerEmail: String): CustomerOrderDetailDtoRes {
        val customer = customerRepository.findByEmail(customerEmail)!!
        val orderItems = orderItemRepository.findAllByOrderId(id).map { it.toDtoRes() }
        return orderRepository.findByIdAndCustomerId(id, customer.id!!).get().toDetailDtoRes(orderItems)
    }

    fun getStoreOrderById(id: UUID, storeEmail: String): StoreOrderDetailDtoRes {
        val store = storeRepository.findByEmail(storeEmail) ?: throw EntityNotFoundException("Loja inativa")

        val order = orderRepository.findByIdAndStoreIdAndStatusIn(
            id = id,
            storeId = store.id!!,
            status = listOf(OrderStatus.WaitingPayment, OrderStatus.Delivered),
        ).getOrNull() ?: throw EntityNotFoundException("Encomenda não encontrada")

        val orderItems = orderItemRepository.findAllByOrderId(order.id!!).map { it.toStoreDtoRes() }
        return order.toStoreDetailDtoRes(orderItems)
    }

    @Transactional
    fun getAllCustomerOrders(customerEmail: String): CustomerOrdersDtoRes {
        val customerId = customerRepository.findByEmail(customerEmail)!!.id!!

        return CustomerOrdersDtoRes(
            delivered = orderRepository.findAllByCustomerIdAndStatusIn(customerId, listOf(OrderStatus.Delivered))
                .map { it.toDtoRes(orderRepository.getOrderItemsCountByOrderId(it.id!!)) },
            canceled = orderRepository.findAllByCustomerIdAndStatusIn(customerId, listOf(OrderStatus.Cancelled))
                .map { it.toDtoRes(orderRepository.getOrderItemsCountByOrderId(it.id!!)) },
            pending = orderRepository.findAllByCustomerIdAndStatusIn(
                customerId, listOf(OrderStatus.Processing, OrderStatus.WaitingPayment)
            ).map { it.toDtoRes(orderRepository.getOrderItemsCountByOrderId(it.id!!)) }
        )
    }

    fun getAllStoreOrders(storeEmail: String): StoreOrdersDtoRes {
        val store = storeRepository.findByEmail(storeEmail) ?: throw EntityNotFoundException("Loja indisponível")

        return StoreOrdersDtoRes(
            totalPendingOrders = orderRepository.getStoreTotalOrdersByStatus(store.id!!, OrderStatus.WaitingPayment),
            totalDeliveredOrders = orderRepository.getStoreTotalOrdersByStatus(store.id!!, OrderStatus.Delivered),
            delivered = orderRepository.findAllByStoreIdAndStatusIn(store.id!!, listOf(OrderStatus.Delivered))
                .map { it.toStoreDtoRes(orderRepository.getOrderItemsCountByOrderId(it.id!!)) },
            pending = orderRepository.findAllByStoreIdAndStatusIn(store.id!!, listOf(OrderStatus.WaitingPayment))
                .map { it.toStoreDtoRes(orderRepository.getOrderItemsCountByOrderId(it.id!!)) }
        )
    }

    @Transactional
    fun confirmOrderPayment(orderId: UUID) {
        val order = orderRepository.findById(orderId).get()

        order.orderItems.forEach { orderItem ->
            val updatedProductItem = getProductItem(orderItem.productItem.id!!).apply {
                this.stockQuantity -= orderItem.quantity
            }
            productItemRepository.save(updatedProductItem)
        }

        order.apply { status = OrderStatus.Processing }
        orderRepository.save(order)

        notificationService.sendPushToUser(
            userId = order.customer.user.id!!,
            title = "Pedido Pago",
            body = "Seu pedido foi pago e encontra-se em processamento",
            targetType = NotificationTargetType.ORDER,
            targetId = order.id
        )

        notificationService.sendPushToUser(
            userId = order.store.user.id!!,
            title = "Novo Pedido",
            body = "Novo pedido, prepara o pedido para entrega",
            targetType = NotificationTargetType.ORDER,
            targetId = order.id
        )
    }

    fun confirmOrderDelivered(orderId: UUID, storeEmail: String) {
        val store = storeRepository.findByEmail(storeEmail) ?: throw EntityNotFoundException("Loja indisponível")
        val order = orderRepository.findByIdAndStoreIdAndStatusLike(orderId, store.id!!, OrderStatus.WaitingPayment)
            .getOrNull() ?: throw EntityNotFoundException("Order not found with id $orderId")

        order.apply { status = OrderStatus.Delivered }
        orderRepository.save(order)

        notificationService.sendPushToUser(
            userId = order.customer.user.id!!,
            title = "Pedido foi entregue",
            body = "Seu pedido foi entregue com sucesso",
            targetType = NotificationTargetType.ORDER,
            targetId = order.id
        )
    }

    @Transactional
    fun cancelOrder(orderId: UUID) {
        val order = orderRepository.findById(orderId).get()

        order.orderItems.forEach { orderItem ->
            val productItem = getProductItem(orderItem.productItem.id!!)
            productItem.stockQuantity += orderItem.quantity
            productItemRepository.save(productItem)
        }

        order.status = OrderStatus.Cancelled
        orderRepository.save(order)
        notificationService.sendPushToUser(
            userId = order.store.user.id!!,
            title = "Pedido Cancelado",
            body = "Pedido cancelado por outro motivos",
            targetType = NotificationTargetType.ORDER,
            targetId = order.id
        )
    }

    private fun validateOrderItems(storeId: UUID, items: List<OrderItemDtoReq>) {
        val productItemIds = items.map { it.productItemId }
        val productItems = productItemRepository.findAllById(productItemIds)

        items.forEach { orderItem ->
            val existingItem = productItems.find { it.id == orderItem.productItemId }
                ?: throw EntityNotFoundException("Product item not found with id ${orderItem.productItemId}")

            if (orderItem.quantity > existingItem.stockQuantity) {
                throw InsufficientStockException(orderItem.productItemId, existingItem.stockQuantity)
            }

            if (existingItem.product.store.id != storeId) {
                throw DifferentStoreProductsException()
            }
        }
    }

    private fun getProductItem(productItemId: UUID): ProductItem {
        return productItemRepository.findByIdAndDeletedFalse(productItemId)
            .getOrNull() ?: throw EntityNotFoundException("Product item not found with id $productItemId")
    }
}
