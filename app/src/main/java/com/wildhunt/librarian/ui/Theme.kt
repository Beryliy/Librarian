package com.wildhunt.librarian.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.wildhunt.librarian.R

object Colors {
    val blue = Color(0xff458CE1)
    val grey = Color(0xff222D3A)
    val greyGradient = Color(0xff243240)
    val greyDark = Color(0xff131A22)
    val greyLight = Color(0xff53657B)
}

val robotoFamily = FontFamily(
    Font(R.font.roboto_regular, FontWeight.Normal),
    Font(R.font.roboto_bold, FontWeight.Bold),
)
