package com.ayoo.consumer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ayoo.consumer.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(navController: NavController, cartViewModel: CartViewModel) {
    val cartItems by cartViewModel.cart.collectAsState()
    val total = cartItems.sumOf { it.item.price * it.quantity }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Your Cart") }) },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = { navController.navigate("checkout") }
            ) {
                Text("Checkout ₱$total")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding)) {
            items(cartItems.size) { i ->
                val cart = cartItems[i]
                ListItem(
                    headlineContent = { Text(cart.item.name) },
                    supportingContent = { Text("₱${cart.item.price} × ${cart.quantity}") }
                )
            }
        }
    }
}
