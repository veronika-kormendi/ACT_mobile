package com.example.act.accounts

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.credentials.Credential
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.example.act.R
import com.example.act.Screen
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import kotlinx.coroutines.CoroutineScope


@Composable
fun SigninScreen(navController: NavController, auth: FirebaseAuth) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(300.dp)
        )

        Spacer(modifier = Modifier.height(64.dp))

        // Email and password fields
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = PasswordVisualTransformation()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isEmpty() || password.isEmpty())  {
                    Toast.makeText(context, "Please fill out all fields", Toast.LENGTH_SHORT).show()
                } else {
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                navController.navigate(Screen.Profile.route)
                            } else {
                                Toast.makeText(
                                    context,
                                    "Login failed: ${task.exception?.message}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF60D5F2)
            )
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Forgot Password?",
            fontSize = 14.sp,
            modifier = Modifier.clickable { navController.navigate(Screen.Reset.route) }
                .padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "New? Make an account",
            fontSize = 14.sp,
            modifier = Modifier.clickable { navController.navigate(Screen.SignUp.route) }
                .padding(vertical = 8.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}
//@Composable
//fun AuthenticationButton(
//    buttonText: Int,
//    onGetCredentialResponse: (Credential) -> Unit)
//{
//    val context = LocalContext.current
//    val CoroutineScope = rememberCoroutineScope()
//    val credentialManager = androidx.credentials.CredentialManager.create(context)
//
//    Button(
//        onClick = {
//           val googleOption = GetGoogleIdOption.Builder()
//               .setFilterByAuthorizedAccounts(false)
//               .setServerClientId(context.getString(R.string.default_web_client_id))
//               .build()
//        }
//    ){
//
//    }
//}
