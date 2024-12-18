package com.example.act.data

import com.google.firebase.firestore.FieldValue

data class User(
    val id: String,
    val email: String,
    val name: String,
    val date: FieldValue
)