package org.example.loja_das_coisas_api.notification.service

import org.springframework.stereotype.Service
import java.util.*

@Service
class NotificationService(
    private val deviceService: DeviceService
    // private val firebaseMessaging: FirebaseMessaging // Assuming standard FCM SDK dependency
) {

    fun sendPushToUser(userId: UUID, title: String, body: String) {
        val tokens = deviceService.getActiveTokensForUser(userId)

        if (tokens.isEmpty()) {
            return // User has no active logged-in devices
        }

        for (token in tokens) {
            try {
                // Pseudo-code representing your actual FCM client logic:
                // val message = Message.builder().setToken(token).setNotification(...).build()
                // firebaseMessaging.send(message)
                println("Sending push notification to token: $token")
            } catch (e: Exception) {
                // If the exception indicates an invalid token (e.g., UNREGISTERED or INVALID_ARGUMENT in FCM)
                if (isTokenDead(e)) {
                    println("Token $token is dead. Cleaning it up from database.")
                    deviceService.removeDevice(token)
                }
            }
        }
    }

    private fun isTokenDead(e: Exception): Boolean {
        // Implement based on your actual FCM SDK exception mapping
        // e.g., if (e is FirebaseMessagingException) return e.messagingErrorCode == MessagingErrorCode.UNREGISTERED
        return e.message?.contains("UNREGISTERED", ignoreCase = true) == true
    }
}