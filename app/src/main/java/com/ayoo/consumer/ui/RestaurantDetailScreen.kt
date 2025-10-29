package com.ayoo.consumer.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ayoo.consumer.model.Restaurant
import com.ayoo.consumer.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantDetailScreen(
    navController: NavController,
    restaurant: Restaurant,
    cartViewModel: CartViewModel
) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(restaurant.name) })
        },
        bottomBar = {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = { navController.navigate("cart") }
            ) { Text("View Cart") }
        }
    ) { padding ->
        LazyColumn(contentPadding = padding) {
            items(restaurant.menu.size) { index ->
                val item = restaurant.menu[index]
                ListItem(
                    headlineContent = { Text(item.name) },
                    supportingContent = { Text("₱${item.price}") },
                    trailingContent = {
                        Button(onClick = { cartViewModel.addItem(item) }) {
                            Text("+ Add")
                        }
                    }
                )
            }
        }
    }
}
