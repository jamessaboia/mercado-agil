package com.example.mercadogil.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long? = 0L,
    val name: String,
    val price: Double,
    val amount: Int,
    val image: String? = null
) : Serializable
