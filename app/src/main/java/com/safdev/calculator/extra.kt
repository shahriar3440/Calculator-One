package com.safdev.calculator
//sealed class CalculatorOperation(val symbol: String) {
//    object Add : CalculatorOperation("+")
//    object Subtract : CalculatorOperation("-")
//    object Multiply : CalculatorOperation("×")
//    object Divide : CalculatorOperation("/")
//}



//    private fun performCalculation() {
//        val number1 = state.number1.toDoubleOrNull()
//        val number2 = state.number2.toDoubleOrNull()
//        if (number1 != null && number2 != null) {
//            val result = when (state.operation) {
//                is CalculatorOperation.Add -> number1 + number2
//                is CalculatorOperation.Subtract -> number1 - number2
//                is CalculatorOperation.Multiply -> number1 * number2
//                is CalculatorOperation.Divide -> if (number2 != 0.0) {
//                    number1 / number2
//                } else {
//                    Double.NaN // Handle division by zero
//                }
//
//                null -> return
//            }
//
//            val resultString = if (result.isNaN()) {
//                "Error"
//            } else if (result % 1.0 == 0.0) {
//                result.toInt().toString()
//            } else {
//                result.toString()
//            }
//
//            state = state.copy(
//                result = resultString,
//                number1 = resultString,
//                number2 = "",
//                operation = null,
//                isCalculationInProgress = true,
//                errorMessage = if (result.isNaN()) "Cannot divide by zero" else null
//            )
//        }
//    }




//    private fun deleteLastCharacter() {
//        when {
//            state.number2.isNotBlank() -> state = state.copy(
//                number2 = state.number2.dropLast(1)
//            )
//
//            state.operation != null -> state = state.copy(
//                operation = null,
//                number2 = "",
//                result = "",
//                isCalculationInProgress = false,
//                errorMessage = null
//            )
//
//            state.number1.isNotBlank() -> state = state.copy(
//                number1 = state.number1.dropLast(1)
//            )
//        }
//        if (state.result == "Error") {
//            state = CalculatorState()
//            return
//        }
//    }




//    private fun enterOperation(operation: CalculatorOperation) {
//        if (state.number1.isNotBlank()) {
//            state = state.copy(
//                numbers = state.numbers + state.currentInput,
//                operations = state.operations + operation,
//                currentInput = ""
//            )
//        }
//    }

//    private fun enterDecimal() {
//
//        if (state.operation == null) {
//            if (!state.number1.contains(".")) {
//                val newNumber1 = if (state.number1.isBlank()) "0." else state.number1 + "."
//                state = state.copy(number1 = newNumber1)
//            }
//        } else {
//            if (!state.number2.contains(".")) {
//                val newNumber2 = if (state.number2.isBlank()) "0." else state.number2 + "."
//                state = state.copy(number2 = newNumber2)
//            }
//        }
//
//        if (state.operation == null) {
//            if (!state.number1.contains(".")) {
//                val newNumber1 = if (state.number1.isBlank()) "0." else state.number1 + "."
//                state = state.copy(number1 = newNumber1)
//            }
//            return
//        }
//
//        if (!state.number2.contains(".") && !state.number2.isBlank()) {
//            state = state.copy(
//                number2 = state.number2 + ".",
//            )
//        }
//
//        if (state.operation == null) {
//            if (!state.number1.contains(".")) {
//                val newNumber1 = if (state.number1.isBlank()) "0." else state.number1 + "."
//                state = state.copy(number1 = newNumber1)
//            }
//            return
//        }
//
//        if (!state.number2.contains(".")) {
//            if (state.number2.isBlank()) {
//                state = state.copy(number2 = "0.")
//            } else {
//                state = state.copy(number2 = state.number2 + ".")
//            }
//        }
//    }



