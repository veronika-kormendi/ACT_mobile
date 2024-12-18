package com.example.act.accounts

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.act.R
import com.example.act.Screen
import com.example.act.data.User
import com.google.firebase.firestore.FieldValue

@Composable
fun SignupScreen(
    context: Context,
    navController: NavController,
    onSignup: (String, String, String, FieldValue, (User) -> Unit) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }

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
        TextField(value = name, onValueChange = { name = it }, label = { Text("Name") })
        Spacer(modifier = Modifier.height(16.dp))
        TextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        TextField(
            value = password, onValueChange = { password = it },
            visualTransformation = PasswordVisualTransformation(),
            label = { Text("Password") })

        Spacer(modifier = Modifier.height(16.dp))


        Button(onClick = {
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
            else {
                onSignup(email, password, name,
                    FieldValue.serverTimestamp()) { user ->
                    Toast.makeText(context, "Account created successfully!", Toast.LENGTH_SHORT).show()
                    navController.navigate(Screen.Login.route)
                }
            }
        }) {
            Text("Sign Up")
        }
    }
}

