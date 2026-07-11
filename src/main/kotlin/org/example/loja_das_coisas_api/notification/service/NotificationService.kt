package org.example.loja_das_coisas_api.notification.service

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.example.loja_das_coisas_api.notification.model.NotificationTargetType
import org.springframework.stereotype.Service
import java.util.*

@Service
class NotificationService(
    private val deviceService: DeviceService,

) {

    fun sendPushToUser(
        userId: UUID,
        title: String,
        body: String,
        imageUrl: String? = null,
        targetType: NotificationTargetType? = null,
        targetId: UUID? = null
    ) {
        val tokens = deviceService.getActiveTokensForUser(userId)

        if (tokens.isEmpty()) {
            return // User has no active logged-in devices
        }

        for (token in tokens) {
            try {
                val notification = Notification.builder()
                    .setTitle(title)
                    .setBody(body)

                if (imageUrl != null) {
                    notification.setImage(imageUrl)
                }

                val message = Message.builder()
                    .setNotification(notification.build())
                    .setToken(token)

                if (targetType != null  && targetId != null) {
                    message.putData("targetType", targetType.name)
                    message.putData("targetId", targetId.toString())
                }


                FirebaseMessaging.getInstance().send(message.build())
            } catch (e: FirebaseMessagingException) {
                println("Error sending push notification: ${e.toString()}")
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