package com.example.Kokodi.entity

import jakarta.persistence.*
import org.hibernate.proxy.HibernateProxy

@Entity
@Table(name = "users")
class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    @Column(name = "username")
    var username: String = "",
    @Column(name = "login")
    var login: String = "",
    @Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.USER,
    @Column(name = "password")
    var password: String = "",
    @ManyToMany(mappedBy = "players")
    var sessions: MutableList<GameSession> = mutableListOf(),
    @OneToMany(mappedBy = "createdBy")
    var createdSession: MutableList<GameSession> = mutableListOf()
)
