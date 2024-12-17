package com.example.act

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.act.screens.ProfileScreen
import com.example.act.screens.SignUpScreen
import com.example.act.screens.SupportScreen
import com.example.act.screens.WelcomeScreen


//creating screens/routes of the app
sealed class Screen(val route: String){
    object Welcome : Screen("WelcomeScreen") // login
    object SignUp : Screen("SignUpScreen") // for account registration
    object LoggedIn : Screen("UserAccountScreen")
    object AIPremiumUpgrade : Screen("AIPremiumUpgradeScreen")
    object Support : Screen("SupportScreen")
    object Feedback : Screen("RatingsAndReviewScreen")
    object PriceAlert : Screen("PriceAlertScreen")
    object Assets: Screen("AssetsScreen")
    object Profile: Screen("ProfileScreen")


}
// navigation items for the bottom bar
data class NavItem(
    var label: String,
    val icon: ImageVector,
    val screen: Screen
)

@Composable
fun MyMain(){
    //init navigation controller
    val navController = rememberNavController()
    // list of navigation items
    val navItemList = listOf(
        NavItem(label = "Account", icon = Icons.Default.Home, screen = Screen.LoggedIn),
        NavItem(label = "Assets", icon = Icons.Default.ShoppingCart, screen = Screen.Assets),
        NavItem(label = "Support", icon = Icons.Default.Info, screen = Screen.Assets)
    )
    NavHost(navController= navController, startDestination = Screen.Welcome.route){
        composable(Screen.Welcome.route) { WelcomeScreen(navController) }
        composable(Screen.SignUp.route) { SignUpScreen(navController) }
        //composable(Screen.LoggedIn.route) { LoggedInScreen(navController) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable(Screen.Support.route) { SupportScreen(navController) }
    }

}