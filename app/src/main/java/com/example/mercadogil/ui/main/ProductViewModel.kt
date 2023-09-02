package com.example.mercadogil.ui.main

import androidx.lifecycle.*
import com.example.mercadogil.data.entity.ProductEntity
import com.example.mercadogil.data.repository.ProductRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val productRepository: ProductRepository
) : ViewModel() {

    private val _productList: MutableLiveData<List<ProductEntity>> = MutableLiveData()
//    val productList: LiveData<List<ProductEntity>> get() = _productList

    fun insertProduct(productEntity: ProductEntity) = viewModelScope.launch {
        productRepository.insertProduct(productEntity)
    }

    fun loadData() = viewModelScope.launch {
        productRepository.getAllProducts().collectLatest {
            _productList.postValue(it)
        }
    }

    fun getAllProducts() = _productList

    fun deleteProduct(product: ProductEntity) = viewModelScope.launch {
        productRepository.deleteProduct(product)
        loadData()
    }

    fun deleteAllProducts() = viewModelScope.launch {
        productRepository.deleteAllProducts()
        loadData()
    }

    fun getTotalPrice(): Double {
        var totalPrice = 0.0
        _productList.value?.forEach { product ->
            totalPrice += product.price * product.amount
        }
        return totalPrice
    }

    fun isProductListEmpty(): Boolean {
        return _productList.value.isNullOrEmpty()
    }
}