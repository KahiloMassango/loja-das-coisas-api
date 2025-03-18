package org.example.loja_das_coisas_api.services.interfaces

import org.example.loja_das_coisas_api.dtos.requests.OrderDtoReq
import org.example.loja_das_coisas_api.dtos.responses.order.*
import java.util.*

interface OrderService {
    fun getAllCustomerOrders(customerEmail: String,): CustomerOrdersDtoRes
    fun getCustomerOrderById(id: UUID, customerEmail: String, ): CustomerOrderDetailDtoRes
    fun placeOrder(customerEmail: String, request: OrderDtoReq): CustomerOrderDtoRes

    fun confirmOrderPayment(orderId: UUID, amount: Int)
    fun cancelOrder(orderId: UUID)

    fun confirmOrderDelivered(orderId: UUID, storeEmail: String)
    fun getAllStoreOrders(storeEmail: String): StoreOrdersDtoRes
    fun getStoreOrderById(id: UUID, storeEmail: String): StoreOrderDetailDtoRes
}