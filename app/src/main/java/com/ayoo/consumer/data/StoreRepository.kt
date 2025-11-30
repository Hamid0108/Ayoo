package com.ayoo.consumer.data

import com.ayoo.consumer.model.Products
import com.ayoo.consumer.model.StoreInfo
import com.backendless.Backendless
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import com.backendless.persistence.DataQueryBuilder
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class StoreRepository {
    suspend fun getStores(): List<StoreInfo> = suspendCoroutine { continuation ->
        Backendless.Data.of(StoreInfo::class.java).find(object : AsyncCallback<List<StoreInfo>> {
            override fun handleResponse(response: List<StoreInfo>?) {
                continuation.resume(response ?: emptyList())
            }

            override fun handleFault(fault: BackendlessFault?) {
                continuation.resumeWithException(
                    Exception(
                        fault?.message ?: "An unknown error occurred"
                    )
                )
            }
        })
    }

    suspend fun getProductsForStore(storeId: String): List<Products> =
        suspendCoroutine { continuation ->
            // This is the corrected query based on your schema.
            // It looks for Products where the 'merchantId' column matches the store's objectId.
            val whereClause = "merchantId = '$storeId'"
            val queryBuilder = DataQueryBuilder.create().setWhereClause(whereClause)

            Backendless.Data.of(Products::class.java)
                .find(queryBuilder, object : AsyncCallback<List<Products>> {
                    override fun handleResponse(response: List<Products>?) {
                        continuation.resume(response ?: emptyList())
                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        continuation.resumeWithException(
                            Exception(
                                fault?.message ?: "An error occurred fetching products"
                            )
                        )
                    }
                })
        }

    suspend fun searchStores(query: String): List<StoreInfo> = suspendCoroutine { continuation ->
        val whereClause = "storeName LIKE '%$query%'"
        val queryBuilder = DataQueryBuilder.create().setWhereClause(whereClause)
        Backendless.Data.of(StoreInfo::class.java)
            .find(queryBuilder, object : AsyncCallback<List<StoreInfo>> {
                override fun handleResponse(response: List<StoreInfo>?) {
                    continuation.resume(response ?: emptyList())
                }

                override fun handleFault(fault: BackendlessFault?) {
                    continuation.resumeWithException(
                        Exception(
                            fault?.message ?: "An unknown error occurred"
                        )
                    )
                }
            })
    }

    suspend fun searchProducts(query: String): List<Products> = suspendCoroutine { continuation ->
        val whereClause = "name LIKE '%$query%'"
        val queryBuilder = DataQueryBuilder.create().setWhereClause(whereClause)
        Backendless.Data.of(Products::class.java)
            .find(queryBuilder, object : AsyncCallback<List<Products>> {
                override fun handleResponse(response: List<Products>?) {
                    continuation.resume(response ?: emptyList())
                }

                override fun handleFault(fault: BackendlessFault?) {
                    continuation.resumeWithException(
                        Exception(
                            fault?.message ?: "An unknown error occurred"
                        )
                    )
                }
            })
    }
}
