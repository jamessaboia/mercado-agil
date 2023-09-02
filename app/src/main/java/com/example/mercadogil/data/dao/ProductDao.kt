package com.example.mercadogil.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.mercadogil.data.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(productEntity: ProductEntity)

    @Query("SELECT * FROM ProductEntity")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("DELETE FROM ProductEntity")
    suspend fun deleteAllProducts()
}