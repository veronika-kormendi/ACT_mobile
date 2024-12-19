package com.example.act.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
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
    var rating by remember { mutableStateOf(TextFieldValue()) }
    var comment by remember { mutableStateOf(TextFieldValue()) }
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Add Your Review", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Name")
        BasicTextField(
            value = name,
            onValueChange = { name = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Rating (1-5)")
        BasicTextField(
            value = rating,
            onValueChange = { rating = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "Comment")
        BasicTextField(
            value = comment,
            onValueChange = { comment = it },
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                    val ratingValue = rating.text.toIntOrNull() ?: 0
                        viewModel.addReview(
                            Review(
                                name = name.text,
                                rating = ratingValue,
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
        ) {
            Text(text = "Submit Review")
        }
    }
}
