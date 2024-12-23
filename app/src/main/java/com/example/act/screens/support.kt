package com.example.act.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.act.R
import com.example.act.Screen

var phoneNumber = "+35316971368"

@Composable
fun SupportScreen(navController: NavController) {
    val context = LocalContext.current
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
                text = "Support Screen",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F4C75),
                modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
            )
            Image(
                painter = painterResource(id = R.drawable.support),
                contentDescription = "Logo",
                modifier = Modifier.size(300.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = { openDialer(context, phoneNumber) },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF60D5F2)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(48.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Call Support",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { navController.navigate("SupportFormScreen") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF60D5F2)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(48.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "Write to Support",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }


            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = { navController.navigate("FAQScreen") },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF60D5F2)),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .height(48.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = "FAQs",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }
    }
}

fun openDialer(context: android.content.Context, phoneNumber: String) {
    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
    context.startActivity(intent)
}