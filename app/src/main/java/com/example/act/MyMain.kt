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
import com.example.act.accounts.ProfileUpdateScreen
import com.example.act.accounts.SigninScreen
import com.example.act.accounts.SignupScreen
import com.example.act.accounts.signupUser
import com.example.act.screens.ProfileScreen
import com.example.act.screens.ReviewScreen
import com.example.act.screens.SupportScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


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

@Composable
fun MainFunction(auth: FirebaseAuth, firestore: FirebaseFirestore) {
    val navController = rememberNavController()

    val context = LocalContext.current

    NavHost(navController = navController,
        startDestination = Screen.Login.route,
        modifier = Modifier.padding()) {

        composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable(Screen.Update.route) { ProfileUpdateScreen(navController) }
        composable(Screen.Support.route) { SupportScreen(navController) }
        composable(Screen.Reviews.route) { ReviewScreen() }
        composable(Screen.Login.route) {
            SigninScreen(
                navController = navController,
                auth = auth
            )
        }
        composable(Screen.SignUp.route) {
            SignupScreen(
                context =  context,
                navController = navController,
                onSignup = { email, password, name, date,
                             onSignupSuccess ->
                    signupUser(auth, firestore, context,
                        email, password, name, date) { user ->
                        onSignupSuccess(user)
                        navController.navigate(Screen.Login.route)
                    }
                }
            )
        }
    }
}
