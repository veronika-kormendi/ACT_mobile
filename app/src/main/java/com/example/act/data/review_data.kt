package com.example.act.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ReviewViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ReviewUiState())
    val uiState: StateFlow<ReviewUiState> = _uiState

    init {
        fetchReviews()
    }

    private fun fetchReviews() {
        _uiState.value = ReviewUiState(isLoading = true)
        FirebaseFirestore.getInstance()
            .collection("reviews")
            .get()
            .addOnSuccessListener { result ->
                val reviews = result.map { document ->
                    Review(
                        id = document.id,
                        name = document.getString("name") ?: "Anonymous",
                        rating = document.getLong("rating")?.toInt() ?: 3,
                        comment = document.getString("comment") ?: "No comment provided.",
                        date = document.getString("date") ?: "Unknown date"
                    )
                }
                _uiState.value = ReviewUiState(reviews = reviews)
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching reviews: ${exception.message}")
                _uiState.value = ReviewUiState(errorMessage = exception.message)
            }
    }
}

data class ReviewUiState(
    val isLoading: Boolean = false,
    val reviews: List<Review> = emptyList(),
    val errorMessage: String? = null
)

data class Review(
    val id: String,
    val name: String,
    val rating: Int,
    val comment: String,
    val date: String
)
