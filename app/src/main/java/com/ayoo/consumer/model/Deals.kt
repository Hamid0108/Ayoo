package com.ayoo.consumer.model

import java.io.Serializable
import java.util.Date

data class Deals(
    var objectId: String? = null,
    var ownerId: String? = null,
    var description: String = "",
    var name: String = "",
    var price: Double = 0.0,
    var rating: Double = 0.0,
    var startDate: Date? = null,
    var endDate: Date? = null,
    var dealType: String = ""
) : Serializable
