package com.ayoo.consumer.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ayoo.consumer.R
import com.ayoo.consumer.model.Products
import com.ayoo.consumer.model.StoreInfo
import com.ayoo.consumer.viewmodel.CartViewModel
import com.ayoo.consumer.viewmodel.ProductState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreDetailScreen(
    navController: NavController,
    storeInfo: StoreInfo,
    productState: ProductState,
    cartViewModel: CartViewModel
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val totalItemsInCart = cartItems.sumOf { it.quantity }
    val updatingProductId by cartViewModel.updatingProductId.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(storeInfo.storeName ?: "Store Details") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            if (totalItemsInCart > 0) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(50.dp),
                    onClick = { navController.navigate("cart") },
                    shape = RoundedCornerShape(50)
                ) {
                    Text("View Cart ($totalItemsInCart)")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp)
        ) {
            item { StoreHeader(storeInfo) }
            item { Spacer(modifier = Modifier.height(16.dp)) }

            when (productState) {
                is ProductState.Loading -> {
                    item {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }

                is ProductState.Error -> {
                    item { Text("Error: ${productState.message}") }
                }

                is ProductState.Success -> {
                    if (productState.products.isEmpty()) {
                        item { Text("No products found for this store.") }
                    } else {
                        items(productState.products, key = { it.objectId ?: "" }) { product ->
                            val cartItem =
                                cartItems.find { it.product?.objectId == product.objectId }
                            ProductCard(
                                product = product,
                                quantityInCart = cartItem?.quantity ?: 0,
                                isUpdating = product.objectId == updatingProductId,
                                onAddToCart = { cartViewModel.addItem(product) },
                                onRemoveFromCart = { cartViewModel.removeItem(product) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StoreHeader(storeInfo: StoreInfo) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ayoo_logo), // Placeholder
            contentDescription = storeInfo.storeName,
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = storeInfo.storeName ?: "Unnamed Store",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = storeInfo.storeType ?: "Unknown Category",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = storeInfo.address ?: "No address provided",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Divider(modifier = Modifier.padding(top = 16.dp))
    }
}

@Composable
private fun ProductCard(
    product: Products,
    quantityInCart: Int,
    isUpdating: Boolean,
    onAddToCart: () -> Unit,
    onRemoveFromCart: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name ?: "Unnamed Product",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "₱${"%.2f".format(product.price)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Box(
                modifier = Modifier.sizeIn(minWidth = 100.dp, minHeight = 48.dp),
                contentAlignment = Alignment.Center
            ) {
                Crossfade(targetState = isUpdating, label = "AddButtonCrossfade") { updating ->
                    if (updating) {
                        CircularProgressIndicator(modifier = Modifier.size(24.dp))
                    } else {
                        if (quantityInCart == 0) {
                            Button(onClick = onAddToCart) {
                                Text("Add")
                            }
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                IconButton(onClick = onRemoveFromCart) {
                                    Icon(
                                        Icons.Default.Remove,
                                        contentDescription = "Decrease quantity"
                                    )
                                }
                                Text(
                                    text = quantityInCart.toString(),
                                    modifier = Modifier.padding(horizontal = 8.dp),
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(onClick = onAddToCart) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = "Increase quantity"
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
