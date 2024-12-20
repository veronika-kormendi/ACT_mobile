package com.example.act

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.act.accounts.SigninScreen
import com.example.act.accounts.SignupScreen
import com.example.act.accounts.signupUser
import com.example.act.ui.theme.ACTTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        setContent {
            ACTTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "SigninScreen") {
                    composable(Screen.Login.route) {
                        SigninScreen(
                            navController = navController,
                            auth = auth,
                        )
                    }
                    composable(Screen.SignUp.route) {
                        SignupScreen(
                            context = this@MainActivity,
                            navController = navController,
                            onSignup = { email, password, name, date, onSignupSuccess ->
                                signupUser(
                                    auth, firestore, this@MainActivity,
                                    email, password, name, date
                                ) { user ->
                                    onSignupSuccess(user)
                                    navController.navigate(Screen.Login.route)
                                }
                            }
                        )
                    }
                    composable("ProfileScreen") {
                        MainFunction()
                    }
                }
            }
        }
    }
}