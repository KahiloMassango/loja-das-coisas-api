package org.example.loja_das_coisas_api.models.mappers

import org.example.loja_das_coisas_api.dtos.responses.ProductDtoRes
import org.example.loja_das_coisas_api.dtos.responses.StoreAdminDtoRes
import org.example.loja_das_coisas_api.dtos.responses.StoreDetailDtoRes
import org.example.loja_das_coisas_api.dtos.responses.StoreDtoRes
import org.example.loja_das_coisas_api.models.store.Store


fun Store.toStoreDtoRes() =
    StoreDtoRes(
        description = description,
        offersDelivery = offersDelivery,
        nif = nif,
        latitude = latitude,
        longitude = longitude,
        id = id!!,
        storeName = storeName,
        storeLogoUrl = storeLogoUrl,
    )


fun Store.toStoreAdminDtoRes() =
    StoreAdminDtoRes(
        id = id!!,
        name = storeName,
        description = description,
        offersDelivery = offersDelivery,
        nif = nif,
        memberSince = "${createdAt!!.toDateTime().month} ${createdAt!!.toDateTime().year}",
        latitude = latitude,
        longitude = longitude,
        email = user.email,
        phoneNumber = user.phoneNumber,
        logoUrl = storeLogoUrl,
        openingTime = openingTime.toString(),
        closingTime = closingTime.toString(),
        address = address,
        isActive = active
    )


fun Store.toStoreDetailDtoRes(
    totalProducts: Int,
    totalCompletedOrders: Int,
    products: List<ProductDtoRes>
) =
    StoreDetailDtoRes(
        name = storeName,
        description = description,
        offersDelivery = offersDelivery,
        nif = nif,
        memberSince = "${createdAt!!.toDateTime().month} ${createdAt!!.toDateTime().year}",
        latitude = latitude,
        longitude = longitude,
        totalProducts = totalProducts,
        totalCompletedOrders = totalCompletedOrders,
        products = products,
        logoUrl = storeLogoUrl,
        openingTime = openingTime.toString(),
        closingTime = closingTime.toString(),
        address = address
    )


