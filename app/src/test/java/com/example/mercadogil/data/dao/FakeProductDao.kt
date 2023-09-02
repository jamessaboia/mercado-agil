package com.example.mercadogil.data.dao

import com.example.mercadogil.data.entity.ProductEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class FakeProductDao: ProductDao {
    private var productEntity: ProductEntity? = null
    private var productList: MutableList<ProductEntity> = mutableListOf()
    override suspend fun insertProduct(productEntity: ProductEntity) {
        productList.add(productEntity)
    }

    override fun getAllProducts(): Flow<List<ProductEntity>> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteProduct(product: ProductEntity) {
        productList.remove(product)
    }

    override suspend fun deleteAllProducts() {
        TODO("Not yet implemented")
    }
}