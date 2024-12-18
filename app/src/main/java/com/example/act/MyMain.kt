package com.example.act

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.graphics.vector.ImageVector


//creating screens/routes of the app
sealed class Screen(val route: String){
    object Welcome : Screen("WelcomeScreen") // login
    object SignUp : Screen("SignUpScreen") // for account registration
    object LoggedIn : Screen("UserAccountScreen")
    object AIPremiumUpgrade : Screen("AIPremiumUpgradeScreen")
    object Support : Screen("SupportScreen")
    object Reviews : Screen("ReviewScreen")
    object PriceAlert : Screen("PriceAlertScreen")
    object Assets: Screen("AssetsScreen")
    object Profile: Screen("ProfileScreen")
    object Update: Screen("UpdateProfileScreen")
    object Login : Screen("SigninScreen")
}

// navigation items for the bottom bar
data class NavItem(
    var label: String,
    val icon: ImageVector,
    val screen: Screen
)

// list of navigation items
val navItemList = listOf(
    NavItem(label = "Account", icon = Icons.Default.Home, screen = Screen.Login),
    NavItem(
        label = "Assets",
        icon = Icons.Default.ShoppingCart,
        screen = Screen.Assets
    ),
    NavItem(label = "Support", icon = Icons.Default.Info, screen = Screen.Assets)
)