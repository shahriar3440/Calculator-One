package com.safdev.calculator

sealed class CalculationActions {
    data class Number(val number: Int) : CalculationActions()
    object Clear : CalculationActions()
    object Delete : CalculationActions()
    object Decimal : CalculationActions()
    object Calculate : CalculationActions()
    data class Operation(val operation: CalculatorOperation): CalculationActions()


}
