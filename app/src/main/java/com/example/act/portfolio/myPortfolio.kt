package com.example.act.portfolio

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.act.assets.CryptoItem
import com.example.act.assets.StockItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun PortfolioScreen(navController: NavController) {
    Column(modifier = Modifier.fillMaxSize()
        .scrollable(
        state = rememberScrollState(),
        orientation = Orientation.Vertical)
    )
    {
        Text("Your Stock Assets:", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        ViewStockPortfolio(navController)

        Spacer(modifier = Modifier.height(16.dp))
        Text("Your Crypto Assets:", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(8.dp))
        ViewCryptoPortfolio(navController)
    }
}

@Composable
fun ViewStockPortfolio(navController: NavController) {
    val stockAssets = remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        fetchStockAssets { assets ->
            stockAssets.value = assets
        }
    }

    LazyColumn{
        items(stockAssets.value){ symbol ->
            StockItem(stock = symbol) {
                navController.navigate("stockDetails/$symbol")
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ViewCryptoPortfolio(navController: NavController) {
    val cryptoAssets = remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        fetchCryptoAssets { assets ->
            cryptoAssets.value = assets
        }
    }
    LazyColumn {
        items(cryptoAssets.value) { symbol ->
            CryptoItem(crypto = symbol) {
                navController.navigate("cryptoDetails/$symbol")
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

fun fetchStockAssets(onResult: (List<String>) -> Unit){
    val db = FirebaseFirestore.getInstance()
    val userID = FirebaseAuth.getInstance().currentUser?.uid

    db.collection("users")
        .document(userID ?: "")
        .collection("portfolio")
        .document("stockAssets")
        .collection("assets")
        .get()
        .addOnSuccessListener { querySnapshot ->
            val symbols = querySnapshot.documents.mapNotNull { it.getString("symbol") }
            onResult(symbols)
        }
        .addOnFailureListener {
            onResult(emptyList())
        }
}
fun fetchCryptoAssets(onResult: (List<String>) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val userID = FirebaseAuth.getInstance().currentUser?.uid

    db.collection("users")
        .document(userID ?: "")
        .collection("portfolio")
        .document("cryptoAssets")
        .collection("assets")
        .get()
        .addOnSuccessListener { querySnapshot ->
            val symbols = querySnapshot.documents.mapNotNull { it.getString("symbol") }
            onResult(symbols)
        }
        .addOnFailureListener {
            onResult(emptyList())
        }
}

