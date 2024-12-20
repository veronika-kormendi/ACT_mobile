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

import android.content.Context
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject


object PaymentsUtil {


    private val baseRequest = JSONObject()
        .put("apiVersion", 2)
        .put("apiVersionMinor", 0)

    private val gatewayTokenizationSpecification: JSONObject =
        JSONObject()
            .put("type", "PAYMENT_GATEWAY")
            .put("parameters", JSONObject(PaymentVariables.PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS))


    private val allowedCardNetworks = JSONArray(PaymentVariables.SUPPORTED_NETWORKS)

    private val allowedCardAuthMethods = JSONArray(PaymentVariables.SUPPORTED_METHODS)

    private fun cardPaymentMethod(): JSONObject =
        JSONObject()
            .put("type", "CARD")
            .put("parameters", JSONObject()
                    .put("allowedAuthMethods", allowedCardAuthMethods)
                    .put("allowedCardNetworks", allowedCardNetworks)
                    .put("billingAddressRequired", true)
                    .put("billingAddressParameters", JSONObject()
                            .put("format", "FULL")
                    )
            )

    private val cardPaymentMethod: JSONObject = cardPaymentMethod()
        .put("tokenizationSpecification", gatewayTokenizationSpecification)

    val allowedPaymentMethods: JSONArray = JSONArray().put(cardPaymentMethod)

    fun isReadyToPayRequest(): JSONObject? =
        try {
            baseRequest
                .put("allowedPaymentMethods", JSONArray().put(cardPaymentMethod()))
        } catch (e: JSONException) {
            null
        }

    private val merchantInfo: JSONObject =
        JSONObject().put("merchantName", "ACT AI")

    fun createPaymentsClient(context: Context): PaymentsClient {
        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(PaymentVariables.PAYMENTS_ENVIRONMENT)
            .build()

        return Wallet.getPaymentsClient(context, walletOptions)
    }

    private fun getTransactionInfo(price: String): JSONObject =
        JSONObject()
            .put("totalPrice", price)
            .put("totalPriceStatus", "FINAL")
            .put("countryCode", PaymentVariables.COUNTRY_CODE)
            .put("currencyCode", PaymentVariables.CURRENCY_CODE)

    fun getPaymentDataRequest(priceLabel: String): JSONObject =
        baseRequest
            .put("allowedPaymentMethods", allowedPaymentMethods)
            .put("transactionInfo", getTransactionInfo(priceLabel))
            .put("merchantInfo", merchantInfo)
            .put("shippingAddressRequired", false)
            .put("shippingAddressParameters", JSONObject()
                    .put("phoneNumberRequired", false)
                    .put("allowedCountryCodes", JSONArray(listOf("US", "IE", "GB")))
            )
}

object PaymentVariables {
    const val PAYMENTS_ENVIRONMENT = WalletConstants.ENVIRONMENT_TEST

    val SUPPORTED_NETWORKS = listOf(
        "AMEX",
        "DISCOVER",
        "JCB",
        "MASTERCARD",
        "VISA")

    val SUPPORTED_METHODS = listOf(
        "PAN_ONLY",
        "CRYPTOGRAM_3DS")

    const val COUNTRY_CODE = "IE"

    const val CURRENCY_CODE = "EUR"

    private const val PAYMENT_GATEWAY_TOKENIZATION_NAME = "example"

    val PAYMENT_GATEWAY_TOKENIZATION_PARAMETERS = mapOf(
        "gateway" to PAYMENT_GATEWAY_TOKENIZATION_NAME,
        "gatewayMerchantId" to "exampleGatewayMerchantId"
    )
}