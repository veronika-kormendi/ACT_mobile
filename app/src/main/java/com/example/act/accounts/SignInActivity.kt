package com.example.act.accounts

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.act.ui.theme.ACTTheme
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class SignInActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        oneTapClient = Identity.getSignInClient(this)

        val googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                oneTapClient.getSignInCredentialFromIntent(result.data)?.let { credential ->
                    val idToken = credential.googleIdToken
                    if (idToken != null) {
                        firebaseAuthWithGoogle(idToken, auth)
                    } else {
                        Log.w("GoogleSignIn", "No ID token found!")
                    }
                }
            }
        }

        setContent {
            ACTTheme {
                val navController = rememberNavController()
                SigninScreen(
                    navController = navController,
                    auth = auth,
                    oneTapClient = oneTapClient,
                    googleSignInLauncher = { intent -> googleSignInLauncher.launch(intent) }
                )
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, auth: FirebaseAuth) {
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


//@Composable
//private fun rememberOneTapSignInClient(): SignInClient {
//    val context = androidx.compose.ui.platform.LocalContext.current
//
//    val signInRequest = BeginSignInRequest.builder()
//        .setGoogleIdTokenRequestOptions(
//            BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                .setSupported(true)
//                .setServerClientId("679657748545-4iiq2qv03u6j283h3bfbogh2b0nul1d5.apps.googleusercontent.com") // Web Client ID from Firebase Console
//                .setFilterByAuthorizedAccounts(false)
//                .build()
//        )
//        .build()
//
//    return Identity.getSignInClient(context).apply {
//        beginSignIn(signInRequest)
//            .addOnSuccessListener { Log.d("GoogleSignIn", "Sign-in flow started") }
//            .addOnFailureListener { e -> Log.e("GoogleSignIn", "Sign-in flow failed: ${e.message}") }
//    }
//}
//
//private fun firebaseAuthWithGoogle(idToken: String, auth: FirebaseAuth) {
//    val credential = GoogleAuthProvider.getCredential(idToken, null)
//    auth.signInWithCredential(credential)
//        .addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                // Sign in success
//                val user = auth.currentUser
//                Log.d("GoogleSignIn", "Sign in successful: ${user?.displayName}")
//            } else {
//                Log.w("GoogleSignIn", "signInWithCredential:failure", task.exception)
//            }
//        }
//}
