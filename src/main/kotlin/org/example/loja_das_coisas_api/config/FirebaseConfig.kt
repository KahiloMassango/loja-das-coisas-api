package org.example.loja_das_coisas_api.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream
import java.io.IOException

@Configuration
class FirebaseConfig {
    @Bean
    @Throws(IOException::class)
    fun initializeFirebase(): FirebaseApp {
        // Path to your service account key JSON file
        val serviceAccount =
            FileInputStream("src/main/resources/serviceAccountKey.json")

        val options: FirebaseOptions? = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

        // Prevent re-initialization if the app restarts in a dev environment
        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options)
        }

        return FirebaseApp.getInstance()
    }
}