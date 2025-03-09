package com.example.gimmea.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.gimmea.R

@OptIn(ExperimentalTextApi::class)
val dynaPuffFontFamily = FontFamily(
    Font(
        R.font.dynapuff_variable,
        variationSettings = FontVariation.Settings(
            FontVariation.weight(950),
            FontVariation.width(30f),
        )
    )
)

val patrickHandFontFamily = FontFamily(
    Font(R.font.patrickhand_regular)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = patrickHandFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = TextStyle(
        fontFamily = dynaPuffFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelMedium = TextStyle(
        fontFamily = dynaPuffFontFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.5.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )

)

@Preview
@Composable
fun PreviewFonts() {
    Column(
        modifier = Modifier.background(color = Color.White).padding(16.dp)
    ) {
        Text(text = "text", fontFamily = dynaPuffFontFamily, fontWeight = FontWeight.Light)
        Text(text = "text", fontFamily = dynaPuffFontFamily, fontWeight = FontWeight.Normal)
        Text(
            text = "text",
            fontFamily = dynaPuffFontFamily,
            fontWeight = FontWeight.Normal,
            fontStyle = FontStyle.Italic
        )
        Text(text = "text", fontFamily = patrickHandFontFamily, fontWeight = FontWeight.Medium)
        Text(text = "text", fontFamily = patrickHandFontFamily, fontWeight = FontWeight.Bold)
    }
}