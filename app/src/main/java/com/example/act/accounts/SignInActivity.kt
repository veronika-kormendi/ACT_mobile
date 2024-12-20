package com.example.act.accounts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.act.accounts.SigninScreen
import com.example.act.ui.theme.ACTTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.auth.ktx.auth


class SignInActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = Firebase.auth

        setContent {
            ACTTheme {
                val navController = rememberNavController()
                SigninScreen(navController = navController, auth = auth)
            }
        }
    }
}