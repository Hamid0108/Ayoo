package com.ayoo.consumer.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.ayoo.consumer.R
import com.ayoo.consumer.viewmodel.ThemeViewModel
import com.ayoo.consumer.viewmodel.UserState
import com.ayoo.consumer.viewmodel.UserViewModel

@Composable
fun ProfileScreen(
    navController: NavController,
    themeViewModel: ThemeViewModel = viewModel(),
    userViewModel: UserViewModel = viewModel()
) {
    val isDarkTheme by themeViewModel.isDarkTheme
    val logoutState by userViewModel.logoutState.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }

    LaunchedEffect(logoutState) {
        if (logoutState is UserState.Success) {
            navController.navigate("login") {
                popUpTo("shop") { inclusive = true }
            }
        }
    }

    val profileOptions = listOf(
        ProfileOption("Edit Profile", Icons.Default.Edit, "edit_profile"),
        ProfileOption(
            "Notification Settings",
            Icons.Default.Notifications,
            "notification_settings"
        ),
        ProfileOption("Support", Icons.Default.HelpOutline, "support"),
        ProfileOption("Terms of Service", Icons.Default.Info, "terms_of_service"),
        ProfileOption("Log Out", Icons.AutoMirrored.Filled.ExitToApp, "logout")
    )

    Scaffold(
        bottomBar = { AyooBottomNavigationBar(navController = navController) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            item {
                Box {
                    Image(
                        painter = painterResource(id = R.drawable.ayoo_logo), // Placeholder
                        contentDescription = "Profile Banner",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentScale = ContentScale.Crop
                    )
                    Column(
                        modifier = Modifier
                            .padding(top = 150.dp, start = 16.dp, end = 16.dp)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ayoo_logo), // Placeholder
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(Color.White)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Andrew D.",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "andrew@domainname.com",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            items(profileOptions) { option ->
                ProfileListItem(option = option, onClick = {
                    if (option.route == "logout") {
                        showLogoutDialog = true
                    } else {
                        navController.navigate(option.route)
                    }
                })
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Dark Mode")
                    Switch(
                        checked = isDarkTheme,
                        onCheckedChange = { themeViewModel.storeTheme(it) }
                    )
                }
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Log Out") },
            text = { Text("Are you sure you want to log out?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    userViewModel.logoutUser()
                }) {
                    Text("Log Out")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

data class ProfileOption(
    val title: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val route: String
)

@Composable
fun ProfileListItem(option: ProfileOption, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .clickable { onClick() },
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(option.icon, contentDescription = null, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Text(option.title, modifier = Modifier.weight(1f))
            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null)
        }
    }
}
