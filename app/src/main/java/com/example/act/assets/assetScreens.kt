package com.example.act.assets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.act.R

@Composable
fun AssetScreen(navController: NavController){
    Box(modifier = Modifier.padding(10.dp), contentAlignment = Alignment.Center){
        Column(modifier = Modifier.fillMaxSize()){
            Text(text = "Assets", style = MaterialTheme.typography.headlineLarge)
            Row(modifier = Modifier.padding(10.dp)){
                ToStock(navController = navController)
                ToCrypto(navController = navController)
            }
        }
        }

}

@Composable
fun ToStock(navController: NavController){
    Column(){
        Text(text = "Stocks")
        Image(painter= painterResource(R.drawable.stonks), contentDescription = null, modifier = Modifier.size(200.dp).clickable{navController.navigate("ViewStock")})
    }
}

@Composable
fun ToCrypto(navController: NavController){
    Column(){
        Text(text = "Cryptocurrencies")
        Image(painter= painterResource(R.drawable.cryptoicon), contentDescription = null, modifier = Modifier.size(200.dp).clickable{navController.navigate("ViewCrypto")})
    }
}