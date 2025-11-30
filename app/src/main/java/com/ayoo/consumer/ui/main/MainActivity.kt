package com.ayoo.consumer.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ayoo.consumer.ui.AddressSelectionScreen
import com.ayoo.consumer.ui.CartScreen
import com.ayoo.consumer.ui.CheckoutScreen
import com.ayoo.consumer.ui.CreateAccountScreen
import com.ayoo.consumer.ui.HomeScreen
import com.ayoo.consumer.ui.LoginScreen
import com.ayoo.consumer.ui.MapPinpointScreen
import com.ayoo.consumer.ui.OnboardingScreen
import com.ayoo.consumer.ui.ProfileScreen
import com.ayoo.consumer.ui.SplashScreen
import com.ayoo.consumer.ui.StoreDetailScreen
import com.ayoo.consumer.ui.theme.AyooTheme
import com.ayoo.consumer.viewmodel.CartViewModel
import com.ayoo.consumer.viewmodel.StoreState
import com.ayoo.consumer.viewmodel.StoreViewModel
import com.ayoo.consumer.viewmodel.ThemeViewModel
import java.net.URLDecoder

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val themeViewModel: ThemeViewModel = viewModel()
            val isDarkTheme by themeViewModel.isDarkTheme

            AyooTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController, startDestination = "splash") {
                        composable("splash") { SplashScreen(navController) }
                        composable("onboarding") { OnboardingScreen(navController) }
                        composable("login") { LoginScreen(navController) }
                        composable("create_account") { CreateAccountScreen(navController) }
                        composable("profile") { ProfileScreen(navController, themeViewModel) }
                        composable(
                            "address_selection/{address}",
                            arguments = listOf(navArgument("address") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val address = URLDecoder.decode(
                                backStackEntry.arguments?.getString("address") ?: "", "UTF-8"
                            )
                            AddressSelectionScreen(navController, initialAddress = address)
                        }
                        composable("map_pinpoint") { MapPinpointScreen(navController) }

                        navigation(startDestination = "home", route = "shop") {
                            composable("home") { backStackEntry ->
                                val parentEntry =
                                    remember(backStackEntry) { navController.getBackStackEntry("shop") }
                                val storeViewModel: StoreViewModel = viewModel(parentEntry)
                                val cartViewModel: CartViewModel = viewModel(parentEntry)
                                val storeState by storeViewModel.storeState.collectAsState()

                                when (val state = storeState) {
                                    is StoreState.Loading -> {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            CircularProgressIndicator()
                                        }
                                    }

                                    is StoreState.Success -> {
                                        HomeScreen(
                                            navController,
                                            state.stores,
                                            storeViewModel,
                                            cartViewModel
                                        )
                                    }

                                    is StoreState.Error -> {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(state.message)
                                        }
                                    }
                                }
                            }
                            composable("cart") { backStackEntry ->
                                val parentEntry =
                                    remember(backStackEntry) { navController.getBackStackEntry("shop") }
                                val cartViewModel: CartViewModel = viewModel(parentEntry)
                                CartScreen(navController, cartViewModel)
                            }
                            composable("checkout") { backStackEntry ->
                                val parentEntry =
                                    remember(backStackEntry) { navController.getBackStackEntry("shop") }
                                val cartViewModel: CartViewModel = viewModel(parentEntry)
                                CheckoutScreen(navController, cartViewModel)
                            }
                            composable("store/{storeId}") { backStackEntry ->
                                val parentEntry =
                                    remember(backStackEntry) { navController.getBackStackEntry("shop") }
                                val cartViewModel: CartViewModel = viewModel(parentEntry)
                                val storeViewModel: StoreViewModel = viewModel(parentEntry)

                                val storeId = backStackEntry.arguments?.getString("storeId") ?: ""
                                val storeState by storeViewModel.storeState.collectAsState()
                                val productState by storeViewModel.productState.collectAsState()

                                LaunchedEffect(storeId) {
                                    storeViewModel.fetchProductsForStore(storeId)
                                }

                                val storeInfo =
                                    (storeState as? StoreState.Success)?.stores?.find { it.objectId == storeId }

                                if (storeInfo != null) {
                                    StoreDetailScreen(
                                        navController = navController,
                                        storeInfo = storeInfo,
                                        productState = productState,
                                        cartViewModel = cartViewModel
                                    )
                                } else {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        CircularProgressIndicator() // Or Text("Store not found")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
