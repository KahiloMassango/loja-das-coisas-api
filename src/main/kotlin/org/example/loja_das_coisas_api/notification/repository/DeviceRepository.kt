package org.example.loja_das_coisas_api.notification.repository

import org.example.loja_das_coisas_api.notification.model.UserDevice
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface DeviceRepository : JpaRepository<UserDevice, UUID> {

    fun findByUserId(userId: UUID): Optional<UserDevice>

    fun findByDeviceToken(deviceToken: String): Optional<UserDevice>
    fun findByUserIdAndIsActiveTrue(userId: UUID): List<UserDevice>
    fun deleteByDeviceToken(deviceToken: String)

}
