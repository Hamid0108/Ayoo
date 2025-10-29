package com.ayoo.consumer.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.ayoo.consumer.R

@Composable
fun OnboardingScreen(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        // Logo
        Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "Ayoo Logo")

        // Illustration
        Image(painter = painterResource(id = R.drawable.ic_launcher_background), contentDescription = "Welcome Dog")

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Welcome", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Hello nice to see you! Welcome to our new food delivery app in Iligan city.",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge
            )
        }

        Button(
            onClick = { navController.navigate("login") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Get Started")
        }
    }
}
