package com.mindmatrix.pashuaahar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.mindmatrix.pashuaahar.presentation.MainViewModel
import com.mindmatrix.pashuaahar.presentation.PashuAaharApp
import com.mindmatrix.pashuaahar.presentation.theme.PashuAaharTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PashuAaharTheme {
                PashuAaharApp(viewModel = viewModel)
            }
        }
    }
}
