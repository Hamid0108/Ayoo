package com.ayoo.consumer.model

data class Restaurant(
    val id: String,
    val name: String,
    val category: String,
    val menu: List<MenuItem>
)
