package com.example.act

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.act.accounts.ProfileUpdateScreen
import com.example.act.accounts.SigninScreen
import com.example.act.accounts.SignupScreen
import com.example.act.data.User
import com.example.act.screens.ProfileScreen
import com.example.act.screens.ReviewScreen
import com.example.act.screens.SupportScreen
import com.example.act.ui.theme.ACTTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ACTTheme {
                val navController = rememberNavController()

                NavHost(navController = navController, startDestination = Screen.Login.route) {

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
                            context = this@MainActivity,
                            navController = navController,
                            onSignup = { email, password, name, date,
                                         onSignupSuccess ->
                                signupUser(email, password, name, date) { user ->
                                    onSignupSuccess(user)
                                    navController.navigate(Screen.Login.route)
                                }
                            }
                        )
                    }
                }
            }
        }
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
    }

    fun signupUser(
        email: String, password: String, name: String, date: FieldValue,
        onSignupSuccess: (User) -> Unit
    ) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        user?.let {
                            val newUser = User(it.uid, email, name, date)
                            firestore.collection("users")
                                .document(it.uid)
                                .set(newUser)
                                .addOnSuccessListener {
                                    onSignupSuccess(newUser)
                                }
                                .addOnFailureListener { e ->
                                    Log.w("MainActivity", "Error creating account", e)
                                    Toast.makeText(
                                        this,
                                        "Error saving user data",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                        }
                    } else {
                        Toast.makeText(
                            this,
                            "${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
    }
