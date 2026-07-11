package org.example.loja_das_coisas_api.shared.infra.seeder

import jakarta.transaction.Transactional
import org.example.loja_das_coisas_api.auth.model.User
import org.example.loja_das_coisas_api.auth.model.UserRole
import org.example.loja_das_coisas_api.auth.repository.UserRepository
import org.example.loja_das_coisas_api.customer.model.CustomerProfile
import org.example.loja_das_coisas_api.customer.repository.CustomerRepository
import org.example.loja_das_coisas_api.store.model.StoreProfile
import org.example.loja_das_coisas_api.store.repository.StoreRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.core.annotation.Order
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.LocalTime


@Component
@Order(2)
class UserSeeder(
    private val userRepository: UserRepository,
    private val customerRepository: CustomerRepository,
    private val storeRepository: StoreRepository,
    private val passwordEncoder: PasswordEncoder,
) : CommandLineRunner {

    @Transactional
    override fun run(vararg args: String?) {
        if (userRepository.count() > 0) return

        val adminUser = User(
            email = "admin@gmail.com",
            password = passwordEncoder.encode("abc123"),
            role = UserRole.ADMIN,
            phoneNumber = "123456789",
            deleted = false,
            refreshToken = null
        )

        val customerUser = User(
            email = "customer@gmail.com",
            password = passwordEncoder.encode("abc123"),
            role = UserRole.CUSTOMER,
            phoneNumber = "987654321",
            deleted = false,
            refreshToken = null
        )

        val sellerUser = User(
            email = "seler@gmail.com",
            password = passwordEncoder.encode("abc123"),
            role = UserRole.STORE,
            phoneNumber = "987654322",
            deleted = false,
            refreshToken = null
        )

        val customerProfile = CustomerProfile(
            username = "Pedro Silva",
            deleted = false,
            user = customerUser
        )

        val storeProfile = StoreProfile(
            storeName = "Loja das Coisas",
            storeLogoUrl = "https://images.pexels.com/photos/5498225/pexels-photo-5498225.jpeg",
            description = "A Loja das Coisas é o seu destino ideal para uma variedade incrível de roupas e muito mais.Aqui, todos encontram algo especial para expressar seu estilo e atender suas necessidades.Venha explorar a diversidade de produtos que temos para oferecer!",
            nif = "ABFF34D21",
            latitude = -8.8849688,
            longitude = 13.2076069,
            address = "Av. 21 de Janeiro",
            openingTime = LocalTime.of(8, 0),
            closingTime = LocalTime.of(16, 0),
            active = true,
            offersDelivery = true,
            deleted = false,
            bankAccountName = "Loja das Coisas Lda",
            bankAccountIban = "AOO06044000006970897610185",
            fee = 5.0,
            user = sellerUser
        )

        userRepository.saveAndFlush(adminUser)
        userRepository.saveAndFlush(customerUser)
        userRepository.saveAndFlush(sellerUser)

        customerRepository.saveAndFlush(customerProfile)
        storeRepository.saveAndFlush(storeProfile)

    }
}