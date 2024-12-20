package com.example.act.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
    var expanded by remember { mutableStateOf(false) }
    var reviews by remember { mutableStateOf(uiState.reviews) }

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
            ) {

                Box(modifier = Modifier
                    .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp)
                            .padding(top = 20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(
                            onClick = { expanded = !expanded },
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .padding(top = 20.dp)
                                .height(48.dp)
                        ) {
                            Text("Sort")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { navController.navigate("CreateReview") },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3F51B5)),
                            modifier = Modifier
                                .padding(horizontal = 32.dp)
                                .padding(top = 20.dp)
                                .height(48.dp),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Text(
                                text = "Add",
                                style = MaterialTheme.typography.bodyLarge,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }
                    }
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(8.dp)
                    ) {
                        DropdownMenuItem(
                            onClick = {
                                viewModel.sortByWorst()
                                reviews = uiState.reviews.sortedBy { it.rating }
                                expanded = false
                            },
                            text = { Text("Lowest Rating")}
                        )
                        DropdownMenuItem(
                            onClick = {
                                viewModel.sortByBest()
                                reviews = uiState.reviews.sortedByDescending { it.rating }
                                expanded = false
                            },
                            text = { Text("Highest Rating") }
                        )
                        DropdownMenuItem(
                            onClick = {
                                viewModel.sortByNewest()
                                expanded = false
                            },
                            text = { Text("Newest")}
                        )
                        DropdownMenuItem(
                            onClick = {
                                viewModel.sortByOldest()
                                expanded = false
                            },
                            text = { Text("Oldest")}
                        )
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))

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
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 1..5) {
                Icon(
                    imageVector = if (i <= review.rating) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "Star $i",
                    tint = if (i <= review.rating) Color(0xFFDECC58) else Color(0xFF797E81),
                    modifier = Modifier
                        .size(20.dp)
                )
            }
        }
        Text(
            text = review.name,
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFF0F4C75)
        )
        Text(
            text = review.comment,
            style = MaterialTheme.typography.bodyMedium,
            color = Color(0xFF0F4C75)
        )
        Text(
            text = review.date,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
    }
}
