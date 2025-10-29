package com.ayoo.consumer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ayoo.consumer.viewmodel.CartViewModel

@Composable
fun CheckoutScreen(navController: NavController, cartViewModel: CartViewModel) {
    val cartItems = cartViewModel.cart.value
    val total = cartItems.sumOf { it.item.price * it.quantity }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Order Summary", style = MaterialTheme.typography.headlineSmall)
            Spacer(Modifier.height(8.dp))
            cartItems.forEach {
                Text("${it.item.name} × ${it.quantity} = ₱${it.item.price * it.quantity}")
            }
            Spacer(Modifier.height(16.dp))
            Text("Total: ₱$total", style = MaterialTheme.typography.titleMedium)
        }

        Button(
            onClick = {
                cartViewModel.clear()
                navController.popBackStack("home", inclusive = false)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Place Order")
        }
    }
}
