package org.example.loja_das_coisas_api.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.util.Base64

@Configuration
class FirebaseConfig {

    private val logger = LoggerFactory.getLogger(FirebaseConfig::class.java)

    @Value("\${firebase.credentials.path:}")
    private val credentialsPath: String = ""

    @Value("\${firebase.credentials.json:}")
    private val credentialsJson: String = ""

    @Bean
    fun initializeFirebase(): FirebaseApp? {
        if (FirebaseApp.getApps().isNotEmpty()) {
            return FirebaseApp.getInstance()
        }

        val inputStream: InputStream? = resolveCredentialsStream()

        if (inputStream == null) {
            logger.warn("Firebase credentials not found. Firebase messaging will be disabled.")
            return null
        }

        return try {
            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .build()
            val app = FirebaseApp.initializeApp(options)
            logger.info("Firebase initialized successfully.")
            app
        } catch (e: Exception) {
            logger.error("Failed to initialize Firebase: ${e.message}", e)
            null
        }
    }

    private fun resolveCredentialsStream(): InputStream? {
        // 1. Direct JSON (or Base64) via environment variable or property
        if (credentialsJson.isNotBlank()) {
            return try {
                val trimmed = credentialsJson.trim()
                if (trimmed.startsWith("{")) {
                    ByteArrayInputStream(trimmed.toByteArray(Charsets.UTF_8))
                } else {
                    val decoded = Base64.getDecoder().decode(trimmed)
                    ByteArrayInputStream(decoded)
                }
            } catch (e: Exception) {
                logger.error("Error parsing firebase.credentials.json: ${e.message}")
                null
            }
        }

        // 2. Explicit file path from env/property
        if (credentialsPath.isNotBlank()) {
            val file = File(credentialsPath)
            if (file.exists()) {
                logger.info("Loading Firebase credentials from file: $credentialsPath")
                return FileInputStream(file)
            }
        }

        // 3. Classpath resource (inside JAR)
        try {
            val resource = ClassPathResource("serviceAccountKey.json")
            if (resource.exists()) {
                logger.info("Loading Firebase credentials from classpath: serviceAccountKey.json")
                return resource.inputStream
            }
        } catch (_: Exception) {
        }

        // 4. Local filesystem fallback (local development)
        val localFile = File("src/main/resources/serviceAccountKey.json")
        if (localFile.exists()) {
            logger.info("Loading Firebase credentials from local filesystem: ${localFile.absolutePath}")
            return FileInputStream(localFile)
        }

        return null
    }
}