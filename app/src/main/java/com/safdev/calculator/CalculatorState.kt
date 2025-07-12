package com.safdev.calculator

data class CalculatorState(
    val numbers: List<String> = emptyList(),
    val operations: List<CalculatorOperation> = emptyList(),
    val currentInput: String = "",
    val number1: String = "",
    val number2: String = "",
    val operation: CalculatorOperation? = null,
    val isNumber1Input: Boolean = true,
    val isDecimalUsed: Boolean = false,
    val isCalculationInProgress: Boolean = false,
    val result: String = "",
    val errorMessage: String? = null
)
