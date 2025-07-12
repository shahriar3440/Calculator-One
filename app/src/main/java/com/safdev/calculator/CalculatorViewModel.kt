package com.safdev.calculator

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlin.text.compareTo

class CalculatorViewModel : ViewModel() {
    var state by mutableStateOf(CalculatorState())
        private set

    fun onAction(action: CalculationActions) {
        when (action) {
            is CalculationActions.Number -> enterNumber(action.number)
            is CalculationActions.Decimal -> enterDecimal()
            is CalculationActions.Clear -> {
                state = CalculatorState()
                Log.d("CalculatorVM", "State cleared")
            }
            is CalculationActions.Operation -> enterOperation(action.operation)
            is CalculationActions.Calculate -> performCalculation()
            is CalculationActions.Delete -> deleteLastCharacter()
        }
    }

    private fun deleteLastCharacter() {
        // If there was an error, clear acts like AC
        if (state.result == "Error") {
            state = CalculatorState()
            return
        }

        // If a result is shown, pressing delete should clear the result
        // and allow new input or clear the last part of the previous expression
        // For simplicity now, let's assume if result is present, delete clears it.
        // A more advanced delete would revert the last calculation step.
        if (state.result.isNotBlank()) {
            // Option 1: Clear everything and start fresh if delete is pressed after a result.
            // state = CalculatorState()

            // Option 2: Retain the result as the starting point for new input,
            // but delete clears it if no new operation has been entered yet.
            // This logic can get complex depending on desired UX.
            // For now, let's assume after a result, new input starts fresh.
            // Or, if you want the result to be the basis for the next calculation,
            // and delete removes the last digit of the *current input* after an operator.

            // Current simplified delete:
            // If currentInput is not blank, delete from it
            if (state.currentInput.isNotBlank()) {
                state = state.copy(currentInput = state.currentInput.dropLast(1))
            }
            // Else if operations are not empty, effectively remove the last operation
            // and make the last number in `numbers` the `currentInput`.
            else if (state.operations.isNotEmpty()) {
                val lastNumber = state.numbers.lastOrNull() ?: ""
                state = state.copy(
                    operations = state.operations.dropLast(1),
                    numbers = state.numbers.dropLast(1),
                    currentInput = lastNumber, // Restore last number to currentInput
                    result = "" // Clear result if we are editing the expression
                )
            }
            // Else if numbers has entries (but no operations yet, e.g. "123" then Del)
            // and currentInput became blank due to previous delete.
            else if (state.numbers.isNotEmpty() && state.currentInput.isBlank()){
                state = state.copy(
                    currentInput = state.numbers.last().dropLast(1),
                    numbers = state.numbers.dropLast(1),
                    result = ""
                )
            }
            // If only currentInput was present (e.g. initial number)
            // This is covered by the first 'if' generally.
            // Consider the case where result was displayed and now user hits delete.
            // If the goal is that result becomes the new number1, delete would clear it.
            // Let's refine based on the "result becomes number1" logic:
            if (state.result.isNotBlank() && state.currentInput.isBlank() && state.operations.isEmpty()) {
                state = state.copy(result = "") // Clear the result, effectively like AC
            }

            return
        }


        // Original delete logic if no result is displayed
        when {
            state.currentInput.isNotBlank() -> {
                state = state.copy(currentInput = state.currentInput.dropLast(1))
            }
            state.operations.isNotEmpty() -> { // Delete last operation
                val lastNumberBeforeOperation = state.numbers.lastOrNull() ?: ""
                state = state.copy(
                    operations = state.operations.dropLast(1),
                    // numbers list remains, currentInput becomes the number before the deleted op
                    currentInput = lastNumberBeforeOperation,
                    numbers = state.numbers.dropLast(1) // Remove the number that was before the op
                )
            }
            state.numbers.isNotEmpty() -> { // Delete from the last number if no operations
                state = state.copy(
                    currentInput = state.numbers.last().dropLast(1),
                    numbers = state.numbers.dropLast(1)
                )
            }
        }
        Log.d("CalculatorVM", "After Delete: Numbers: ${state.numbers}, Ops: ${state.operations.map { it.symbol }}, Current: ${state.currentInput}, Result: ${state.result}")
    }

//    private fun deleteLastCharacter() {
//        if (state.result == "Error") {
//            state = CalculatorState()
//            return
//        }
//
//        when {
//            state.currentInput.isNotBlank() -> {
//                state = state.copy(currentInput = state.currentInput.dropLast(1))
//            }
//            state.operations.isNotEmpty() && state.numbers.isNotEmpty() -> {
//                val newOperations = state.operations.dropLast(1)
//                val newNumbers = state.numbers.dropLast(1)
//                val lastNumber = state.numbers.lastOrNull() ?: ""
//                state = state.copy(
//                    operations = newOperations,
//                    numbers = newNumbers,
//                    currentInput = lastNumber
//                )
//            }
//        }
//    }

