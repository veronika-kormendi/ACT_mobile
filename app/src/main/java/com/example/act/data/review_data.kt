package com.example.act.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

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

    fun addReview(review: Review) {
        FirebaseFirestore.getInstance()
            .collection("reviews")
            .add(review)
            .addOnSuccessListener {
                fetchReviews()
            }
            .addOnFailureListener {
                _uiState.value = _uiState.value.copy(errorMessage = "Failed to add review.")
            }
    }

    fun sortByBest() {
        val sortedReviews = _uiState.value.reviews.sortedByDescending { it.rating}
        _uiState.value = _uiState.value.copy(reviews = sortedReviews)
    }

    fun sortByWorst() {
        val sortedReviews = _uiState.value.reviews.sortedBy { it.rating}
        _uiState.value = _uiState.value.copy(reviews = sortedReviews)
    }

    fun sortByNewest() {
        val sortedReviews = _uiState.value.reviews.sortedByDescending { it.date}
        _uiState.value = _uiState.value.copy(reviews = sortedReviews)
    }

    fun sortByOldest() {
        val sortedReviews = _uiState.value.reviews.sortedBy{ it.date}
        _uiState.value = _uiState.value.copy(reviews = sortedReviews)
    }


}

data class ReviewUiState(
    val isLoading: Boolean = false,
    val reviews: List<Review> = emptyList(),
    val errorMessage: String? = null
)

data class Review(
    val name: String,
    val rating: Int,
    val comment: String,
    val date: String
)
