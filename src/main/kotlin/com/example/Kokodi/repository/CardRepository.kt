package com.example.Kokodi.repository;

import com.example.Kokodi.entity.Card
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CardRepository : JpaRepository<Card, String> {

}