    private fun performCalculation() {
        val allNumbers = if (state.currentInput.isNotBlank()) {
            state.numbers + state.currentInput
        } else state.numbers.ifEmpty {
            // Nothing to calculate if no numbers and no current input
            // Or if only one number is present without operations
            if (state.numbers.size == 1 && state.operations.isEmpty() && state.currentInput.isBlank()) {
                state = state.copy(result = state.numbers.first()) // "5 =" should show "5"
                return
            }
            Log.d("CalculatorVM", "PerformCalc: Not enough numbers or operations.")
            return
        }

        // If there's only one number and no operations, the result is that number.
        if (allNumbers.size == 1 && state.operations.isEmpty()) {
            val res = allNumbers[0]
            state = state.copy(
                result = formatResult(res.toDoubleOrNull()), // Result is shown
                numbers = listOf(res), // This result becomes the new starting number
                operations = emptyList(),
                currentInput = ""      // Current input is cleared
            )
            Log.d("CalculatorVM", "PerformCalc (single number): ${state.result}")
            return
        }


        if (allNumbers.size <= state.operations.size) {
            Log.d("CalculatorVM", "PerformCalc: Not enough numbers for operations. N: ${allNumbers.size}, O: ${state.operations.size}")
            if (allNumbers.isNotEmpty()) { // e.g. "5+" then "="
                state = state.copy(
                    result = formatResult(allNumbers.last().toDoubleOrNull()),
                    numbers = listOf(allNumbers.last()),
                    operations = emptyList(),
                    currentInput = ""
                )
            }
            return // Not enough operands for all operations
        }


        var calculatedValue = allNumbers[0].toDoubleOrNull()
        if (calculatedValue == null) {
            state = state.copy(result = "Error")
            Log.d("CalculatorVM", "PerformCalc: First number is not valid.")
            return
        }

        for (i in state.operations.indices) {
            val nextNumStr = allNumbers.getOrNull(i + 1)
            val nextNum = nextNumStr?.toDoubleOrNull()

            if (nextNum == null) {
                // If we have an operation like "5 * =" (waiting for second operand)
                // Some calculators repeat the last operation with the same number "5 * 5"
                // Others show error or do nothing. For now, let's treat as error or incomplete.
                // Or, if we want "5 * =" to result in "5", then the logic above (allNumbers.size <= state.operations.size) handles it.
                // For a more robust approach, decide the behavior for "5 * =".
                // If it should be an error or just show 5.
                // The earlier check for "allNumbers.size <= state.operations.size" should catch "5*="
                // and just display "5" as the result.
                Log.d("CalculatorVM", "PerformCalc: Subsequent number is not valid or missing for operation ${state.operations[i].symbol}.")
                // state = state.copy(result = "Error") // Or handle as per desired UX
                return
            }

            calculatedValue = when (state.operations[i]) {
                is CalculatorOperation.Add -> calculatedValue?.plus(nextNum)
                is CalculatorOperation.Subtract -> calculatedValue?.minus(nextNum)
                is CalculatorOperation.Multiply -> calculatedValue?.times(nextNum)
                is CalculatorOperation.Divide -> {
                    if (nextNum != 0.0) calculatedValue?.div(nextNum) else Double.NaN
                }
            }
            if (calculatedValue == null || calculatedValue.isNaN()) {
                Log.d("CalculatorVM", "Breaking calculation due to null or NaN result.")
                break // Stop if error like division by zero or other calculation issue
            }
        }

        val resultStr = formatResult(calculatedValue)
        state = state.copy(
            result = resultStr,                         // Display the final result
            numbers = if (resultStr != "Error") listOf(resultStr) else emptyList(), // The result becomes the new starting number
            operations = emptyList(),                   // Clear operations
            currentInput = ""                           // Clear current input
        )
        Log.d("CalculatorVM", "PerformCalc: Result: ${state.result}, Next numbers: ${state.numbers}")
    }

    private fun formatResult(value: Double?): String {
        if (value == null || value.isNaN() || value.isInfinite()) {
            return "Error"
        }
        // Show as Int if it's a whole number, otherwise format as Double
        return if (value % 1.0 == 0.0) {
            value.toLong().toString() // Use Long for larger whole numbers
        } else {
            // Potentially format to a certain number of decimal places if needed
            value.toString()
        }
    }


