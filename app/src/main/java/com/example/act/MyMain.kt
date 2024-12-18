package com.example.act

import android.media.MediaPlayer
import android.system.Os.remove
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.width
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController


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
    NavItem(label = "Account", icon = Icons.Default.Home, screen = Screen.Profile),
    NavItem(
        label = "Assets",
        icon = Icons.Default.ShoppingCart,
        screen = Screen.Assets
    ),
    NavItem(label = "Support", icon = Icons.Default.Info, screen = Screen.Assets)
)
