package com.example.act.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.act.data.Review
import com.example.act.data.ReviewViewModel

@Composable
fun ReviewScreen(navController: NavController, viewModel: ReviewViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    when {
        uiState.isLoading -> {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(color = Color.Gray)
            }
        }
        uiState.errorMessage != null -> {
            Text(
                text = "Error: ${uiState.errorMessage}",
                modifier = Modifier.fillMaxSize(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Red
            )
        }
        else -> {
            Spacer(modifier = Modifier.height(8.dp))
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxSize()
                    .background(Color(0xFFBBE1FA))
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { navController.navigate("CreateReview") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp)
                        .height(48.dp),
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        text = "Add Review",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                uiState.reviews.forEach { review ->
                    ReviewRow(review = review)
                }
            }
        }
    }
}

@Composable
fun ReviewRow(review: Review) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color.White)
            .padding(16.dp)
    ) {
        Text(
            text = "Name: ${review.name}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF0F4C75)
        )
        Text(
            text = "Rating: ${review.rating} / 5",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        Text(
            text = "Comment: ${review.comment}",
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF0F4C75)
        )
        Text(
            text = "Date: ${review.date}",
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}
