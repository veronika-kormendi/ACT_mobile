package com.example.act.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.act.data.Review
import com.example.act.data.ReviewViewModel
import java.time.LocalDate

@Composable
fun CreateReviewScreen(navController: NavController, viewModel: ReviewViewModel = viewModel()) {
    var name by remember { mutableStateOf(TextFieldValue()) }
    var rating by remember { mutableStateOf(0) }
    var comment by remember { mutableStateOf(TextFieldValue()) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE3ECF1))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(15.dp))
        Text(text = "Add Your Review",
            style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF0F4C75))

        Spacer(modifier = Modifier.height(50.dp))

        Text(text = "Name")

        Spacer(modifier = Modifier.height(5.dp))
        BasicTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp)
        )
        Spacer(modifier = Modifier.height(50.dp))

        StarRating(rating = rating, onRatingChange = { rating = it })
        Spacer(modifier = Modifier.height(50.dp))

        Text(text = "Comment")
        Spacer(modifier = Modifier.height(5.dp))

        BasicTextField(
            value = comment,
            onValueChange = { comment = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(50.dp)
        )

        Spacer(modifier = Modifier.height(90.dp))

        Button(
            onClick = {
                if (name.text.isBlank() || rating == 0 || comment.text.isBlank()) {
                    Toast.makeText(
                        context,
                        "Fill in fields",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.addReview(
                        Review(
                            name = name.text,
                            rating = rating,
                            comment = comment.text,
                            date = LocalDate.now().toString()
                        )
                    )
                    Toast.makeText(
                        context,
                        "Review Added",
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.popBackStack()
                }
            }
        ) {
            Text(text = "Submit Review")
        }
    }
}


@Composable
fun StarRating(rating: Int, onRatingChange: (Int) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Rating", style = MaterialTheme.typography.bodyMedium, )

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 1..5) {
                Icon(
                    imageVector = if (i <= rating) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = "Star $i",
                    tint = if (i <= rating) Color.Yellow else Color.Gray,
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onRatingChange(i) }
                )
            }
        }
    }
}
