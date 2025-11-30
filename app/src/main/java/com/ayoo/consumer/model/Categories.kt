package com.ayoo.consumer.model

import java.io.Serializable

data class Categories(
    var objectId: String? = null,
    var ownerId: String? = null,
    var description: String = "",
    var name: String = ""
) : Serializable
