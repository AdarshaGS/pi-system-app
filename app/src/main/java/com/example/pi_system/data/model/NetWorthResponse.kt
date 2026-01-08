package com.example.pi_system.data.model

import com.google.gson.annotations.SerializedName

data class NetWorthResponse(
    @SerializedName("totalAssets")
    val totalAssets: Double,

    @SerializedName("totalLiabilities")
    val totalLiabilities: Double,

    @SerializedName("netWorth")
    val netWorth: Double,

    @SerializedName("portfolioValue")
    val portfolioValue: Double,

    @SerializedName("savingsValue")
    val savingsValue: Double,

    @SerializedName("outstandingLoans")
    val outstandingLoans: Double,

    @SerializedName("outstandingTaxLiability")
    val outstandingTaxLiability: Double,

    @SerializedName("outstandingLendings")
    val outstandingLendings: Double,

    @SerializedName("netWorthAfterTax")
    val netWorthAfterTax: Double,

    @SerializedName("assetBreakdown")
    val assetBreakdown: Map<String, Double>,

    @SerializedName("liabilityBreakdown")
    val liabilityBreakdown: Map<String, Double>
)

