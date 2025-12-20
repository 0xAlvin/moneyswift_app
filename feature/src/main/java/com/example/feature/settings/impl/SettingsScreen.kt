package com.example.feature.settings.impl

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpCenter
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: SettingsViewModel = hiltViewModel(),
    onSignOutSuccess: () -> Unit,
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.signOutState.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(state) {
        if (state is SignOutState.Success) {
            onSignOutSuccess()
        }
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    SignOutAction(
                        isLoading = state is SignOutState.Loading,
                        onClick = { viewModel.signOut() }
                    )
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            HeaderSection("Account")
            SettingsItem(
                icon = Icons.Default.Person,
                title = "Profile Information",
                subtitle = "Name, Email, Phone number"
            )
            SettingsItem(
                icon = Icons.Default.LocationOn,
                title = "Shipping Addresses",
                subtitle = "Manage your delivery locations"
            )
            SettingsItem(
                icon = Icons.Default.Payment,
                title = "Payment Methods",
                subtitle = "Saved cards and billing info"
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

            HeaderSection("Preferences")
            var notificationsEnabled by remember { mutableStateOf(true) }
            SettingsSwitchItem(
                icon = Icons.Default.Notifications,
                title = "Push Notifications",
                subtitle = "Alerts for orders and promos",
                checked = notificationsEnabled,
                onCheckedChange = { notificationsEnabled = it }
            )
            SettingsItem(
                icon = Icons.Default.Language,
                title = "Language",
                subtitle = "English (United States)"
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.outlineVariant)

            HeaderSection("Support & Legal")
            SettingsItem(icon = Icons.AutoMirrored.Filled.HelpCenter, title = "Help Center")
            SettingsItem(icon = Icons.Default.Description, title = "Privacy Policy")
            SettingsItem(icon = Icons.Default.Info, title = "App Version", subtitle = "1.0.42 (Stable)")

            if (state is SignOutState.Error) {
                Text(
                    text = (state as SignOutState.Error).message ?: "Sign out failed",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp)
                )
            }

            Spacer(Modifier.height(48.dp))
        }
    }
}

@Composable
fun SignOutAction(isLoading: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier.padding(end = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.dp
            )
        } else {
            TextButton(
                onClick = onClick,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Sign Out")
            }
        }
    }
}

@Composable
fun HeaderSection(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit = {}
) {
    ListItem(
        modifier = Modifier.clickable { onClick() },
        headlineContent = { Text(title, fontWeight = FontWeight.Medium) },
        supportingContent = subtitle?.let { { Text(it) } },
        leadingContent = {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingContent = { Icon(Icons.Default.ChevronRight, contentDescription = null) }
    )
}

@Composable
fun SettingsSwitchItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    ListItem(
        headlineContent = { Text(title, fontWeight = FontWeight.Medium) },
        supportingContent = { Text(subtitle) },
        leadingContent = {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        trailingContent = {
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    )
}