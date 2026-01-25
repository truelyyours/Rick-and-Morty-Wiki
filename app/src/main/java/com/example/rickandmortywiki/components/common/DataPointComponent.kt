package com.example.rickandmortywiki.components.common

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.rickandmortywiki.ui.theme.RickAction
import com.example.rickandmortywiki.ui.theme.RickTextPrimary

data class DataPoint(val title: String,
    val description: String)

@Composable
fun DataPointComponent(dataPoint: DataPoint) {
    /*Data point of character*/
    Column {
        Text(
            text = dataPoint.title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = RickAction
        )
        Text(
            text = dataPoint.description,
            fontSize = 24.sp,
            color = RickTextPrimary
        )
    }
}