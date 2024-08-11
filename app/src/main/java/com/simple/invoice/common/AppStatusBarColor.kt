package com.simple.invoice.common

import androidx.activity.ComponentActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.simple.invoice.ui.theme.White

@Composable
fun AppStatusBarColor(color: Color = White) {
    val context = LocalContext.current
    val view = LocalView.current
    val window = (context as? ComponentActivity)?.window

    LaunchedEffect(color) {
        window?.let {
            it.statusBarColor = color.toArgb()
            WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = false
        }
    }
}
