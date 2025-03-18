package org.example.loja_das_coisas_api.services.impl

import jakarta.transaction.Transactional
import org.example.loja_das_coisas_api.dtos.requests.CustomerDtoReq
import org.example.loja_das_coisas_api.dtos.requests.CustomerUpdateDtoReq
import org.example.loja_das_coisas_api.dtos.responses.CustomerDtoRes
import org.example.loja_das_coisas_api.exceptions.AlreadyExistsException
import org.example.loja_das_coisas_api.exceptions.EntityNotFoundException
import org.example.loja_das_coisas_api.models.Customer
import org.example.loja_das_coisas_api.models.User
import org.example.loja_das_coisas_api.models.UserRole
import org.example.loja_das_coisas_api.models.mappers.toDtoRes
import org.example.loja_das_coisas_api.repositories.CustomerRepository
import org.example.loja_das_coisas_api.repositories.UserRepository
import org.example.loja_das_coisas_api.services.interfaces.CustomerService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CustomerServiceImpl(
    private val customerRepository: CustomerRepository,
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder
) : CustomerService {


    override fun create(request: CustomerDtoReq): CustomerDtoRes {
        // validate email or phone number already in use
        if (userRepository.findByEmailOrPhoneNumberAndDeletedFalse(request.email) != null) {
            throw AlreadyExistsException("Email o u número de telemóvel já em uso!")
        }


        val user = User(
            email = request.email,
            phoneNumber = request.phoneNumber,
            password = encoder.encode(request.password),
            role = UserRole.CUSTOMER,
        )
        val savedUser = userRepository.saveAndFlush(user)

        val customer = Customer(
            username = request.username,
            user = savedUser
        )
        return customerRepository.saveAndFlush(customer).toDtoRes()
    }

    override fun updatePassword(identifier: String, newPassword: String) {
        val customer = userRepository.findByEmailOrPhoneNumberAndDeletedFalse(identifier)
            ?: throw EntityNotFoundException("Usuário não encontrado!")

        customer.password = encoder.encode(newPassword)
        userRepository.save(customer)
    }

    override fun getDetails(email: String): CustomerDtoRes {
        val customer = customerRepository.findByEmail(email)!!

        return customer.toDtoRes()
    }

    override fun delete(email: String) {
        val customer = customerRepository.findByEmail(email)!!
        customer.apply {
            deleted = true
        }
        customerRepository.save(customer)
    }

    @Transactional
    override fun update(email: String, request: CustomerUpdateDtoReq): CustomerDtoRes {
        val user = userRepository.findByEmailOrPhoneNumberAndDeletedFalse(email)
            ?: throw EntityNotFoundException("Usuário não encontrado!")

        val customer = customerRepository.findByEmail(email)!!

        user.phoneNumber = request.phoneNumber
        user.email = request.email
        customer.username = request.username


        userRepository.save(user)

        return customerRepository.save(customer).toDtoRes() // Safe since we check null before
    }

}