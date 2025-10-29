package com.ayoo.consumer.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ayoo.consumer.R

@Composable
fun HomeScreen(navController: NavController, restaurants: List<com.ayoo.consumer.model.Restaurant>) {
    Scaffold(
        bottomBar = { BottomNavigationBar(navController) }
    ) {
        Column(modifier = Modifier.padding(it).padding(horizontal = 16.dp).verticalScroll(rememberScrollState())) {
            Header(navController)
            Spacer(modifier = Modifier.height(16.dp))
            SearchBar()
            Spacer(modifier = Modifier.height(16.dp))
            ShopCategory()
            Spacer(modifier = Modifier.height(24.dp))
            Categories()
            Spacer(modifier = Modifier.height(24.dp))
            LimitedDeals()
        }
    }
}

@Composable
fun Header(navController: NavController) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Magenta)
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text("Deliver to", fontSize = 12.sp)
            Text("San roque, Iligan city", fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_background),
            contentDescription = "Profile",
            modifier = Modifier.size(40.dp).clip(CircleShape).clickable { navController.navigate("profile") }
        )
    }
}

@Composable
fun SearchBar() {
    OutlinedTextField(
        value = "",
        onValueChange = {},
        placeholder = { Text("what are you looking for?") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
        trailingIcon = { Icon(painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "Filter") },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(32.dp)
    )
}

@Composable
fun ShopCategory() {
    val categories = listOf("Food", "Grocery", "Pharmacy", "Pabili")
    Column {
        Text("Shop Category", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(categories) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = it,
                        modifier = Modifier.size(80.dp).clip(RoundedCornerShape(16.dp)),
                        contentScale = ContentScale.Crop
                    )
                    Text(it, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun Categories() {
    val categories = listOf("Fast Food", "Filipino Meals", "Desserts", "Halal", "Pizza", "Ice Cream")
    Column {
        Text("Categories", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(categories) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_launcher_background),
                        contentDescription = it,
                        modifier = Modifier.size(64.dp).clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Text(it, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun LimitedDeals() {
    Column {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Limited Deals", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.weight(1f))
            TextButton(onClick = { /*TODO*/ }) {
                Text("See All", color = Color.Magenta)
            }
        }
        // TODO: Add content for Limited Deals
    }
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = true,
            onClick = { /* TODO */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Cart") },
            label = { Text("Cart") },
            selected = false,
            onClick = { navController.navigate("cart") }
        )
        NavigationBarItem(
            icon = { Icon(painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "MyFind") },
            label = { Text("MyFind") },
            selected = false,
            onClick = { /* TODO */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            label = { Text("Search") },
            selected = false,
            onClick = { /* TODO */ }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = false,
            onClick = { navController.navigate("profile") }
        )
    }
}
