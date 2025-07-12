package com.safdev.calculator

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Calculator(
    state: CalculatorState,
    modifier: Modifier = Modifier,
    buttonSacing: Dp = 8.dp,
    onAction: (CalculationActions) -> Unit,

) {

    // Log the state here
    Log.d("CalculatorUI", "Recomposing. Operations: ${state.operations.joinToString { it.symbol }}, Numbers: ${state.numbers}, CurrentInput: ${state.currentInput}")


    Box(modifier = modifier){

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(buttonSacing),
        ) {

            // Display for the result
            if (state.result.isNotBlank()) {
                Text(
                    text = state.result,
                    textAlign = TextAlign.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 60.sp,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Display for the current expression (input)
            val expression = buildString {
                state.numbers.forEachIndexed { i, num ->
                    append(num)
                    if (i < state.operations.size) {
                        append(state.operations[i].symbol)
                    }
                }
                append(state.currentInput)
            }
            Log.d("CalculatorUI", "Built expression: $expression")

            Text(
                text = if (state.result.isNotBlank()) "" else expression,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 60.sp,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSacing),
            ) {
                CalculatorButton(
                    symbol = "AC",
                    modifier = Modifier
                        .background(Color(0xFF9E9E9E))
                        .aspectRatio(2f)
                        .weight(2f),
                    onClick = {
                        onAction(
                            CalculationActions.Clear
                        )
                    }
                )

                CalculatorButton(
                    symbol = "Del",
                    modifier = Modifier
                        .background(Color(0xFF9E9E9E))
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        onAction(
                            CalculationActions.Delete
                        )
                    }
                )

                CalculatorButton(
                    symbol = "/",
                    modifier = Modifier
                        .background(Color(0xFFF14C14))
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        onAction(
                            CalculationActions.Operation(CalculatorOperation.Divide)
                        )
                    }
                )

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSacing),
            ) {

                CalculatorButton(
                    symbol = "7",
                    modifier = Modifier
                        .background(Color(0xFF323232))
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        onAction(
                            CalculationActions.Number(7)
                        )
                    }
                )

                CalculatorButton(
                    symbol = "8",
                    modifier = Modifier
                        .background(Color(0xFF323232))
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        onAction(
                            CalculationActions.Number(8)
                        )
                    }
                )

                CalculatorButton(
                    symbol = "9",
                    modifier = Modifier
                        .background(Color(0xFF323232))
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        onAction(
                            CalculationActions.Number(9)
                        )
                    }
                )

                CalculatorButton(
                    symbol = "×",
                    modifier = Modifier
                        .background(Color(0xFFF14C14))
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        onAction(
                            CalculationActions.Operation(CalculatorOperation.Multiply)
                        )
                    }
                )

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSacing),
            ) {
                CalculatorButton(
                    symbol = "4",
                    modifier = Modifier
                        .background(Color(0xFF323232))
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        onAction(
                            CalculationActions.Number(4)
                        )
                    }
                )

                CalculatorButton(
                    symbol = "5",
                    modifier = Modifier
                        .background(Color(0xFF323232))
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        onAction(
                            CalculationActions.Number(5)
                        )
                    }
                )

                CalculatorButton(
                    symbol = "6",
                    modifier = Modifier
                        .background(Color(0xFF323232))
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        onAction(
                            CalculationActions.Number(6)
                        )
                    }
                )

                CalculatorButton(
                    symbol = "-",
                    modifier = Modifier
                        .background(Color(0xFFF14C14))
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        onAction(
                            CalculationActions.Operation(CalculatorOperation.Subtract)
                        )
                    }
                )

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSacing),
            ) {

                CalculatorButton(
                    symbol = "1",
                    modifier = Modifier
                        .background(Color(0xFF323232))
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        onAction(
                            CalculationActions.Number(1)
                        )
                    }
                )

                CalculatorButton(
                    symbol = "2",
                    modifier = Modifier
                        .background(Color(0xFF323232))
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        onAction(
                            CalculationActions.Number(2)
                        )
                    }
                )

                CalculatorButton(
                    symbol = "3",
                    modifier = Modifier
                        .background(Color(0xFF323232))
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        onAction(
                            CalculationActions.Number(3)
                        )
                    }
                )

                CalculatorButton(
                    symbol = "+",
                    modifier = Modifier
                        .background(Color(0xFFF14C14))
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        onAction(
                            CalculationActions.Operation(CalculatorOperation.Add)
                        )
                    }
                )

            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(buttonSacing),
            ) {

                CalculatorButton(
                    symbol = "0",
                    modifier = Modifier
                        .background(Color(0xFF323232))
                        .aspectRatio(2f)
                        .weight(2f),
                    onClick = {
                        onAction(
                            CalculationActions.Number(0)
                        )
                    }
                )

                CalculatorButton(
                    symbol = ".",
                    modifier = Modifier
                        .background(Color(0xFF323232))
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        onAction(
                            CalculationActions.Decimal
                        )
                    }
                )

                CalculatorButton(
                    symbol = "=",
                    modifier = Modifier
                        .background(Color(0xFFF14C14))
                        .aspectRatio(1f)
                        .weight(1f),
                    onClick = {
                        onAction(
                            CalculationActions.Calculate
                        )
                    }
                )

            }
            Spacer(modifier = Modifier.height(22.dp))
        }
    }
}