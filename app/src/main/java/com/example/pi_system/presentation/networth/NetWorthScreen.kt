package com.example.pi_system.presentation.networth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.pi_system.data.model.NetWorthResponse
import com.example.pi_system.domain.util.Resource
import java.text.NumberFormat
import java.util.Locale

@Composable
fun NetWorthScreen(
    viewModel: NetWorthViewModel = hiltViewModel()
) {
    val netWorthState by viewModel.netWorthState.observeAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        when (val state = netWorthState) {
            is Resource.Loading -> {
                LoadingView()
            }
            is Resource.Success -> {
                state.data?.let { netWorth ->
                    NetWorthContent(netWorth = netWorth, onRefresh = { viewModel.loadNetWorth() })
                }
            }
            is Resource.Error -> {
                ErrorView(
                    message = state.message ?: "Failed to load net worth",
                    onRetry = { viewModel.loadNetWorth() }
                )
            }
            null -> {
                LoadingView()
            }
        }
    }
}

@Composable
private fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorView(message: String, onRetry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Error",
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(64.dp)
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Button(onClick = onRetry) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Retry",
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Retry")
            }
        }
    }
}

@Composable
private fun NetWorthContent(netWorth: NetWorthResponse, onRefresh: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Net Worth Summary Card
        NetWorthSummaryCard(
            netWorth = netWorth.netWorth,
            netWorthAfterTax = netWorth.netWorthAfterTax
        )

        // Assets & Liabilities Overview
        AssetsLiabilitiesCard(
            totalAssets = netWorth.totalAssets,
            totalLiabilities = netWorth.totalLiabilities
        )

        // Portfolio & Savings
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CompactCard(
                modifier = Modifier.weight(1f),
                title = "Portfolio",
                value = netWorth.portfolioValue,
                icon = Icons.Default.TrendingUp,
                color = Color(0xFF2196F3)
            )
            CompactCard(
                modifier = Modifier.weight(1f),
                title = "Savings",
                value = netWorth.savingsValue,
                icon = Icons.Default.Savings,
                color = Color(0xFF4CAF50)
            )
        }

        // Loans & Lendings
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CompactCard(
                modifier = Modifier.weight(1f),
                title = "Loans",
                value = netWorth.outstandingLoans,
                icon = Icons.Default.CreditCard,
                color = Color(0xFFF44336)
            )
            CompactCard(
                modifier = Modifier.weight(1f),
                title = "Lendings",
                value = netWorth.outstandingLendings,
                icon = Icons.Default.AccountBalance,
                color = Color(0xFFFFA726)
            )
        }

        // Tax Liability Card (if exists)
        if (netWorth.outstandingTaxLiability > 0) {
            TaxLiabilityCard(taxLiability = netWorth.outstandingTaxLiability)
        }

        // Asset Breakdown Chart
        if (netWorth.assetBreakdown.isNotEmpty()) {
            AssetBreakdownChart(assetBreakdown = netWorth.assetBreakdown)
        }

        // Liability Breakdown Chart
        if (netWorth.liabilityBreakdown.isNotEmpty()) {
            LiabilityBreakdownChart(liabilityBreakdown = netWorth.liabilityBreakdown)
        }
    }
}

@Composable
private fun NetWorthSummaryCard(netWorth: Double, netWorthAfterTax: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total Net Worth",
                style = MaterialTheme.typography.titleLarge,
                color = Color(0xFF009688) // Teal color for title
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = formatCurrency(netWorth),
                style = MaterialTheme.typography.displaySmall.copy(
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = Color(0xFF009688) // Teal color for value
            )

            if (netWorthAfterTax != netWorth) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "After Tax: ${formatCurrency(netWorthAfterTax)}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF009688).copy(alpha = 0.7f) // Teal with transparency
                )
            }
        }
    }
}

@Composable
private fun AssetsLiabilitiesCard(totalAssets: Double, totalLiabilities: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AssetLiabilityItem(
                label = "Total Assets",
                amount = totalAssets,
                color = Color(0xFF4CAF50)
            )

            HorizontalDivider(
                modifier = Modifier
                    .height(60.dp)
                    .width(1.dp),
                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
            )

            AssetLiabilityItem(
                label = "Total Liabilities",
                amount = totalLiabilities,
                color = Color(0xFFF44336)
            )
        }
    }
}

@Composable
private fun AssetLiabilityItem(label: String, amount: Double, color: Color) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = formatCurrency(amount),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = color
        )
    }
}

@Composable
private fun CompactCard(
    modifier: Modifier = Modifier,
    title: String,
    value: Double,
    icon: ImageVector,
    color: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = formatCurrency(value),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun TaxLiabilityCard(taxLiability: Double) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFFFA726).copy(alpha = 0.1f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Outstanding Tax Liability",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Pending tax payments",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
            Text(
                text = formatCurrency(taxLiability),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFFFFA726)
            )
        }
    }
}

private fun formatCurrency(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    return formatter.format(amount)
}

