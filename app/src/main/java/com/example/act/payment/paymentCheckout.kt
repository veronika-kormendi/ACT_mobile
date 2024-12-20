/*
 * Copyright 2024 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.act.R
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
    payUiState: PaymentUiState = PaymentUiState.NotStarted,
) {
    val padding = 20.dp
    val black = Color(0xff000000.toInt())
    val grey = Color(0xffeeeeee.toInt())

    if (payUiState is PaymentUiState.PaymentCompleted) {
        Column(
            modifier = Modifier
                .testTag("successScreen")
                .background(grey)
                .padding(padding)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                contentDescription = "checkmark",
                painter = painterResource(R.drawable.check),
                modifier = Modifier
                    .width(200.dp)
                    .height(200.dp)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "${payUiState.payerName} paid.",
                fontSize = 17.sp,
                color = Color.DarkGray,
                textAlign = TextAlign.Center
            )
        }

    } else {
        Column(
            modifier = Modifier
                .background(grey)
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
                color = black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Text(text = price, color = black)
            Spacer(Modifier)
            Text(
                text = "Description",
                color = black,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                color = black
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
                image = R.drawable.logo,
                payUiState = payState,
                onGooglePayButtonClick = this::requestPayment,
            )
        }
    }

    private fun requestPayment() {
        val task = model.getLoadPaymentDataTask(priceLabel = "10.0")
        task.addOnCompleteListener(paymentDataLauncher::launch)
    }
}