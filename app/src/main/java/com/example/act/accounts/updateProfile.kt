package com.example.sportsteam.screens.accounts

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions

@Composable
fun ProfileUpdateScreen(navController: NavController) {
    val db = FirebaseFirestore.getInstance()
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    var name by remember { mutableStateOf("") }
    var documentId by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val userEmail = auth.currentUser?.email

    LaunchedEffect(userEmail) {
        if (userEmail != null) {
            db.collection("users")
                .whereEqualTo("email", userEmail)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    if (querySnapshot.isEmpty) {
                        Toast.makeText(context, "User not found", Toast.LENGTH_SHORT).show()
                    } else {
                        val document = querySnapshot.documents[0]
                        name = document.getString("username") ?: ""
                        documentId = document.id
                    }
                    isLoading = false
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(context, "Failed to fetch user: ${exception.message}", Toast.LENGTH_SHORT).show()
                    isLoading = false
                }
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Loading...")
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = userEmail ?: "",
                onValueChange = { },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                enabled = false
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val userData = mutableMapOf(
                        "username" to name
                    )

                    documentId?.let { id ->
                        db.collection("users").document(id)
                            .set(userData, SetOptions.merge()) // Merging data
                            .addOnSuccessListener {
                                Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener { exception ->
                                Toast.makeText(context, "Failed to update profile: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF60D5F2))
            ) {
                Text("Save Changes")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Go Back",
                fontSize = 14.sp,
                modifier = Modifier.clickable { navController.popBackStack() }
                    .padding(vertical = 8.dp),
                color = Color.Blue
            )
        }
    }
}
