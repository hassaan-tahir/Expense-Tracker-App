package com.example.expensetracker.feature.stats

import android.view.LayoutInflater
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import com.example.expensetracker.R
import com.example.expensetracker.Utils
import com.example.expensetracker.feature.home.TransactionList
import com.example.expensetracker.viewmodel.StatsViewModel
import com.example.expensetracker.viewmodel.StatsViewModelFactory
import com.example.expensetracker.widget.ExpenseTextView
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet


@Composable
fun StatsScreen(navController: NavController) {
    Scaffold(topBar = {
        Box(modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 8.dp))

        {
            Image(
                painter = painterResource(id = R.drawable.chevron_left),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterStart),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Black)
            )
            ExpenseTextView(
                text = "Statistics",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center),
            )
            Image(
                painter = painterResource(id = R.drawable.dots_menu),
                contentDescription = null,
                modifier = Modifier.align(Alignment.CenterEnd),
                colorFilter = androidx.compose.ui.graphics.ColorFilter.tint(Color.Black)
            )
        }
    }) {

        val viewModel = StatsViewModelFactory(navController.context).create(StatsViewModel::class.java)
        val dataState = viewModel.entries.collectAsState(emptyList())
        val topExpense = viewModel.topEntries.collectAsState(initial = emptyList())

        Column(modifier = Modifier.padding(it)) {
            val entries = viewModel.getEntriesForChart(dataState.value)
            LineChart(entries = entries)
            Spacer(modifier = Modifier.height(16.dp))
            TransactionList(Modifier, list = topExpense.value, "Top Spending")
        }
    }
}


@Composable
fun LineChart(entries: List<Entry>) {
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(8.dp)
            .background(Color.White)
    ) {
        if (entries.isEmpty()) {
            ExpenseTextView(
                text = "No data available",
                fontSize = 16.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            AndroidView(
                factory = {
                    val view = LayoutInflater.from(context).inflate(R.layout.stats_line_chart, null)
                    view
                },
                modifier = Modifier.fillMaxWidth()
            ) { view ->
                val lineChart = view.findViewById<LineChart>(R.id.lineChart)

                val dataSet = LineDataSet(entries, "Expense").apply {
                    color = android.graphics.Color.parseColor("#FF2F7E79")
                    valueTextColor = android.graphics.Color.BLACK
                    lineWidth = 3f
                    axisDependency = YAxis.AxisDependency.LEFT
                    setDrawFilled(true)
                    setDrawValues(true) // Ensures all values are visible
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                    valueTextSize = 12f
                    valueTextColor = android.graphics.Color.parseColor("#FF2F7E79")
                    val drawable = ContextCompat.getDrawable(context, R.drawable.chart_gradient)
                    drawable?.let { fillDrawable = it }
                }

                lineChart.xAxis.apply {
                    position = XAxis.XAxisPosition.BOTTOM
                    granularity = 1f // Ensures all points are shown
                    setDrawGridLines(false)
                    setDrawAxisLine(true)
                }

                lineChart.axisLeft.apply {
                    setDrawGridLines(true)
                    granularity = 1f
                    axisMinimum = 0f
                }

                lineChart.axisRight.isEnabled = false

                lineChart.data = com.github.mikephil.charting.data.LineData(dataSet)
                lineChart.notifyDataSetChanged()
                lineChart.invalidate()
            }
        }
    }
}
