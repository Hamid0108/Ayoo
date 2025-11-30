package com.ayoo.consumer.data

import com.backendless.Backendless
import com.backendless.BackendlessUser
import com.backendless.async.callback.AsyncCallback
import com.backendless.exceptions.BackendlessFault
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class UserRepository {

    suspend fun registerUser(user: BackendlessUser): BackendlessUser =
        suspendCoroutine { continuation ->
            Backendless.UserService.register(user, object : AsyncCallback<BackendlessUser> {
                override fun handleResponse(response: BackendlessUser?) {
                    if (response != null) {
                        continuation.resume(response)
                    } else {
                        continuation.resumeWithException(Exception("An unknown error occurred during registration."))
                    }
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

    suspend fun loginUser(email: String, password: String): BackendlessUser =
        suspendCoroutine { continuation ->
            Backendless.UserService.login(email, password, object : AsyncCallback<BackendlessUser> {
                override fun handleResponse(response: BackendlessUser?) {
                    if (response != null) {
                        continuation.resume(response)
                    } else {
                        continuation.resumeWithException(Exception("An unknown error occurred during login."))
                    }
                }

                override fun handleFault(fault: BackendlessFault?) {
                    continuation.resumeWithException(
                        Exception(
                            fault?.message ?: "An unknown error occurred"
                        )
                    )
                }
            }, true) // stayLoggedIn = true
        }

    suspend fun loginWithGoogle(idToken: String): BackendlessUser =
        suspendCoroutine { continuation ->
            // The first parameter is the provider code. For Google, it's "googleplus".
            // The second parameter is the access token (idToken) from the Google Sign-In result.
            // The third parameter is for optional fields mapping (null here).
            // The fourth parameter is the AsyncCallback.
            // The fifth parameter is stayLoggedIn (true).
            Backendless.UserService.loginWithOAuth2(
                "googleplus",
                idToken,
                null,
                object : AsyncCallback<BackendlessUser> {
                    override fun handleResponse(response: BackendlessUser?) {
                        if (response != null) {
                            continuation.resume(response)
                        } else {
                            continuation.resumeWithException(Exception("An unknown error occurred during Google login."))
                        }
                    }

                    override fun handleFault(fault: BackendlessFault?) {
                        continuation.resumeWithException(
                            Exception(
                                fault?.message ?: "An unknown error occurred"
                            )
                        )
                    }
                },
                true
            )
        }

    suspend fun isValidLogin(): Boolean = suspendCoroutine { continuation ->
        Backendless.UserService.isValidLogin(object : AsyncCallback<Boolean> {
            override fun handleResponse(response: Boolean?) {
                continuation.resume(response ?: false)
            }

            override fun handleFault(fault: BackendlessFault?) {
                continuation.resume(false)
            }
        })
    }

    suspend fun logoutUser(): Void? = suspendCoroutine { continuation ->
        Backendless.UserService.logout(object : AsyncCallback<Void?> {
            override fun handleResponse(response: Void?) {
                continuation.resume(response)
            }

            override fun handleFault(fault: BackendlessFault?) {
                continuation.resumeWithException(
                    Exception(
                        fault?.message ?: "An unknown error occurred during logout."
                    )
                )
            }
        })
    }
}
