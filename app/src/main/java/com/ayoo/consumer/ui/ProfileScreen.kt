package com.ayoo.consumer.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ayoo.consumer.R

@Composable
fun ProfileScreen(navController: NavController) {
    val profileOptions = listOf(
        "My Account",
        "Payment Methods",
        "Order History",
        "Settings",
        "Help Center",
        "Log Out"
    )

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // User Info
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = "Profile Picture",
                modifier = Modifier.size(80.dp).clip(CircleShape)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text("John Doe", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text("Edit Profile", style = MaterialTheme.typography.bodyMedium, modifier = Modifier.clickable { /* TODO: Navigate to edit profile */ })
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Profile Options
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(profileOptions) {
                Card(modifier = Modifier.fillMaxWidth().clickable { /* TODO: Handle navigation */ }) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(it, modifier = Modifier.weight(1f))
                        Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null)
                    }
                }
            }
        }
    }
}
