package com.ayoo.consumer

import com.ayoo.consumer.model.Restaurant
import com.ayoo.consumer.model.MenuItem
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.*
import com.ayoo.consumer.model.*
import com.ayoo.consumer.ui.*
import com.ayoo.consumer.ui.theme.AyooTheme
import com.ayoo.consumer.viewmodel.CartViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // installSplashScreen() // Temporarily disabled for diagnostics
        setContent {
            AyooTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    val cartViewModel: CartViewModel = viewModel()
                    val navController = rememberNavController()

                    val mockRestaurants = listOf(
                        Restaurant(
                            id = "1",
                            name = "Pizza Planet",
                            category = "Pizza",
                            menu = listOf(
                                MenuItem("m1", "Pepperoni Pizza", 299.0),
                                MenuItem("m2", "Cheese Pizza", 249.0)
                            )
                        ),
                        Restaurant(
                            id = "2",
                            name = "Ayoo Chicken",
                            category = "Fried Chicken",
                            menu = listOf(
                                MenuItem("m3", "Chicken Bucket", 499.0),
                                MenuItem("m4", "Wings", 199.0)
                            )
                        )
                    )

                    NavHost(navController, startDestination = "onboarding") {
                        composable("onboarding") {
                            OnboardingScreen(navController)
                        }
                        composable("login") {
                            LoginScreen(navController)
                        }
                        composable("create_account") {
                            CreateAccountScreen(navController)
                        }
                        composable("home") {
                            HomeScreen(navController, mockRestaurants)
                        }
                        composable("profile") {
                            ProfileScreen(navController)
                        }
                        composable("restaurant/{id}") { backStackEntry ->
                            val id = backStackEntry.arguments?.getString("id")
                            val restaurant = mockRestaurants.find { it.id == id }!!
                            RestaurantDetailScreen(navController, restaurant, cartViewModel)
                        }
                        composable("cart") {
                            CartScreen(navController, cartViewModel)
                        }
                        composable("checkout") {
                            CheckoutScreen(navController, cartViewModel)
                        }
                    }
                }
            }
        }
    }
}
