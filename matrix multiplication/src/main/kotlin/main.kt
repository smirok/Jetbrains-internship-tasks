fun multiplyNaive(
    firstArray: Array<out Array<Number>>,
    secondArray: Array<out Array<Number>>
): Array<out Array<Number>> {
    return multiplier.naiveMultiplication(Matrix(firstArray), Matrix(secondArray)).getArray()
}

fun multiplyDivideAndConquer(
    firstArray: Array<out Array<Number>>,
    secondArray: Array<out Array<Number>>
): Array<out Array<Number>> {
    return multiplier.naiveDivideAndConquer(Matrix(firstArray), Matrix(secondArray)).getArray()
}

fun multiplyShtrassenVinograd(
    firstArray: Array<out Array<Number>>,
    secondArray: Array<out Array<Number>>
): Array<out Array<Number>> {
    return multiplier.shtrassenVinogradMultiplication(Matrix(firstArray), Matrix(secondArray)).getArray()
}
