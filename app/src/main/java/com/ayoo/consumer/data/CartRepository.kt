package com.ayoo.consumer.data

import com.ayoo.consumer.model.CartItem
import com.ayoo.consumer.model.Products
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class CartRepository {

    suspend fun getCartItems(userId: String): List<CartItem> = suspendCoroutine { continuation ->
        val queryBuilder = DataQueryBuilder.create()
        queryBuilder.whereClause = "ownerId = '$userId'"
        queryBuilder.setRelated("product")

        Backendless.Data.of(CartItem::class.java)
            .find(queryBuilder, object : AsyncCallback<List<CartItem>> {
                override fun handleResponse(response: List<CartItem>?) {
                    continuation.resume(response ?: emptyList())
                }

                override fun handleFault(fault: BackendlessFault?) {
                    continuation.resumeWithException(
                        Exception(
                            fault?.message ?: "Failed to load cart"
                        )
                    )
                }
            })
    }

    private suspend fun findCartItem(userId: String, product: Products): CartItem? =
        suspendCoroutine { continuation ->
            val queryBuilder = DataQueryBuilder.create()
            queryBuilder.whereClause =
                "ownerId = '$userId' AND product.objectId = '${product.objectId}'"
            // No need to get related product, we just need the objectId and quantity
            Backendless.Data.of(CartItem::class.java)
                .find(queryBuilder, object : AsyncCallback<List<CartItem>> {
                    override fun handleResponse(response: List<CartItem>?) {
                        continuation.resume(response?.firstOrNull())
                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        // If it fails to find, it's not a critical error, just means no item exists.
                        continuation.resume(null)
                    }
                })
        }

    private suspend fun saveCartItem(item: CartItem): CartItem = suspendCoroutine { continuation ->
        Backendless.Data.of(CartItem::class.java).save(item, object : AsyncCallback<CartItem> {
            override fun handleResponse(response: CartItem?) {
                if (response != null) {
                    continuation.resume(response)
                } else {
                    continuation.resumeWithException(Exception("Failed to save cart item: Response was null."))
                }
            }

            override fun handleFault(fault: BackendlessFault?) {
                continuation.resumeWithException(Exception(fault?.message ?: "Failed to save item"))
            }
        })
    }

    private suspend fun deleteCartItem(item: CartItem) {
        suspendCoroutine<Unit> { continuation ->
            Backendless.Data.of(CartItem::class.java).remove(item, object : AsyncCallback<Long> {
                override fun handleResponse(response: Long?) {
                    continuation.resume(Unit)
                }

                override fun handleFault(fault: BackendlessFault?) {
                    continuation.resumeWithException(
                        Exception(
                            fault?.message ?: "Failed to delete item"
                        )
                    )
                }
            })
        }
    }

    suspend fun addItemToCart(userId: String, product: Products) {
        val existingItem = findCartItem(userId, product)

        if (existingItem != null) {
            // --- UPDATE PATH ---
            // Create a new object for the update, only setting the fields that change.
            // This is the most robust way to prevent relationship issues.
            val itemToUpdate = CartItem(
                objectId = existingItem.objectId,
                quantity = existingItem.quantity + 1
            )
            saveCartItem(itemToUpdate)
        } else {
            // --- CREATE PATH ---
            // Create a new item and set the relationship to the product.
            val newItem = CartItem(ownerId = userId, product = product, quantity = 1)
            saveCartItem(newItem)
        }
    }

    suspend fun removeItemFromCart(userId: String, product: Products) {
        val existingItem = findCartItem(userId, product)

        if (existingItem != null) {
            if (existingItem.quantity > 1) {
                // --- UPDATE PATH ---
                val itemToUpdate = CartItem(
                    objectId = existingItem.objectId,
                    quantity = existingItem.quantity - 1
                )
                saveCartItem(itemToUpdate)
            } else {
                // --- DELETE PATH ---
                deleteCartItem(existingItem)
            }
        }
    }
}
