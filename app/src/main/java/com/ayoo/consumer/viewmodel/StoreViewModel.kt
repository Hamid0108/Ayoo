package com.ayoo.consumer.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayoo.consumer.data.StoreRepository
import com.ayoo.consumer.model.Products
import com.ayoo.consumer.model.StoreInfo
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

sealed class StoreState {
    object Loading : StoreState()
    data class Success(val stores: List<StoreInfo>) : StoreState()
    data class Error(val message: String) : StoreState()
}

sealed class ProductState {
    object Loading : ProductState()
    data class Success(val products: List<Products>) : ProductState()
    data class Error(val message: String) : ProductState()
}

sealed class SearchState {
    object Idle : SearchState()
    object Loading : SearchState()
    data class Success(val stores: List<StoreInfo>, val products: List<Products>) : SearchState()
    data class Error(val message: String) : SearchState()
}

class StoreViewModel : ViewModel() {
    private val repository = StoreRepository()

    private val _storeState = MutableStateFlow<StoreState>(StoreState.Loading)
    val storeState: StateFlow<StoreState> = _storeState

    private val _productState = MutableStateFlow<ProductState>(ProductState.Loading)
    val productState: StateFlow<ProductState> = _productState

    private val _searchState = MutableStateFlow<SearchState>(SearchState.Idle)
    val searchState: StateFlow<SearchState> = _searchState

    private var searchJob: Job? = null

    init {
        fetchStores()
    }

    fun fetchStores() {
        viewModelScope.launch {
            _storeState.value = StoreState.Loading
            try {
                // Simulate network delay for better UX on refresh
                delay(1000)
                _storeState.value = StoreState.Success(repository.getStores())
            } catch (e: Exception) {
                _storeState.value = StoreState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun fetchProductsForStore(storeId: String) {
        viewModelScope.launch {
            _productState.value = ProductState.Loading
            try {
                _productState.value = ProductState.Success(repository.getProductsForStore(storeId))
            } catch (e: Exception) {
                _productState.value = ProductState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }

    fun search(query: String) {
        searchJob?.cancel() // Cancel previous search job
        if (query.isBlank()) {
            _searchState.value = SearchState.Idle
            return
        }
        searchJob = viewModelScope.launch {
            delay(300) // Debounce to avoid searching on every keystroke
            _searchState.value = SearchState.Loading
            try {
                val stores = repository.searchStores(query)
                val products = repository.searchProducts(query)
                _searchState.value = SearchState.Success(stores, products)
            } catch (e: Exception) {
                _searchState.value = SearchState.Error(e.message ?: "An unknown error occurred")
            }
        }
    }
}
