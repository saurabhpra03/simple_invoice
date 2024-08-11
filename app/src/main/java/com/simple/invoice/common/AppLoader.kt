package com.simple.invoice.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.simple.invoice.ui.theme.BgLoader

@Composable
fun AppLoader(modifier: Modifier) {
    Box(modifier = modifier
        .background(color = BgLoader),
        contentAlignment = Alignment.Center){
        CircularProgressIndicator()
    }
}