package com.example.Kokodi.repository;

import com.example.Kokodi.entity.Turn
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TurnRepository : JpaRepository<Turn, Long> {
}