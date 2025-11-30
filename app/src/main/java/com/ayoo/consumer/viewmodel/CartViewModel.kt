package com.ayoo.consumer.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayoo.consumer.data.CartRepository
import com.ayoo.consumer.model.CartItem
import com.ayoo.consumer.model.Products
import com.backendless.Backendless
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CartViewModel : ViewModel() {

    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems.asStateFlow()

    private val _totalPrice = MutableStateFlow(0.0)
    val totalPrice: StateFlow<Double> = _totalPrice.asStateFlow()

    private val _updatingProductId = MutableStateFlow<String?>(null)
    val updatingProductId: StateFlow<String?> = _updatingProductId.asStateFlow()

    private val cartRepository = CartRepository()
    private val currentUserId: String?
        get() = Backendless.UserService.CurrentUser()?.userId

    init {
        loadInitialCart()
    }

    fun loadInitialCart() {
        val userId = currentUserId ?: return
        viewModelScope.launch {
            try {
                val items = cartRepository.getCartItems(userId)
                _cartItems.value = items.filter { it.product != null }
                calculateTotalPrice()
            } catch (e: Exception) {
                Log.e("CartViewModel", "Failed to load cart", e)
                _cartItems.value = emptyList()
            }
        }
    }

    fun addItem(product: Products?) {
        val userId = currentUserId ?: return
        val productId = product?.objectId ?: return

        viewModelScope.launch {
            _updatingProductId.value = productId
            try {
                cartRepository.addItemToCart(userId, product)
                // After the backend call is successful, reload the cart
                // to get the single source of truth from the server.
                loadInitialCart()
            } catch (e: Exception) {
                Log.e("CartViewModel", "Failed to add item", e)
                // You could show an error message to the user here
            } finally {
                // Ensure the loading state is always cleared
                _updatingProductId.value = null
            }
        }
    }

    fun removeItem(product: Products?) {
        val userId = currentUserId ?: return
        val productId = product?.objectId ?: return

        viewModelScope.launch {
            _updatingProductId.value = productId
            try {
                cartRepository.removeItemFromCart(userId, product)
                loadInitialCart()
            } catch (e: Exception) {
                Log.e("CartViewModel", "Failed to remove item", e)
            } finally {
                _updatingProductId.value = null
            }
        }
    }

    private fun calculateTotalPrice() {
        val total = _cartItems.value.sumOf { (it.product?.price ?: 0.0) * it.quantity }
        _totalPrice.value = total
    }

    fun refreshCart() {
        loadInitialCart()
    }
}
