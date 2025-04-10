package com.example.assetxandroidapp

import android.content.Context
import android.util.AttributeSet
import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.assetxandroidapp.data.Asset
import com.example.assetxandroidapp.data.Expense
import com.example.assetxandroidapp.viewmodel.DashboardViewModel
import com.example.assetxandroidapp.viewmodel.DashboardViewModelFactory
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.HorizontalBarChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                DashboardScreen()
            }
        }
    }
}

// Custom extension function to reset the chart views
fun BarChart.resetChart() {
    resetZoom()
    fitScreen()
    resetViewPortOffsets()
    moveViewToX(0f)
}

fun HorizontalBarChart.resetChart() {
    resetZoom()
    fitScreen()
    resetViewPortOffsets()
    moveViewToX(0f)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen( viewModel: DashboardViewModel = viewModel(factory = DashboardViewModelFactory())) {
    val assets by viewModel.assets.collectAsState()
    val expenses by viewModel.expenses.collectAsState()

    var selectedAssetChart by remember { mutableStateOf("Assets by Type") }
    var selectedExpenseChart by remember { mutableStateOf("Expenses by Payment Status") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("IT Asset & Expense Dashboard") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = true,
                    onClick = { /* Navigate to Home */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.List, contentDescription = "List") },
                    label = { Text("List") },
                    selected = false,
                    onClick = { /* Navigate to List */ }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                    label = { Text("Settings") },
                    selected = false,
                    onClick = { /* Navigate to Settings */ }
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Asset Overview",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AssetChartSelector(selectedChart = selectedAssetChart, onChartSelected = { selectedAssetChart = it })
                IconButton(onClick = { (chartRefs["assets"] as? BarChart)?.resetChart() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset Chart")
                }
            }
            AssetChart(assets = assets, chartType = selectedAssetChart)

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Expense Overview",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExpenseChartSelector(selectedChart = selectedExpenseChart, onChartSelected = { selectedExpenseChart = it })
                IconButton(onClick = { (chartRefs["expenses"] as? HorizontalBarChart)?.resetChart() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Reset Chart")
                }
            }
            ExpenseChart(assets = assets, expenses = expenses, chartType = selectedExpenseChart)
        }
    }
}

// Store chart references globally to access from reset buttons
private val chartRefs = mutableMapOf<String, Any?>()

@Composable
fun AssetChartSelector(selectedChart: String, onChartSelected: (String) -> Unit) {
    val options = listOf("Assets by Type", "Assets by Status", "Assets by Personnel", "Value by Personnel", "Value by Assets")
    DropdownMenuSelector(options, selectedChart, onChartSelected)
}

@Composable
fun ExpenseChartSelector(selectedChart: String, onChartSelected: (String) -> Unit) {
    val options = listOf("Expenses by Payment Status", "Expenses by Vendor", "Total Amount by Month", "Total Amount by Year")
    DropdownMenuSelector(options, selectedChart, onChartSelected)
}

@Composable
fun DropdownMenuSelector(options: List<String>, selectedOption: String, onOptionSelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    Box {
        OutlinedButton(onClick = { expanded = true }) { Text(selectedOption) }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option) },
                    onClick = {
                        onOptionSelected(option)
                        expanded = false
                    }
                )
            }
        }
    }
}

@Composable
fun AssetChart(assets: List<Asset>, chartType: String) {
    AndroidView(
        factory = { context ->
            BarChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)
                animateY(1000)
                legend.isEnabled = true
                legend.textSize = 12f
                setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        e?.let { entry: Entry -> highlightValue(Highlight(entry.x, entry.y, h?.dataSetIndex ?: 0)) }
                    }
                    override fun onNothingSelected() {}
                })
                marker = CustomMarkerView(context, R.layout.marker_view, this)
                chartRefs["assets"] = this
            }
        },
        update = { chart ->
            val entries = when (chartType) {
                "Assets by Type" -> assets.groupBy { it.assetType }
                    .entries.mapIndexed { i, (type, group) -> BarEntry(i.toFloat(), group.size.toFloat(), type) }
                "Assets by Status" -> assets.groupBy { it.status }
                    .entries.mapIndexed { i, (status, group) -> BarEntry(i.toFloat(), group.size.toFloat(), status) }
                "Assets by Personnel" -> assets.groupBy { it.personnel }.filter { (personnel, _) -> personnel != "Unassigned" }
                    .entries.mapIndexed { i, (person, group) -> BarEntry(i.toFloat(), group.size.toFloat(), person) }
                "Value by Personnel" -> assets.groupBy { it.personnel }.filter { (personnel, _) -> personnel != "Unassigned" }
                    .entries.mapIndexed { i, (person, group) ->
                        val value = (group.sumOf { it.assetValue.toDouble() } / 1000).roundToInt().toFloat()
                        BarEntry(i.toFloat(), value, person)
                    }
                "Value by Assets" -> assets.groupBy { it.assetType }
                    .entries.mapIndexed { i, (type, group) -> BarEntry(i.toFloat(), group.sumOf { it.assetValue.toDouble().roundToInt() }.toFloat(), type) }

                else -> emptyList()
            }
            val dataSet = BarDataSet(entries, "$chartType Count").apply {
                setColors(intArrayOf(android.R.color.holo_blue_light, android.R.color.holo_green_light), chart.context)
                valueTextSize = 12f
                valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return if (chartType == "Value by Assets" || chartType == "Assets by Type" || chartType == "Assets by Status" || chartType == "Assets by Personnel") {
                            value.toInt().toString()
                        } else if (chartType == "Value by Personnel") {
                            "${value.toInt()}K"
                        } else {
                            value.toString()
                        }
                    }
                }
            }
            chart.data = BarData(dataSet).apply { barWidth = 0.8f }
            chart.xAxis.apply {
                setDrawLabels(true)
                granularity = 1f
                textSize = 10f
                valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return entries.getOrNull(value.toInt())?.data as? String ?: value.toString()
                    }
                }
            }
            chart.axisLeft.textSize = 12f
            chart.axisRight.isEnabled = false
            chart.invalidate()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}

