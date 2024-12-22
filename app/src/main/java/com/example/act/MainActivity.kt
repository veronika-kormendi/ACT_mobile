package com.example.act

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.act.accounts.ResetPasswordScreen
import com.example.act.accounts.SigninScreen
import com.example.act.accounts.SignupScreen
import com.example.act.accounts.signupUser
import com.example.act.ui.theme.ACTTheme
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var oneTapClient: SignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase and OneTap Sign-In client
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()
        oneTapClient = Identity.getSignInClient(this)

        // Register the Google Sign-In Launcher
        val googleSignInLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            try {
                // Handle the sign-in result
                val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                val idToken = credential?.googleIdToken
                if (idToken != null) {
                    firebaseAuthWithGoogle(idToken)
                } else {
                    Log.w("GoogleSignIn", "No ID token found!")
                }
            } catch (e: Exception) {
                Log.e("GoogleSignIn", "Sign-in failed: ${e.message}")
            }
        }

        setContent {
            ACTTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "SigninScreen") {
                    composable(Screen.Login.route) {
                        SigninScreen(
                            navController = navController,
                            onGoogleSignIn = {
                                val signInRequest = BeginSignInRequest.builder()
                                    .setGoogleIdTokenRequestOptions(
                                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                            .setSupported(true)
                                            .build()
                                    )
                                    .build()

                                // Start Google Sign-In flow
                                oneTapClient.beginSignIn(signInRequest)
                                    .addOnSuccessListener { beginSignInResult ->
                                        val signInIntent = beginSignInResult.signInIntent
                                        googleSignInLauncher.launch(signInIntent)
                                    }
                                    .addOnFailureListener { exception ->
                                        Log.e("GoogleSignIn", "Google sign-in failed: ${exception.message}")
                                    }
                            }
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
                    composable(Screen.Reset.route) { ResetPasswordScreen() }
                    composable("ProfileScreen") {
                        MainFunction()
                    }
                }
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success
                    val user = auth.currentUser
                    Log.d("GoogleSignIn", "Sign in successful: ${user?.displayName}")
                } else {
                    Log.w("GoogleSignIn", "signInWithCredential:failure", task.exception)
                }
            }
    }
}
