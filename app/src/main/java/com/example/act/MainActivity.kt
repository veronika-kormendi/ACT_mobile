package com.example.act

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.act.ui.theme.ACTTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ACTTheme {
                    MyMain()
                }
            }
        }
    }

////creating screens/routes of the app
//sealed class Screen(val route: String){
//    object Welcome : Screen("WelcomeScreen") // login
//    object SignUp : Screen("SignUpScreen") // for account registration
//    object LoggedIn : Screen("UserAccountScreen")
//    object AIPremiumUpgrade : Screen("AIPremiumUpgradeScreen")
//    object Help : Screen("HelpScreen")
//    object Feedback : Screen("RatingsAndReviewScreen")
//    object PriceAlert : Screen("PriceAlertScreen")
//    object Assets: Screen("AssetsScreen")
//
//
//}
//// navigation items for the bottom bar
//data class NavItem(
//    var label: String,
//    val icon: ImageVector,
//    val screen: Screen
//)

//@Composable
//fun MyMain(){
//    //init navigation controller
//    val navContoroller = rememberNavController()
//    // list of navigation items
//    val navItemList = listOf(
//        NavItem(label = "Account", icon = Icons.Default.Home, screen = Screen.LoggedIn),
//        NavItem(label = "Assets", icon = Icons.Default.ShoppingCart, screen = Screen.Assets),
//        NavItem(label = "Help", icon = Icons.Default.Info, screen = Screen.Assets)
//    )
//}