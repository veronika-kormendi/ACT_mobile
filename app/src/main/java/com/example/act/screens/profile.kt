package com.example.act.screens

import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.act.payment.CheckoutActivity

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F4C75), Color(0xFFE3ECF1))
                )
            )
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {

            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Welcome!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))
            ProfileButton(text = "Support", onClick = { navController.navigate("SupportScreen")},
                color = Color(0xFF8BC34A),
                icon = Icons.AutoMirrored.Filled.Help
            )
            Spacer(modifier = Modifier.height(12.dp))
            ProfileButton(text = "Reviews", onClick = { navController.navigate("ReviewScreen")},
                color = Color(0xFF1B262C),
                icon = Icons.Default.Star
            )
            Spacer(modifier = Modifier.height(12.dp))
            ProfileButton(
                text = "Update",
                onClick = { navController.navigate("UpdateProfileScreen") },
                color = Color(0xFF3282B8),
                icon = Icons.Default.Edit
            )
            Spacer(modifier = Modifier.height(12.dp))
            ProfileButton(
                text = "Act AI Premium",
                onClick = {
                    val intent = Intent(context as? Activity, CheckoutActivity::class.java)
                    context.startActivity(intent)
                },
                color = Color(0xFF0F4C75),
                icon = Icons.Default.ShoppingCart
            )
            Spacer(modifier = Modifier.height(12.dp))
            ProfileButton(
                text = "AI Engine",
                onClick = {
                    navController.navigate("AIScreen")
                },
                color = Color(0xFFE94560),
                icon = Icons.Default.Memory
            )
            Spacer(modifier = Modifier.height(12.dp))
            ProfileButton(
                text = "Assets",
                onClick = {
                    navController.navigate("AssetsScreen")
                },
                color = Color(0xFF6A0572),
                icon = Icons.Default.AttachMoney
            )
            Spacer(modifier = Modifier.height(12.dp))
            ProfileButton(
                text = "Financial Info",
                onClick = {
                    navController.navigate("FinancialScreen")
                },
                color = Color(0xFF3282B8),
                icon = Icons.Default.AccountBalance
            )
            Spacer(modifier = Modifier.height(12.dp))
            ProfileButton(
                text="Profile",
                onClick = { navController.navigate("PortfolioScreen") }
            )

            Spacer(modifier = Modifier.height(12.dp))
            ProfileButton(
                text = "Price Alerts",
                onClick = {
                    navController.navigate("PriceAlertScreen")
                },

                color = Color(0xFF0F4C75),
                icon = Icons.Default.Notifications
            )


        }
    }
}


@Composable
fun ProfileButton(
    text: String,
    onClick: () -> Unit,
    color: Color,
    icon: ImageVector
) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = color),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(56.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "$text Icon",
                tint = Color.White,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }
    }
}