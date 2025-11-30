package com.ayoo.consumer.model

data class CartItem(
    var objectId: String? = null,
    var ownerId: String? = null,
    var product: Products? = null, // Changed to nullable to prevent crashes from orphaned cart items
    var quantity: Int
)
