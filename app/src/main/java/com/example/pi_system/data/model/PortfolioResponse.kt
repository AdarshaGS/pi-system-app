package com.example.pi_system.data.model

import com.google.gson.annotations.SerializedName

data class PortfolioResponse(
    @SerializedName("score")
    val score: Int,

    @SerializedName("assessment")
    val assessment: String,

    @SerializedName("recommendations")
    val recommendations: List<String>,

    @SerializedName("totalInvestment")
    val totalInvestment: Double,

    @SerializedName("currentValue")
    val currentValue: Double,

    @SerializedName("totalProfitLoss")
    val totalProfitLoss: Double,

    @SerializedName("totalProfitLossPercentage")
    val totalProfitLossPercentage: Double,

    @SerializedName("sectorAllocation")
    val sectorAllocation: Map<String, Double>,

    @SerializedName("insights")
    val insights: Insights,

    @SerializedName("riskSummary")
    val riskSummary: RiskSummary,

    @SerializedName("nextBestAction")
    val nextBestAction: NextBestAction,

    @SerializedName("scoreExplanation")
    val scoreExplanation: ScoreExplanation,

    @SerializedName("marketCapAllocation")
    val marketCapAllocation: MarketCapAllocation,

    @SerializedName("dataFreshness")
    val dataFreshness: DataFreshness,

    @SerializedName("savingsTotal")
    val savingsTotal: Double,

    @SerializedName("loansOutstanding")
    val loansOutstanding: Double,

    @SerializedName("insuranceCoverTotal")
    val insuranceCoverTotal: Double
)

data class Insights(
    @SerializedName("critical")
    val critical: List<InsightItem>,

    @SerializedName("warning")
    val warning: List<InsightItem>,

    @SerializedName("info")
    val info: List<InsightItem>
)

data class InsightItem(
    @SerializedName("type")
    val type: String,

    @SerializedName("category")
    val category: String,

    @SerializedName("priority")
    val priority: Int,

    @SerializedName("message")
    val message: String,

    @SerializedName("recommendedAction")
    val recommendedAction: String
)

data class RiskSummary(
    @SerializedName("criticalCount")
    val criticalCount: Int,

    @SerializedName("warningCount")
    val warningCount: Int,

    @SerializedName("infoCount")
    val infoCount: Int
)

data class NextBestAction(
    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String,

    @SerializedName("urgency")
    val urgency: String
)

data class ScoreExplanation(
    @SerializedName("baseScore")
    val baseScore: Int,

    @SerializedName("penalties")
    val penalties: List<String>,

    @SerializedName("finalScore")
    val finalScore: Int
)

data class MarketCapAllocation(
    @SerializedName("largeCapPercentage")
    val largeCapPercentage: Double,

    @SerializedName("midCapPercentage")
    val midCapPercentage: Double,

    @SerializedName("smallCapPercentage")
    val smallCapPercentage: Double
)

data class DataFreshness(
    @SerializedName("priceLastUpdatedAt")
    val priceLastUpdatedAt: String,

    @SerializedName("stale")
    val stale: Boolean
)

