package org.example.loja_das_coisas_api.order.service

import jakarta.transaction.Transactional
import org.example.loja_das_coisas_api.customer.repository.CustomerRepository
import org.example.loja_das_coisas_api.exception.DifferentStoreProductsException
import org.example.loja_das_coisas_api.exception.EntityNotFoundException
import org.example.loja_das_coisas_api.exception.InsufficientStockException
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
) {

    @Transactional
    fun placeOrder(customerEmail: String, request: OrderDtoReq): CustomerOrderDtoRes {
        val user = customerRepository.findByEmail(customerEmail)!!

        val productItem = getProductItem(request.orderItems.first().productItemId)
        validateOrderItems(productItem.product.store.id!!, request.orderItems)

        val subtotal = request.orderItems.sumOf {
            it.quantity * productItemRepository.findByIdAndDeletedFalse(it.productItemId).get().price
        }
        val order = Order(
            customer = user,
            store = productItem.product.store,
            productQty = request.orderItems.size,
            subTotal = subtotal,
            total = subtotal + request.deliveryFee,
            deliveryFee = request.deliveryFee,
            deliveryAddressName = request.deliveryAddressName,
            deliveryLatitude = request.latitude,
            deliveryLongitude = request.longitude,
            paymentType = request.paymentType,
        )

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

        orderItemRepository.saveAll(orderItems)
        return orderRepository.save(order).toDtoRes(request.orderItems.size)
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
            status = listOf(OrderStatus.Processando, OrderStatus.Entregue),
        ).getOrNull() ?: throw EntityNotFoundException("Encomenda não encontrada")

        val orderItems = orderItemRepository.findAllByOrderId(order.id!!).map { it.toStoreDtoRes() }
        return order.toStoreDetailDtoRes(orderItems)
    }

    @Transactional
    fun getAllCustomerOrders(customerEmail: String): CustomerOrdersDtoRes {
        val customerId = customerRepository.findByEmail(customerEmail)!!.id!!

        return CustomerOrdersDtoRes(
            delivered = orderRepository.findAllByCustomerIdAndStatusIn(customerId, listOf(OrderStatus.Entregue))
                .map { it.toDtoRes(orderRepository.getOrderItemsCountByOrderId(it.id!!)) },
            canceled = orderRepository.findAllByCustomerIdAndStatusIn(customerId, listOf(OrderStatus.Cancelado))
                .map { it.toDtoRes(orderRepository.getOrderItemsCountByOrderId(it.id!!)) },
            pending = orderRepository.findAllByCustomerIdAndStatusIn(
                customerId, listOf(OrderStatus.Pendente, OrderStatus.Processando)
            ).map { it.toDtoRes(orderRepository.getOrderItemsCountByOrderId(it.id!!)) }
        )
    }

    fun getAllStoreOrders(storeEmail: String): StoreOrdersDtoRes {
        val store = storeRepository.findByEmail(storeEmail) ?: throw EntityNotFoundException("Loja indisponível")

        return StoreOrdersDtoRes(
            totalPendingOrders = orderRepository.getStoreTotalOrdersByStatus(store.id!!, OrderStatus.Processando),
            totalDeliveredOrders = orderRepository.getStoreTotalOrdersByStatus(store.id!!, OrderStatus.Entregue),
            delivered = orderRepository.findAllByStoreIdAndStatusIn(store.id!!, listOf(OrderStatus.Entregue))
                .map { it.toStoreDtoRes(orderRepository.getOrderItemsCountByOrderId(it.id!!)) },
            pending = orderRepository.findAllByStoreIdAndStatusIn(store.id!!, listOf(OrderStatus.Processando))
                .map { it.toStoreDtoRes(orderRepository.getOrderItemsCountByOrderId(it.id!!)) }
        )
    }

    @Transactional
    fun confirmOrderPayment(orderId: UUID, amount: Int) {
        val order = orderRepository.findById(orderId).get()

        if (order.total != amount) {
            throw Exception("Order Inválid amount for the order")
        }

        order.orderItems.forEach { orderItem ->
            val updatedProductItem = getProductItem(orderItem.productItem.id!!).apply {
                this.stockQuantity -= orderItem.quantity
            }
            productItemRepository.save(updatedProductItem)
        }

        order.apply { status = OrderStatus.Pendente }
    }

    fun confirmOrderDelivered(orderId: UUID, storeEmail: String) {
        val store = storeRepository.findByEmail(storeEmail) ?: throw EntityNotFoundException("Loja indisponível")
        val order = orderRepository.findByIdAndStoreIdAndStatusLike(orderId, store.id!!, OrderStatus.Processando)
            .getOrNull() ?: throw EntityNotFoundException("Order not found with id $orderId")

        order.apply { status = OrderStatus.Entregue }
        orderRepository.save(order)
    }

    @Transactional
    fun cancelOrder(orderId: UUID) {
        val order = orderRepository.findById(orderId).get()

        order.orderItems.forEach { orderItem ->
            val productItem = getProductItem(orderItem.productItem.id!!)
            productItem.stockQuantity += orderItem.quantity
            productItemRepository.save(productItem)
        }

        order.status = OrderStatus.Cancelado
        orderRepository.save(order)
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