@Composable
private fun AssetBreakdownChart(assetBreakdown: Map<String, Double>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Asset Breakdown",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Calculate total for percentages
            val total = assetBreakdown.values.sum()

            // Donut Chart with center value
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                contentAlignment = Alignment.Center
            ) {
                DonutChartWithCenter(
                    data = assetBreakdown,
                    total = total,
                    centerLabel = "Total Assets",
                    colors = listOf(
                        Color(0xFF4CAF50), // Green for STOCK
                        Color(0xFF2196F3), // Blue for CASH
                        Color(0xFFFFA726), // Orange for LENDING
                        Color(0xFF9C27B0)  // Purple for OTHER
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Legend
            assetBreakdown.entries.forEachIndexed { index, entry ->
                val color = when (index % 4) {
                    0 -> Color(0xFF4CAF50)
                    1 -> Color(0xFF2196F3)
                    2 -> Color(0xFFFFA726)
                    else -> Color(0xFF9C27B0)
                }
                ChartLegendItem(
                    label = formatAssetLabel(entry.key),
                    value = entry.value,
                    percentage = (entry.value / total * 100),
                    color = color
                )
                if (index < assetBreakdown.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun LiabilityBreakdownChart(liabilityBreakdown: Map<String, Double>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = "Liability Breakdown",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))

            // Calculate total for percentages
            val total = liabilityBreakdown.values.sum()

            // Donut Chart with center value
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp),
                contentAlignment = Alignment.Center
            ) {
                DonutChartWithCenter(
                    data = liabilityBreakdown,
                    total = total,
                    centerLabel = "Total Liabilities",
                    colors = listOf(
                        Color(0xFFF44336), // Red for PERSONAL_LOAN
                        Color(0xFFFF5722), // Deep Orange for OTHER
                        Color(0xFFE91E63), // Pink for additional items
                        Color(0xFFFF9800)  // Amber for additional items
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Legend
            liabilityBreakdown.entries.forEachIndexed { index, entry ->
                val color = when (index % 4) {
                    0 -> Color(0xFFF44336)
                    1 -> Color(0xFFFF5722)
                    2 -> Color(0xFFE91E63)
                    else -> Color(0xFFFF9800)
                }
                ChartLegendItem(
                    label = formatLiabilityLabel(entry.key),
                    value = entry.value,
                    percentage = (entry.value / total * 100),
                    color = color
                )
                if (index < liabilityBreakdown.size - 1) {
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
private fun DonutChartWithCenter(
    data: Map<String, Double>,
    total: Double,
    centerLabel: String,
    colors: List<Color>
) {
    val isDarkMode = isSystemInDarkTheme()
    // Match the card background color in both modes
    val backgroundColor = if (isDarkMode) Color(0xFF2C2C2C) else Color.White
    val textColor = if (isDarkMode) Color.White else Color(0xFF212121)

    Box(
        modifier = Modifier.size(260.dp),
        contentAlignment = Alignment.Center
    ) {
        // Draw the donut chart with percentage labels
        Canvas(modifier = Modifier.size(260.dp)) {
            val canvasSize = 220.dp.toPx()
            val radius = canvasSize / 2
            val strokeWidth = 70f
            val centerX = size.width / 2
            val centerY = size.height / 2

            var startAngle = -90f

            data.entries.forEachIndexed { index, entry ->
                val sweepAngle = (entry.value / total * 360).toFloat()
                val color = colors[index % colors.size]

                // Draw arc
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = androidx.compose.ui.geometry.Offset(
                        (size.width - canvasSize) / 2,
                        (size.height - canvasSize) / 2
                    ),
                    size = androidx.compose.ui.geometry.Size(canvasSize, canvasSize),
                    style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
                )


                startAngle += sweepAngle
            }

            // Draw center circle (donut hole)
            drawCircle(
                color = backgroundColor,
                radius = radius - strokeWidth,
                center = androidx.compose.ui.geometry.Offset(centerX, centerY)
            )
        }

        // Center text with total value
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = centerLabel,
                style = MaterialTheme.typography.bodySmall,
                color = textColor.copy(alpha = 0.7f),
                fontSize = 11.sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatCurrency(total),
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                color = textColor
            )
        }
    }
}

@Composable
private fun ChartLegendItem(
    label: String,
    value: Double,
    percentage: Double,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(color, shape = CircleShape)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = formatCurrency(value),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = String.format(Locale.US, "%.1f%%", percentage),
                style = MaterialTheme.typography.bodySmall,
                color = color
            )
        }
    }
}

private fun formatAssetLabel(key: String): String {
    return when (key) {
        "STOCK" -> "Stocks"
        "CASH" -> "Cash"
        "LENDING" -> "Lending"
        "OTHER" -> "Other Assets"
        else -> key.replace("_", " ").lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}

private fun formatLiabilityLabel(key: String): String {
    return when (key) {
        "PERSONAL_LOAN" -> "Personal Loan"
        "OTHER" -> "Other Liabilities"
        else -> key.replace("_", " ").lowercase()
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }
    }
}
