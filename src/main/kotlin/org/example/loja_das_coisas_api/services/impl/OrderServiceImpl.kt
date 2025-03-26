package org.example.loja_das_coisas_api.services.impl

import jakarta.transaction.Transactional
import org.example.loja_das_coisas_api.dtos.requests.OrderDtoReq
import org.example.loja_das_coisas_api.dtos.requests.OrderItemDtoReq
import org.example.loja_das_coisas_api.dtos.responses.order.*
import org.example.loja_das_coisas_api.exceptions.DifferentStoreProductsException
import org.example.loja_das_coisas_api.exceptions.EntityNotFoundException
import org.example.loja_das_coisas_api.exceptions.InsufficientStockException
import org.example.loja_das_coisas_api.models.OrderItem
import org.example.loja_das_coisas_api.models.ProductItem
import org.example.loja_das_coisas_api.models.mappers.toDtoRes
import org.example.loja_das_coisas_api.models.mappers.toStoreDtoRes
import org.example.loja_das_coisas_api.models.order.*
import org.example.loja_das_coisas_api.repositories.*
import org.example.loja_das_coisas_api.services.interfaces.OrderService
import org.springframework.stereotype.Service
import java.util.*
import kotlin.jvm.optionals.getOrNull

@Service
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val productItemRepository: ProductItemRepository,
    private val storeRepository: StoreRepository,
    private val orderItemRepository: OrderItemRepository,
    private val customerRepository: CustomerRepository,
) : OrderService {

    @Transactional
    override fun placeOrder(customerEmail: String, request: OrderDtoReq): CustomerOrderDtoRes {
        val user = customerRepository.findByEmail(customerEmail)!!

        // Use the first order item to validate all are from the same store
        val productItem = getProductItem(request.orderItems.first().productItemId)

        // Validate all order items before proceeding. This ensures the items exist and have sufficient stock.
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

        // subtract product item stock qty by orderItem qty
        // to ensure order processing safety
        // if the order is canceled update again product item stock qty
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

    override fun getCustomerOrderById(id: UUID, customerEmail: String): CustomerOrderDetailDtoRes {
        val customer = customerRepository.findByEmail(customerEmail)!!

        val orderItems = orderItemRepository.findAllByOrderId(id).map { it.toDtoRes() }

        return orderRepository.findByIdAndCustomerId(id, customer.id!!).get().toDetailDtoRes(orderItems)
    }

    override fun getStoreOrderById(id: UUID, storeEmail: String): StoreOrderDetailDtoRes {
        val store = storeRepository.findByEmail(storeEmail) ?: throw EntityNotFoundException("Loja inativa")

        val order = orderRepository.findByIdAndStoreIdAndStatusIn(
            id = id,
            storeId = store.id!!,
            status = listOf(OrderStatus.Processando, OrderStatus.Entregue),
        )
            .getOrNull() ?: throw EntityNotFoundException("Encomenda não encontrada")

        val orderItems = orderItemRepository.findAllByOrderId(order.id!!).map { it.toStoreDtoRes() }

        return order.toStoreDetailDtoRes(orderItems)
    }

    @Transactional
    override fun getAllCustomerOrders(customerEmail: String): CustomerOrdersDtoRes {

        val customerId = customerRepository.findByEmail(customerEmail)!!.id!!

        val response = CustomerOrdersDtoRes(
            delivered = orderRepository.findAllByCustomerIdAndStatusIn(customerId, listOf(OrderStatus.Entregue))
                .map {
                    val totalItemsInOrder = orderRepository.getOrderItemsCountByOrderId(it.id!!)
                    it.toDtoRes(totalItemsInOrder)
                },
            canceled = orderRepository.findAllByCustomerIdAndStatusIn(customerId, listOf(OrderStatus.Cancelado))
                .map {
                    val totalItemsInOrder = orderRepository.getOrderItemsCountByOrderId(it.id!!)
                    it.toDtoRes(totalItemsInOrder)
                },
            pending = orderRepository.findAllByCustomerIdAndStatusIn(
                customerId,
                listOf(OrderStatus.Pendente, OrderStatus.Processando)
            ).map {
                val totalItemsInOrder = orderRepository.getOrderItemsCountByOrderId(it.id!!)
                it.toDtoRes(totalItemsInOrder)
            }
        )

        return response
    }

    override fun getAllStoreOrders(storeEmail: String): StoreOrdersDtoRes {
        val store = storeRepository.findByEmail(storeEmail) ?: throw EntityNotFoundException("Loja indisponível")

        return StoreOrdersDtoRes(
            totalPendingOrders = orderRepository.getStoreTotalOrdersByStatus(store.id!!, OrderStatus.Processando),
            totalDeliveredOrders = orderRepository.getStoreTotalOrdersByStatus(store.id!!, OrderStatus.Entregue),
            delivered = orderRepository.findAllByStoreIdAndStatusIn(store.id!!, listOf(OrderStatus.Entregue))
                .map {
                    val totalItemsInOrder = orderRepository.getOrderItemsCountByOrderId(it.id!!)
                    it.toStoreDtoRes(totalItemsInOrder)
                },

            pending = orderRepository.findAllByStoreIdAndStatusIn(
                store.id!!,
                listOf(OrderStatus.Processando)
            ).map {
                val totalItemsInOrder = orderRepository.getOrderItemsCountByOrderId(it.id!!)
                it.toStoreDtoRes(totalItemsInOrder)
            }
        )
    }

    @Transactional
    override fun confirmOrderPayment(orderId: UUID, amount: Int) {
        val order = orderRepository.findById(orderId).get()

        if (order.total != amount) {
            throw Exception("Order Inválid amount for the order")
        }

        order.orderItems.forEach { orderItem ->
            // Fetch the product item, apply the quantity reduction to stock, and save it.
            val updatedProductItem = getProductItem(orderItem.productItem.id!!).apply {
                this.stockQuantity -= orderItem.quantity
            }
            productItemRepository.save(updatedProductItem)
        }

        order.apply {
            status = OrderStatus.Pendente
        }
    }

    override fun confirmOrderDelivered(orderId: UUID, storeEmail: String) {
        val store = storeRepository.findByEmail(storeEmail) ?: throw EntityNotFoundException("Loja indisponível")
        val order = orderRepository.findByIdAndStoreIdAndStatusLike(orderId, store.id!!, OrderStatus.Processando)
            .getOrNull() ?: throw EntityNotFoundException("Order not found with id $orderId")

        order.apply {
            status = OrderStatus.Entregue
        }
        orderRepository.save(order)
    }

    @Transactional
    override fun cancelOrder(orderId: UUID) {
        val order = orderRepository.findById(orderId).get()

        // return back the product item stock qty after order is canceled
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