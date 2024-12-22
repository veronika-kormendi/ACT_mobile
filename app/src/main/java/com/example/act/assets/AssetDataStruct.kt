package com.example.act.assets

data class StockData(
    val Open: Double,
    val High: Double,
    val Low: Double,
    val Close: Double,
    val Volume: Double
)

data class StockAnalysis(
    val ticker_symbol: String,
    val analysis: String
)