@Composable
fun ExpenseChart(assets: List<Asset>, expenses: List<Expense>, chartType: String) {
    // hard coded for now, need to dynamically generate this (use foreign key in expense table => vendor)
    val vendorNames = mapOf(1 to "Telstra", 2 to "Optus", 3 to "AWS", 5 to "Microsoft", 6 to "Dell", 7 to "ConsultCorp")

    AndroidView(
        factory = { context ->
            HorizontalBarChart(context).apply {
                description.isEnabled = false
                setTouchEnabled(true)
                isDragEnabled = true
                setScaleEnabled(true)
                setPinchZoom(true)
                animateY(1000)
                legend.isEnabled = true
                legend.textSize = 12f
                setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
                    override fun onValueSelected(e: Entry?, h: Highlight?) {
                        e?.let { entry: Entry -> highlightValue(Highlight(entry.x, entry.y, h?.dataSetIndex ?: 0)) }
                    }
                    override fun onNothingSelected() {}
                })
                marker = CustomMarkerView(context, R.layout.marker_view, this)
                chartRefs["expenses"] = this
            }
        },
        update = { chart ->
            val entries = when (chartType) {
                "Expenses by Payment Status" -> expenses.groupBy { it.paymentStatus ?: "Unknown" }
                    .entries.mapIndexed { i, (status, group) -> BarEntry(i.toFloat(), group.size.toFloat(), status) }
                "Expenses by Vendor" -> expenses.groupBy { it.vendorId ?: -1 }
                    .entries.mapIndexed { i, (vendorId, group) ->
                        val vendorName = vendorNames[vendorId] ?: "Unknown Vendor $vendorId"
                        BarEntry(i.toFloat(), group.size.toFloat(), vendorName)
                    }
                "Total Amount by Month" -> expenses.groupBy {
                    it.dateIncurred?.let { date ->
                        LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME).format(DateTimeFormatter.ofPattern("MMM"))
                    } ?: "Unknown"
                }.entries.mapIndexed { i, (month, group) -> BarEntry(i.toFloat(), group.sumOf { it.amount ?: 0.0 }.toFloat(), month) }
                "Total Amount by Year" -> expenses.groupBy {
                    it.dateIncurred?.let { date ->
                        LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME).year
                    } ?: 0
                }.entries.mapIndexed { i, (year, group) -> BarEntry(i.toFloat(), group.sumOf { it.amount ?: 0.0 }.toFloat(), year.toString()) }

                else -> emptyList()
            }
            val dataSet = BarDataSet(entries, "$chartType Count").apply {
                setColors(
                    intArrayOf(android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_purple),
                    chart.context
                )
                valueTextSize = 12f
                valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return value.toInt().toString()
                    }
                }
            }
            chart.data = BarData(dataSet).apply { barWidth = 0.8f }
            chart.xAxis.apply {
                setDrawLabels(true)
                granularity = 1f
                textSize = 10f
                labelCount = entries.size
                valueFormatter = object : com.github.mikephil.charting.formatter.ValueFormatter() {
                    override fun getFormattedValue(value: Float): String {
                        return entries.getOrNull(value.toInt())?.data as? String ?: value.toString()
                    }
                }
            }
            chart.axisLeft.textSize = 12f
            chart.axisRight.isEnabled = false
            chart.invalidate()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    )
}

class CustomMarkerView(context: Context, layoutResource: Int, private val chart: BarChart) : MarkerView(context, layoutResource) {
    constructor(context: Context, attrs: AttributeSet) : this(context, R.layout.marker_view, BarChart(context))
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : this(context, R.layout.marker_view, BarChart(context))
    constructor(context: Context) : this(context, R.layout.marker_view, BarChart(context))

    constructor(context: Context, layoutResource: Int, chart: HorizontalBarChart) : this(context, layoutResource, chart as BarChart)

    private val tvContent: TextView = findViewById(android.R.id.text1)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        e?.let {
            val label = e.data as? String ?: "Unknown"
            val value = if (e.y.toInt().toFloat() == e.y) e.y.toInt() else e.y
            tvContent.text = "$label: $value"
        }
        super.refreshContent(e, highlight)
    }

    override fun getOffsetForDrawingAtPoint(posX: Float, posY: Float): com.github.mikephil.charting.utils.MPPointF {
        return com.github.mikephil.charting.utils.MPPointF(-width / 2f, -height.toFloat())
    }
}