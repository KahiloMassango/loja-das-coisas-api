package org.example.loja_das_coisas_api.notification.service

import jakarta.transaction.Transactional
import org.example.loja_das_coisas_api.auth.repository.UserRepository
import org.example.loja_das_coisas_api.exception.ResourceNotFoundException
import org.example.loja_das_coisas_api.notification.model.DeviceType
import org.example.loja_das_coisas_api.notification.model.UserDevice
import org.example.loja_das_coisas_api.notification.repository.DeviceRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class DeviceService(
    private val deviceRepository: DeviceRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun registerDevice(userId: UUID, deviceToken: String, deviceType: DeviceType): UserDevice {
        val existingDevice = deviceRepository.findByDeviceToken(deviceToken)

        val user = userRepository.findById(userId)
            .orElseThrow { ResourceNotFoundException("Usuário não encontrado") }

        if (existingDevice.isPresent) {
            val deviceToUpdate = existingDevice.get()

            deviceToUpdate.user = user
            deviceToUpdate.deviceType = deviceType
            deviceToUpdate.isActive = true

            return deviceRepository.saveAndFlush(deviceToUpdate)
        }

        val newDevice = UserDevice(
            user = user,
            deviceToken = deviceToken,
            deviceType = deviceType
        )

        return deviceRepository.saveAndFlush(newDevice)
    }

    @Transactional
    fun getActiveTokensForUser(userId: UUID): List<String> {
        return deviceRepository.findByUserIdAndIsActiveTrue(userId)
            .map { it.deviceToken }
    }

    fun removeDevice(deviceToken: String) {
        deviceRepository.deleteByDeviceToken(deviceToken)
    }
}
