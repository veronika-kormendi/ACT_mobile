package com.example.act.assets

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.act.constants.assetConstants
import com.google.common.reflect.TypeToken
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewStock(navController: NavController){
    val stockList = assetConstants.STOCKS
    Scaffold(topBar = {
        TopAppBar(title = { Text("Stock Assets") })
    }) {
        LazyColumn(
            modifier = Modifier.padding(16.dp)
        ) {
            items(stockList) { stock ->
                StockItem(stock = stock) {
                    navController.navigate("stockDetails/$stock")
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}



@Composable
fun StockItem(stock: String, onClick: () -> Unit) {
    Text(
        text = stock,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable { onClick() },
        style = MaterialTheme.typography.bodyLarge
    )
}


@Composable
fun StockDetails(symbol: String) {
    // State to hold the API data
    val stockData = remember { mutableStateOf<Map<String, StockData>?>(null) }
    val errorMessage = remember { mutableStateOf<String?>(null) }

    // State to hold quantity input
    val quantityInput = remember { mutableStateOf("") }
    val inPortfolio = remember { mutableStateOf(false) }
    val ownedQuantity = remember { mutableStateOf<Double?>(null) }
    val boughtPrice = remember { mutableStateOf<Double?>(null) }
    val context = LocalContext.current

    // Fetch data using LaunchedEffect
    LaunchedEffect(symbol) {
        try {
            stockData.value = callStockApi(symbol)
            inPortfolio.value = checkIfStockInPortfolio(symbol)
            if (inPortfolio.value) {
                getStockDetails(symbol, context) { quantity, price ->
                    ownedQuantity.value = quantity
                    boughtPrice.value = price
                }
            }
        } catch (e: Exception) {
            errorMessage.value = "Failed to fetch data: ${e.message}"
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Details for $symbol",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        when {
            errorMessage.value != null -> {
                Text(
                    text = errorMessage.value ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error
                )
            }
            stockData.value == null -> {
                CircularProgressIndicator()
            }
            else -> {
                val data = stockData.value?.get(symbol)
                if (data != null) {
                    Text("Open: ${data.Open}")
                    Text("High: ${data.High}")
                    Text("Low: ${data.Low}")
                    Text("Close: ${data.Close}")
                    Text("Volume: ${data.Volume}")
                    Spacer(modifier = Modifier.height(8.dp))

                    if (!inPortfolio.value) {
                        TextField(
                            value = quantityInput.value,
                            onValueChange = { quantityInput.value = it },
                            label = { Text("Quantity to buy:") },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                    } else {
                        Text("Quantity owned: ${ownedQuantity.value ?: "Unknown"}")
                        val profit = ownedQuantity.value?.let {
                            (data.Close - (boughtPrice.value ?: 0.0)) * it
                        }
                        Text("Profit: ${profit ?: "Calculating..."}")
                    }
                    Button(
                        onClick = {
                            if (inPortfolio.value) {
                                removeStockFromPortfolio(symbol, context)
                                inPortfolio.value = false
                            } else {
                                val qty = quantityInput.value.toDoubleOrNull()
                                if (qty != null && qty > 0) {
                                    addStockToPortfolio(
                                        symbol = symbol,
                                        quantity = qty,
                                        price = data.Close,
                                        context = context
                                    )
                                    inPortfolio.value = true
                                } else {
                                    Toast.makeText(context, "Invalid quantity", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    ) {
                        Text(if (inPortfolio.value) "Remove from Portfolio" else "Add to Portfolio")
                    }
                } else {
                    Text("No data available for $symbol")
                }
            }
        }
    }
    }

suspend fun callStockApi(symbol: String): Map<String, StockData> {
    return withContext(Dispatchers.IO) {
        val url = URL("https://yfianance-api-904c5fa45cd2.herokuapp.com/get_stock_data")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.setRequestProperty("Content-Type", "application/json")
        connection.doOutput = true

        // Prepare the request body
        val jsonBody = JSONObject()
        jsonBody.put("array", JSONArray(listOf(symbol)))

        // Write the request body
        val outputStream = OutputStreamWriter(connection.outputStream)
        outputStream.write(jsonBody.toString())
        outputStream.flush()

        // Read the response
        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val response = connection.inputStream.bufferedReader().use { it.readText() }

            val gson = Gson()
            val type = object : TypeToken<Map<String, StockData>>() {}.type
            gson.fromJson<Map<String, StockData>>(response, type)
        } else {
            throw Exception("Error: ${connection.responseCode}")
        }.also {
            connection.disconnect()
        }
    }
}

fun addStockToPortfolio(symbol: String, quantity: Double, price: Double, context: Context) {
    val db = FirebaseFirestore.getInstance()
    val userID = FirebaseAuth.getInstance().currentUser?.uid

    val newStockAsset = mapOf(
        "symbol" to symbol,
        "quantity" to quantity,
        "price" to price
    )

    val stockAssetsRef = db.collection("users")
        .document(userID ?: "")
        .collection("portfolio")
        .document("stockAssets")

    stockAssetsRef.collection("assets")
        .get()
        .addOnSuccessListener { querySnapshot ->
            if (querySnapshot.size() < 10) {
                stockAssetsRef.collection("assets")
                    .add(newStockAsset)
                    .addOnSuccessListener {
                        Toast.makeText(context, "$symbol added to portfolio", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error adding stock: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(context, "Cannot add more than 10 stock assets", Toast.LENGTH_SHORT).show()
            }
        }
}
fun removeStockFromPortfolio(symbol: String, context: Context) {
    val db = FirebaseFirestore.getInstance()
    val userID = FirebaseAuth.getInstance().currentUser?.uid

    db.collection("users")
        .document(userID ?: "")
        .collection("portfolio")
        .document("stockAssets")
        .collection("assets")
        .whereEqualTo("symbol", symbol)
        .get()
        .addOnSuccessListener { querySnapshot ->
            for (document in querySnapshot) {
                document.reference.delete()
                    .addOnSuccessListener {
                        Toast.makeText(context, "$symbol removed from portfolio", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error removing stock: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Error finding stock: ${e.message}", Toast.LENGTH_SHORT).show()
        }
}
suspend fun checkIfStockInPortfolio(symbol: String): Boolean {
    val db = FirebaseFirestore.getInstance()
    val userID = FirebaseAuth.getInstance().currentUser?.uid
    return withContext(Dispatchers.IO) {
        try {
            val snapshot = db.collection("users")
                .document(userID ?: "")
                .collection("portfolio")
                .document("stockAssets")
                .collection("assets")
                .whereEqualTo("symbol", symbol)
                .get()
                .await()
            !snapshot.isEmpty
        } catch (e: Exception) {
            false
        }
    }
}
fun getStockDetails(symbol: String, context: Context, onResult: (quantity: Double?, price: Double?) -> Unit) {
    val db = FirebaseFirestore.getInstance()
    val userID = FirebaseAuth.getInstance().currentUser?.uid

    db.collection("users")
        .document(userID ?: "")
        .collection("portfolio")
        .document("stockAssets") // Change to "cryptoAssets" if retrieving crypto
        .collection("assets")
        .whereEqualTo("symbol", symbol)
        .get()
        .addOnSuccessListener { querySnapshot ->
            if (!querySnapshot.isEmpty) {
                val document = querySnapshot.documents.first()
                val quantity = document.getDouble("quantity")
                val price = document.getDouble("price")
                onResult(quantity, price) // Pass the results to the callback
            } else {
                Toast.makeText(context, "Asset not found in portfolio", Toast.LENGTH_SHORT).show()
                onResult(null, null)
            }
        }
        .addOnFailureListener { e ->
            Toast.makeText(context, "Error retrieving asset: ${e.message}", Toast.LENGTH_SHORT).show()
            onResult(null, null)
        }
}