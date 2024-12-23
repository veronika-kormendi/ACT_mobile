package com.example.act.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun ChatPremAI() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFBBE1FA))
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "ACT-AI premium",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F4C75),
                modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))
            AISwitch()
        }
    }
}

@Composable
fun AISwitch(){
    val aiState = remember{ mutableStateOf(false)}
    val context = LocalContext.current

    LaunchedEffect(Unit){
        fetchAIStatus { result ->
            aiState.value = result
        }
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ){
        Text(
            text = "Enable AI",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF0F4C75)
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = aiState.value,
            onCheckedChange = {checked ->
                aiState.value = checked
                toggleAIFunctions(context, checked)
            }
        )
    }

}

fun toggleAIFunctions(context: Context, enabled: Boolean){
    val db = FirebaseFirestore.getInstance()
    val userID = FirebaseAuth.getInstance().currentUser?.uid

    if (userID != null) {
        db.collection("users")
            .document(userID)
            .update("aiStatus", enabled)
            .addOnSuccessListener {
                Toast.makeText(context, "AI status updated", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(context, "Failed to update AI status", Toast.LENGTH_SHORT).show()
            }
    }
}

fun fetchAIStatus(onResult:(Boolean) -> Unit){
    val db = FirebaseFirestore.getInstance()
    val userID = FirebaseAuth.getInstance().currentUser?.uid

    db.collection("users")
        .document(userID ?: "")
        .get()
        .addOnSuccessListener { querySnapshot ->
            if(querySnapshot.exists()){
                val aiStatus = querySnapshot.getBoolean("aiStatus") ?: false
                onResult(aiStatus)
            } else {
                onResult(false)
            }
        }
        .addOnFailureListener {
            onResult(false)
        }
}
