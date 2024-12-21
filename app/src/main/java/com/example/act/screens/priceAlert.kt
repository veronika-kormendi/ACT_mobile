package com.example.act.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PriceAlertScreen() {
    val context = LocalContext.current

    // State variables for input fields
    var stockSymbol by remember { mutableStateOf("") }
    var priceThreshold by remember { mutableStateOf("") }
    var isIncreaseAlert by remember { mutableStateOf(true) } // Increase or decrease

    // Screen layout
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)) // Light background color
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Set Price Alert",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Input for Stock Symbol
            OutlinedTextField(
                value = stockSymbol,
                onValueChange = { stockSymbol = it },
                label = { Text("Stock Symbol") },
                placeholder = { Text("e.g., AAPL") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Input for Price Threshold
            OutlinedTextField(
                value = priceThreshold,
                onValueChange = { priceThreshold = it },
                label = { Text("Price Threshold (%)") },
                placeholder = { Text("e.g., 5") },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )

            // Toggle: Alert Type (Increase or Decrease)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Alert Type: ",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.CenterVertically)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    RadioButton(
                        selected = isIncreaseAlert,
                        onClick = { isIncreaseAlert = true }
                    )
                    Text(text = "Increase", modifier = Modifier.padding(end = 16.dp))

                    RadioButton(
                        selected = !isIncreaseAlert,
                        onClick = { isIncreaseAlert = false }
                    )
                    Text(text = "Decrease")
                }
            }

            // Button: Save Alert
            Button(
                onClick = {
                    if (stockSymbol.isBlank() || priceThreshold.isBlank()) {
                        Toast.makeText(context, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
                    } else {
                        val alertType = if (isIncreaseAlert) "increase" else "decrease"
                        Toast.makeText(
                            context,
                            "Alert set for $stockSymbol: $alertType by $priceThreshold%.",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Text(
                    text = "Set Alert",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
        }
    }
}