    private fun enterOperation(operation: CalculatorOperation) {
        Log.d("CalculatorVM", "EnterOp: CurrentInput: '${state.currentInput}', Numbers: ${state.numbers}, Ops: ${state.operations.map{it.symbol}}, Result: '${state.result}'")

        // If a result is already shown, that result becomes the first number for the new operation.
        if (state.result.isNotBlank() && state.result != "Error") {
            state = state.copy(
                numbers = listOf(state.result), // Result becomes the first number
                operations = listOf(operation),   // Add the new operation
                currentInput = "",                // Clear current input for the next number
                result = ""                       // Clear the displayed result
            )
            Log.d("CalculatorVM", "EnterOp after result: N: ${state.numbers}, O: ${state.operations.map{it.symbol}}, CI: '', Res: ''")
            return
        }

        // If currentInput is not blank (user is typing a number)
        if (state.currentInput.isNotBlank()) {
            // If currentInput is just "-" and it's the start of a new number (not after an op that expects a number)
            if (state.currentInput == "-" && state.operations.size == state.numbers.size) {
                // Allow "-" to be part of currentInput, don't treat as operation yet
                Log.d("CalculatorVM", "EnterOp: currentInput is '-', waiting for number.")
                // state = state.copy(operations = state.operations + operation) // This would be wrong here
                // We actually want to *replace* the last op if user types "5 + -" then changes mind to "*"
                if (state.operations.isNotEmpty() && state.numbers.size == state.operations.size) {
                    state = state.copy(
                        operations = state.operations.dropLast(1) + operation,
                        currentInput = "" // Clear the "-" as we are adding a new operation
                    )
                    Log.d("CalculatorVM", "EnterOp: Replaced last op due to '-' then new op. Ops: ${state.operations.map{it.symbol}}")
                }
                return
            }

            state = state.copy(
                numbers = state.numbers + state.currentInput,
                operations = state.operations + operation,
                currentInput = ""
            )
            Log.d("CalculatorVM", "EnterOp with currentInput: N: ${state.numbers}, O: ${state.operations.map{it.symbol}}, CI: ''")
        }
        // If currentInput is blank, but there are numbers (e.g., "5" then "+")
        // And we want to change the operation (e.g. "5+" then user clicks "*" -> "5*")
        else if (state.numbers.isNotEmpty() && state.operations.isNotEmpty() && state.numbers.size == state.operations.size) {
            state = state.copy(
                operations = state.operations.dropLast(1) + operation // Replace last operation
            )
            Log.d("CalculatorVM", "EnterOp: Replaced last operation. Ops: ${state.operations.map{it.symbol}}")
        }
        // If starting with an operation (e.g. for a negative number, allow "-" if currentInput is empty and no numbers yet)
        else if (state.currentInput.isBlank() && state.numbers.isEmpty() && operation is CalculatorOperation.Subtract) {
            state = state.copy(currentInput = "-") // Start new number with "-"
            Log.d("CalculatorVM", "EnterOp: Started with '-' for negative number.")
        } else {
            Log.d("CalculatorVM", "EnterOp: No action taken. CI: '${state.currentInput}', N_size: ${state.numbers.size}, O_size: ${state.operations.size}")
        }
    }

    private fun enterDecimal() {
        Log.d("CalculatorVM", "EnterDecimal: CI: '${state.currentInput}', Result: '${state.result}'")
        if (state.result.isNotBlank()) { // If result is shown, decimal starts a new number "0."
            state = state.copy(
                numbers = listOf(state.result), // Keep result as n1
                operations = emptyList(),     // Clear ops
                currentInput = "0.",
                result = ""
            )
            Log.d("CalculatorVM", "EnterDecimal after result: CI: '0.', N: ${state.numbers}")
            return
        }

        if (!state.currentInput.contains(".")) {
            val newInput = if (state.currentInput.isBlank()) "0." else state.currentInput + "."
            state = state.copy(currentInput = newInput)
            Log.d("CalculatorVM", "EnterDecimal: New CI: '$newInput'")
        }
    }

    private fun enterNumber(number: Int) {
        Log.d("CalculatorVM", "EnterNumber $number: CI: '${state.currentInput}', Result: '${state.result}'")

        // If a result is shown (including "Error"), and the user types a number,
        // it means they want to start a new calculation.
        // The previous result/error is discarded.
        if (state.result.isNotBlank()) { // This condition correctly includes state.result == "Error"
            state = state.copy(
                currentInput = number.toString(),
                numbers = emptyList(),      // New calculation starts
                operations = emptyList(),
                result = ""                 // Clear the previous result/error
            )
            Log.d("CalculatorVM", "EnterNumber after result/error: New CI: '${state.currentInput}'")
            return
        }

        // If currentInput is already "0" and the user presses 0, do nothing further.
        if (state.currentInput == "0" && number == 0) {
            Log.d("CalculatorVM", "EnterNumber: CI is '0', pressed '0'. No change.")
            return
        }

        // If currentInput is "0" and the user presses a non-zero number, replace "0".
        if (state.currentInput == "0" && number != 0) {
            state = state.copy(currentInput = number.toString())
            Log.d("CalculatorVM", "EnterNumber: CI was '0', replaced with '$number'. New CI: '${state.currentInput}'")
            return
        }

        // Standard case: append the number, but check max length first.
        if (state.currentInput.length >= MAX_NUMBER_LENGTH) {
            Log.d("CalculatorVM", "EnterNumber: Max length $MAX_NUMBER_LENGTH reached.")
            return
        }

        state = state.copy(currentInput = state.currentInput + number)
        Log.d("CalculatorVM", "EnterNumber: Appended '$number'. New CI: '${state.currentInput}'")
    }


    companion object {
        private const val MAX_NUMBER_LENGTH = 12
    }
}

