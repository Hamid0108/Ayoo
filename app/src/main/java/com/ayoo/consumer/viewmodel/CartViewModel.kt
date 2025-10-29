package com.ayoo.consumer.viewmodel

import androidx.lifecycle.ViewModel
import com.ayoo.consumer.model.CartItem
import com.ayoo.consumer.model.MenuItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class CartViewModel : ViewModel() {
    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart

    fun addItem(item: MenuItem) {
        val updated = _cart.value.toMutableList()
        val existing = updated.find { it.item.id == item.id }
        if (existing != null) {
            val newItem = existing.copy(quantity = existing.quantity + 1)
            updated[updated.indexOf(existing)] = newItem
        } else {
            updated.add(CartItem(item, 1))
        }
        _cart.value = updated
    }

    fun clear() {
        _cart.value = emptyList()
    }
}
