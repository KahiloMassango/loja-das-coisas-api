package org.example.loja_das_coisas_api.notification.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.example.loja_das_coisas_api.auth.model.User
import org.example.loja_das_coisas_api.shared.model.BaseEntity


@Entity
@Table(name = "user_devices")
class UserDevice(

    @Column(nullable = false, unique = true)
    var deviceToken: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var deviceType: DeviceType,

    var isActive: Boolean = true,

    @OneToOne(cascade = [(CascadeType.ALL)])
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    var user: User

): BaseEntity()

enum class DeviceType {
    ANDROID, IOS, WEB
}