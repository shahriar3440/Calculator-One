package com.safdev.calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.safdev.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CalculatorTheme {

                val viewModel = viewModel<CalculatorViewModel>()
                val buttonSpacing = 8.dp
                val state = viewModel.state
                Calculator(
                    state = state,
                    onAction = viewModel::onAction,
                    buttonSacing = buttonSpacing,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFF585858))
                        .padding(24.dp)
                )

            }
        }
    }
}
