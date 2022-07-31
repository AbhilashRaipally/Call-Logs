package com.example.readcalllogs

import android.content.Context
import android.database.Cursor
import android.os.Bundle
import android.provider.CallLog.Calls.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.readcalllogs.ui.theme.ReadCallLogsTheme
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ReadCallLogsTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        ReadCallLogs {
                            ShowCallLogs()
                        }

                    }
                }
            }
        }
    }
}


@Composable
private fun ShowCallLogs() {
    val callLogs = getCallLogs(LocalContext.current)
    Column {
        TableScreen(callLogs)
    }
}

@Composable
fun RowScope.TableCell(
    text: String,
    weight: Float
) {
    Text(
        text = text,
        Modifier
            .border(1.dp, Color.Black)
            .weight(weight)
            .padding(5.dp)
    )
}

@Composable
fun TableScreen(tableData: List<CallDetails>) {
    // Each cell of a column must have the same weight.
    val column1Weight = .4f // 40%
    val column2Weight = .5f // 40%
    val column3Weight = .2f // 20%
    val column4Weight = .3f

    // The LazyColumn will be our table. Notice the use of the weights below
    LazyColumn(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Here is the header
        item {
            Row(Modifier.background(Color.Yellow)) {
                TableCell(text = "Date", weight = column1Weight)
                TableCell(text = "Number", weight = column2Weight)
                TableCell(text = "Type", weight = column3Weight)
                TableCell(text = "Duration", weight = column4Weight)
            }
        }


        // Here are all the lines of your table.
        items(tableData) {
            //val (id, text) = it
            Row(Modifier.fillMaxWidth()) {
                TableCell(text = it.date, weight = column1Weight)
                TableCell(text = it.number, weight = column2Weight)
                TableCell(text = it.type.code, weight = column3Weight)
                TableCell(text = it.duration, weight = column4Weight)
            }
        }
    }
}

private fun getCallLogs(context: Context): List<CallDetails> {
    val c = context.applicationContext
    val projection = arrayOf(CACHED_NAME, NUMBER, TYPE, DATE, DURATION)
    val cursor = c.contentResolver.query(
        CONTENT_URI,
        projection,
        null,
        null,
        null,
        null
    )
    return cursorToCallDetailsList(cursor)
}

private fun cursorToCallDetailsList(cursor: Cursor?): List<CallDetails> {
    val arraySize = cursor?.count ?: 0
    val callDetailsList = mutableListOf<CallDetails>()
    cursor?.use {
        for (i in 0 until arraySize) {
            it.moveToNext()
            val cachedName: Int = it.getColumnIndex(CACHED_NAME)
            val number: Int = it.getColumnIndex(NUMBER)
            val type: Int = it.getColumnIndex(TYPE)
            val date: Int = it.getColumnIndex(DATE)
            val duration: Int = it.getColumnIndex(DURATION)

            val simpleDate = SimpleDateFormat("d/M/yy hh:mm")
            val callDate = simpleDate.format(Date(it.getString(date).toLong()))

            val callType = when (it.getString(type).toInt()) {
                OUTGOING_TYPE -> CallType.OUTGOING
                INCOMING_TYPE -> CallType.INCOMING
                MISSED_TYPE -> CallType.MISSED
                REJECTED_TYPE -> CallType.REJECTED
                else -> CallType.UNKNOWN
            }


            val timeFormat = "%02d:%02d:%02d"
            val durationInSecs = it.getString(duration).toLong()
            val hours = durationInSecs / 3600
            val mins = (durationInSecs % 3600) / 60
            val secs = durationInSecs % 60
            val callDuration = String.format(timeFormat, hours, mins, secs)

            callDetailsList.add(
                CallDetails(
                    name = it.getString(cachedName),
                    number = it.getString(number),
                    type = callType,
                    date = callDate,
                    duration = callDuration
                )

            )
        }
    }

    return callDetailsList
}

data class CallDetails(
    val name: String?,
    val number: String,
    val type: CallType,
    val date: String,
    val duration: String
)

enum class CallType(val code: String) {
    INCOMING("IN"), OUTGOING("OUT"), MISSED("MISS"), REJECTED("REJ"), UNKNOWN("NA")
}