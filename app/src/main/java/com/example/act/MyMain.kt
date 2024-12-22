package com.example.act

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.act.accounts.ProfileUpdateScreen
import com.example.act.assets.CryptoDetails
import com.example.act.assets.StockDetails
import com.example.act.assets.ViewCrypto
import com.example.act.assets.ViewStock
import com.example.act.screens.ChatPremAI
import com.example.act.screens.CreateReviewScreen
import com.example.act.screens.FinancialScreen
import com.example.act.screens.PriceAlertScreen
import com.example.act.screens.ProfileScreen
import com.example.act.screens.QuestionScreen
import com.example.act.screens.ReviewScreen
import com.example.act.screens.SupportFormScreen
import com.example.act.screens.SupportScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


//creating screens/routes of the app
sealed class Screen(val route: String) {
    object SignUp : Screen("SignUpScreen") // for account registration
    object AddReview : Screen("CreateReview")
    object AIPremiumUpgrade : Screen("AIPremiumUpgradeScreen")
    object Support : Screen("SupportScreen")
    object Reviews : Screen("ReviewScreen")
    object Reset : Screen("ResetPassScreen")
    object SupportForm : Screen("SupportFormScreen")
    object PriceAlert : Screen("PriceAlertScreen")
    object Assets : Screen("AssetsScreen")
    object Questions : Screen("FAQScreen")
    object Financial : Screen("FinancialScreen")
    object AIEngine : Screen("AIScreen")
    object PremiumChat : Screen("PremiumAI")
    object Profile : Screen("ProfileScreen")
    object Update : Screen("UpdateProfileScreen")
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
    NavItem(label = "Support", icon = Icons.Default.Info, screen = Screen.Support)
)

@Composable
fun MainFunction() {
    val navController = rememberNavController()
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
                NavigationBar {
                    navItemList.forEachIndexed { index, navItem ->
                        NavigationBarItem(
                            selected = selectedIndex == index,
                            onClick = {
                                selectedIndex = index
                                if (navController.currentDestination?.route != navItem.screen.route) {
                                    navController.navigate(navItem.screen.route) {
                                        launchSingleTop =
                                            true // prevent multiple copies of the same destination
                                        restoreState =
                                            true // restore state to previously selected item
                                    }
                                }
                            },
                            label = { Text(text = navItem.label) },
                            icon = { navItem.icon }
                        )
                    }
                }

        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Profile.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("cryptoDetails/{symbol}") { backStackEntry ->
                val symbol = backStackEntry.arguments?.getString("symbol") ?: ""
                CryptoDetails(symbol)
            }
            composable("stockDetails/{symbol}") { backStackEntry ->
                val symbol = backStackEntry.arguments?.getString("symbol") ?: ""
                StockDetails(symbol)
            }
            composable(Screen.Assets.route) { ViewStock(navController) }
            composable(Screen.Questions.route) { QuestionScreen() }
            composable(Screen.PremiumChat.route) { ChatPremAI() }
            composable(Screen.Assets.route) { AssetsScreen() }
            composable(Screen.Financial.route) { FinancialScreen()}
            composable(Screen.AIEngine.route) { AIEngineScreen() }
            composable(Screen.Profile.route) { ProfileScreen(navController) }
            composable(Screen.SupportForm.route) { SupportFormScreen() }
            composable(Screen.AddReview.route) { CreateReviewScreen(navController) }
            composable(Screen.Update.route) { ProfileUpdateScreen(navController) }
            composable(Screen.Support.route) { SupportScreen(navController) }
            composable(Screen.Reviews.route) { ReviewScreen(navController) }
            composable(Screen.PriceAlert.route) { PriceAlertScreen() }

        }
    }
}

