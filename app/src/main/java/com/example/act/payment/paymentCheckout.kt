package com.example.act.payment

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.act.R
import com.example.act.screens.ChatPremAI
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.wallet.contract.TaskResultContracts.GetPaymentDataResult
import com.google.pay.button.PayButton


@Composable
fun PremiumAIScreen(
    title: String,
    description: String,
    price: String,
    image: Int,
    onGooglePayButtonClick: () -> Unit,
    payUiState: PaymentUiState = PaymentUiState.NotStarted
) {
    val padding = 20.dp

    if (payUiState is PaymentUiState.PaymentCompleted) {
        ChatPremAI()
    } else {
        Column(
            modifier = Modifier
                .background(Color(0xffeeeeee))
                .padding(padding)
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(space = padding / 2),
        ) {
            Image(
                contentDescription = "checkmark",
                painter = painterResource(image),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(350.dp)
            )
            Text(
                text = title,
                color = Color(0xff000000),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = price, color = Color(0xff000000))
            Spacer(Modifier)
            Text(
                text = "Description",
                color = Color(0xff000000),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                color =Color(0xff000000)
            )
            Spacer(modifier = Modifier.height(8.dp))
            if (payUiState !is PaymentUiState.NotStarted) {
                PayButton(
                    modifier = Modifier
                        .testTag("payButton")
                        .fillMaxWidth(),
                    onClick = onGooglePayButtonClick,
                    allowedPaymentMethods = PaymentsUtil.allowedPaymentMethods.toString()
                )
            }
        }
    }
}



class CheckoutActivity : ComponentActivity() {

    private val paymentDataLauncher =
        registerForActivityResult(GetPaymentDataResult()) { taskResult ->
            when (taskResult.status.statusCode) {
                CommonStatusCodes.SUCCESS -> {
                    taskResult.result!!.let {
                        Log.i("Google Pay result:", it.toJson())
                        model.setPaymentData(it)
                    }
                }
            }
        }
    private val model: CheckoutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val payState: PaymentUiState by model.paymentUiState.collectAsStateWithLifecycle()
            PremiumAIScreen(
                title = "ACT-AI premium",
                description = "A premium version of the ACT-AI chatbot.",
                price = "€10.00",
                image = R.drawable.ai,
                payUiState = payState,
                onGooglePayButtonClick = this::requestPayment
            )
        }
    }

    private fun requestPayment() {
        val task = model.getLoadPaymentDataTask(priceLabel = "10.0")
        task.addOnCompleteListener(paymentDataLauncher::launch)
    }
}