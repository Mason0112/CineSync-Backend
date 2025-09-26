package org.example.mason.movie.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.mason.movie.model.enum.UsersRole

@Entity
@Table(name = "users")
data class Users (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動產生 ID
    var id: Long? = null,
    var userName: String,
    @Column(unique = true)
    var email: String,
    var password: String,
    @Enumerated(EnumType.STRING)
    var usersRole: UsersRole
)