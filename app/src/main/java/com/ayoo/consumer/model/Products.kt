package com.ayoo.consumer.model

import java.io.Serializable

data class Products(
    var objectId: String? = null,
    var ownerId: String? = null,
    var merchantId: String? = null, // This was the missing field
    var available: Boolean = false,
    var description: String? = null,
    var name: String? = null,
    var price: Double = 0.0,
    var rating: Double = 0.0,
    var stock: Double = 0.0,
    var imageUrl: String? = null
) : Serializable
