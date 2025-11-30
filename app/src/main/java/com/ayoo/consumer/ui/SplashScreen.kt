package com.ayoo.consumer.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ayoo.consumer.viewmodel.SessionState
import com.ayoo.consumer.viewmodel.UserViewModel

@Composable
fun SplashScreen(navController: NavController, userViewModel: UserViewModel = viewModel()) {
    val sessionState by userViewModel.sessionState.collectAsState()

    // This will run once when the composable enters the composition to check the session
    LaunchedEffect(Unit) {
        userViewModel.validateSession()
    }

    // This will react to the result of the session check
    LaunchedEffect(sessionState) {
        if (sessionState is SessionState.Valid) {
            val destination =
                if ((sessionState as SessionState.Valid).isValid) "shop" else "onboarding"
            navController.navigate(destination) {
                // Pop up to the start destination of the graph to remove the splash screen from the back stack
                popUpTo(navController.graph.startDestinationId) { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}