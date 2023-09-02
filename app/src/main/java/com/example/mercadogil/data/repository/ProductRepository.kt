package com.example.mercadogil.data.repository

import androidx.lifecycle.LiveData
import com.example.mercadogil.data.dao.ProductDao
import com.example.mercadogil.data.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ProductRepository @Inject constructor(private val productDao: ProductDao) {

    suspend fun insertProduct(productEntity: ProductEntity) {
        productDao.insertProduct(productEntity)
    }

    fun getAllProducts(): Flow<List<ProductEntity>> {
        return productDao.getAllProducts()
    }

    suspend fun deleteProduct(product: ProductEntity) {
        productDao.deleteProduct(product)
    }

    suspend fun deleteAllProducts() {
        productDao.deleteAllProducts()
    }
}