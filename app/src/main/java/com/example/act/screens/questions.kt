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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
@Composable
fun QuestionScreen() {
    val faqData = listOf(
        Category(
            title = "Accounts",
            questions = listOf(
                Question("How do I change my account details?", "To change your account details, go to settings, select 'Update Personal Details'."),
                Question("How do I upgrade my account?", "Go to Settings/Account/Upgrade Account/Select ACT Premium."),
                Question("How to set a price alert?", "To set a price alert, go to Settings/Alerts/Price Alerts/Set Alert. There you can tailor your price alerts."),
                Question("How can I delete my account if I no longer need it?", "You cannot delete your own account; you need to send a request to Customer Support and the System Administrator can remove it for you.")
            )
        ),
        Category(
            title = "Trading",
            questions = listOf(
                Question("What shares can I trade on ACT platforms?", "Up to 10 technology shares can be selected, and you can find all the options under Asset Management/Shares."),
                Question("What cryptocurrencies can I trade on ACT platforms?", "Three crypto assets can be selected, and you can find the options under Asset Management/Cryptocurrencies.")
            )
        ),
        Category(
            title = "ACT",
            questions = listOf(
                Question("What is ACT?", "ACT: Agentic Corporate Trader is an online trading platform that allows users to invest in technology shares and cryptocurrencies."),
                Question("Who is ACT Web for?", "ACT Web is developed for Fund Managers."),
                Question("Who is ACT Mobile for?", "ACT Mobile is for Fund Managers and Fund Administrators.")
            )
        )
    )

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
                text = "Frequently Asked Questions",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F4C75),
                modifier = Modifier.padding(top = 32.dp, bottom = 16.dp)
            )

            faqData.forEach { category ->
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1B262C),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                category.questions.forEach { question ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Q: ${question.question}", //question displayed
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF0F4C75),
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "A: ${question.answer}", // corresponding answer displayed
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF3282B8),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

data class Category(val title: String, val questions: List<Question>)
data class Question(val question: String, val answer: String)