//    private fun enterNumber(number: Int) {
//        if (state.number1 == "Error") {
//            state = state.copy(number1 = number.toString(), result = "", operation = null, number2 = "")
//            return
//        }
//        if (state.currentInput.length >= MAX_NUMBER_LENGTH) return
//        val newInput = state.currentInput + number
//        state = state.copy(currentInput = newInput)
//
//        if (state.operation == null) {
//            if (state.number1.length >= MAX_NUMBER_LENGTH) return
//            if (number == 0) {
//                if (state.number1.isBlank()) {
//                    state = state.copy(number1 = "0")
//                    return
//                }
//                // Prevent multiple leading zeros
//                if (state.number1 == "0") return
//            }
//            // Replace "0" with the new digit
//            val newNumber1 = if (state.number1 == "0") number.toString() else state.number1 + number
//            state = state.copy(number1 = newNumber1)
//            return
//
//            // Append digit normally
//            state = state.copy(number1 = state.number1 + number)
//            return
//        }
//        if (state.number2.length >= MAX_NUMBER_LENGTH) return
//        if (number == 0) {
//            if (state.number2.isBlank()) {
//                state = state.copy(number2 = "0")
//                return
//            }
//            if (state.number2 == "0") return
//        }
//        state = state.copy(number2 = state.number2 + number)
//    }





//2nd time

//    private fun performCalculation() {
//        val allNumbers = if (state.currentInput.isNotBlank())
//            state.numbers + state.currentInput
//        else
//            state.numbers
//
//        if (allNumbers.size < 2 || state.operations.isEmpty()) return
//
//        var result = allNumbers[0].toDoubleOrNull() ?: return
//        for (i in state.operations.indices) {
//            val next = allNumbers.getOrNull(i + 1)?.toDoubleOrNull() ?: continue
//            result = when (state.operations[i]) {
//                is CalculatorOperation.Add -> result + next
//                is CalculatorOperation.Subtract -> result - next
//                is CalculatorOperation.Multiply -> result * next
//                is CalculatorOperation.Divide -> if (next != 0.0) result / next else Double.NaN
//            }
//        }
//        state = state.copy(
//            result = if (result.isNaN()) "Error" else result.toString(),
//            numbers = emptyList(),
//            operations = emptyList(),
//            currentInput = ""
//        )
//    }
//
//
//    private fun enterOperation(operation: CalculatorOperation) {
//        Log.d("CalculatorVM", "enterOperation called with: ${operation.symbol}") // Log the symbol
//        if (state.currentInput.isNotBlank() || state.numbers.isNotEmpty()) { // Allow operation if there's a current number or previous numbers
//            val newNumbers = if (state.currentInput.isNotBlank()) state.numbers + state.currentInput else state.numbers
//            if (newNumbers.isNotEmpty()) { // Only add operation if there's a number to operate on
//                state = state.copy(
//                    numbers = newNumbers,
//                    operations = state.operations + operation, // Make sure this line is executed
//                    currentInput = ""
//                )
//                Log.d("CalculatorVM", "New state operations: ${state.operations.joinToString { it.symbol }}")
//            } else {
//                Log.d("CalculatorVM", "No numbers to operate on, operation not added.")
//            }
//        } else {
//            Log.d("CalculatorVM", "currentInput is blank and no previous numbers, operation not added.")
//        }
//    }
//
//
//    private fun enterDecimal() {
//        if (!state.currentInput.contains(".")) {
//            val newInput = if (state.currentInput.isBlank()) "0." else state.currentInput + "."
//            state = state.copy(currentInput = newInput)
//        }
//    }
//
//    private fun enterNumber(number: Int) {
//        if (state.currentInput.length >= MAX_NUMBER_LENGTH) return
//        // Prevent multiple leading zeros
//        if (state.currentInput == "0" && number == 0) return
//        if (state.currentInput == "0" && number != 0) {
//            state = state.copy(currentInput = number.toString())
//            return
//        }
//        if (state.currentInput.isBlank() && number == 0) {
//            state = state.copy(currentInput = "0")
//            return
//        }
//        state = state.copy(currentInput = state.currentInput + number)
//    }



