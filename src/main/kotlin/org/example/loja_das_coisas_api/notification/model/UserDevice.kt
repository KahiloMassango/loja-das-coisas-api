package org.example.loja_das_coisas_api.notification.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import org.example.loja_das_coisas_api.auth.model.User
import org.example.loja_das_coisas_api.shared.model.BaseEntity


@Entity
@Table(name = "user_devices")
class UserDevice(

    @Column(nullable = false)
    var deviceToken: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var deviceType: DeviceType,

    var isActive: Boolean = true,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    var user: User

): BaseEntity()

enum class DeviceType {
    ANDROID, IOS, WEB
}