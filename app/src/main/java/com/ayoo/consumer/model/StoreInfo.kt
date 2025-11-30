package com.ayoo.consumer.model

import java.io.Serializable

data class StoreInfo(
    var objectId: String? = null,
    var ownerId: String? = null,
    var address: String? = null,
    var description: String? = null,
    var logoURL: String? = null,
    var storeName: String? = null,
    var storeOpen: Boolean = false,
    var storeType: String? = null
) : Serializable
