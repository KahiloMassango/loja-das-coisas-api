package org.example.loja_das_coisas_api.customer.service

import jakarta.transaction.Transactional
import org.example.loja_das_coisas_api.auth.model.User
import org.example.loja_das_coisas_api.auth.model.UserRole
import org.example.loja_das_coisas_api.auth.repository.UserRepository
import org.example.loja_das_coisas_api.customer.dto.CustomerProfileResponse
import org.example.loja_das_coisas_api.customer.dto.CustomerRegisterRequest
import org.example.loja_das_coisas_api.customer.dto.CustomerUpdateRequest
import org.example.loja_das_coisas_api.customer.model.CustomerProfile
import org.example.loja_das_coisas_api.customer.repository.CustomerRepository
import org.example.loja_das_coisas_api.exception.AlreadyExistsException
import org.example.loja_das_coisas_api.exception.EntityNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomerService(
    private val customerRepository: CustomerRepository,
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder
) {

    fun create(request: CustomerRegisterRequest): CustomerProfileResponse {
        if (userRepository.findByEmailOrPhoneNumberAndDeletedFalse(request.email) != null) {
            throw AlreadyExistsException("Email ou número de telemóvel já em uso!")
        }

        val user = User(
            email = request.email,
            phoneNumber = request.phoneNumber,
            password = encoder.encode(request.password),
            role = UserRole.CUSTOMER,
        )
        val savedUser = userRepository.saveAndFlush(user)

        val customer = CustomerProfile(
            username = request.username, user = savedUser
        )
        return customerRepository.saveAndFlush(customer).toResponse()
    }

    fun updatePassword(identifier: String, newPassword: String) {
        val customer = userRepository.findByEmailOrPhoneNumberAndDeletedFalse(identifier)
            ?: throw EntityNotFoundException("Usuário não encontrado!")
        customer.password = encoder.encode(newPassword)
        userRepository.save(customer)
    }

    fun getDetails(email: String): CustomerProfileResponse {
        val customer = customerRepository.findByEmail(email)!!
        return customer.toResponse()
    }

    fun delete(email: String) {
        val customer = customerRepository.findByEmail(email)!!
        customer.apply { deleted = true }
        customerRepository.save(customer)
    }

    @Transactional
    fun update(email: String, request: CustomerUpdateRequest): CustomerProfileResponse {
        val user = userRepository.findByEmailOrPhoneNumberAndDeletedFalse(email)
            ?: throw EntityNotFoundException("Usuário não encontrado!")
        val customer = customerRepository.findByEmail(email)!!

        user.phoneNumber = request.phoneNumber
        user.email = request.email
        customer.username = request.username

        userRepository.save(user)
        return customerRepository.save(customer).toResponse()
    }
}

fun CustomerProfile.toResponse() = CustomerProfileResponse(
    email = user.email,
    username = username,
    phoneNumber = user.phoneNumber,
)
