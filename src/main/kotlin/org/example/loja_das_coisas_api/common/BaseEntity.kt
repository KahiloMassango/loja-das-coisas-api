package org.example.loja_das_coisas_api.common

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.Instant
import java.util.*

@MappedSuperclass
class BaseEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null,

    @CreationTimestamp
    var createdAt: Instant? = null,

    @UpdateTimestamp
    //@Column(nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT NOW()")
    var updatedAt: Instant? = null
)
