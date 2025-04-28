package com.example.Kokodi.repository;

import com.example.Kokodi.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun existsByLogin(login: String): Boolean
    fun findByLogin(login: String): User?
}