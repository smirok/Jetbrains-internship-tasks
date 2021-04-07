package multiplier

import Matrix

private fun naiveInnerMultiplication(
    firstMatrix: Matrix,
    topLeftFirst: Pair<Int, Int>,
    secondMatrix: Matrix,
    topLeftSecond: Pair<Int, Int>,
    result: Matrix,
    topLeftResult: Pair<Int, Int>,
    size: Int
) {
    for (i in 0 until size) {
        for (k in 0 until size) {
            for (j in 0 until size) {
                result[i + topLeftResult.first, j + topLeftResult.second] =
                    result[i + topLeftResult.first, j + topLeftResult.second].toFloat() +
                            (firstMatrix[i + topLeftFirst.first, k + topLeftFirst.second].toFloat() *
                                    secondMatrix[k + topLeftSecond.first, j + topLeftSecond.second].toFloat()) // avoiding cache misses
            }
        }
    }
    return
}

private fun closestTwoDegree(num: Int): Int {
    var degree = 0
    while (1 shl degree < num) {
        degree++
    }
    return (1 shl degree)
}

private fun naiveDivideAndConquerRecursive(
    firstMatrix: Matrix,
    topLeftFirst: Pair<Int, Int>,
    secondMatrix: Matrix,
    topLeftSecond: Pair<Int, Int>,
    result: Matrix,
    topLeftResult: Pair<Int, Int>,
    size: Int
) {

    if (size <= 32) {
        naiveInnerMultiplication(
            firstMatrix, topLeftFirst,
            secondMatrix, topLeftSecond,
            result, topLeftResult, size * 2
        )
        return
    }

    naiveDivideAndConquerRecursive(
        firstMatrix, topLeftFirst,
        secondMatrix, topLeftSecond,
        result, topLeftResult, size / 2
    )

    naiveDivideAndConquerRecursive(
        firstMatrix, Pair(topLeftFirst.first, topLeftFirst.second + size),
        secondMatrix, Pair(topLeftSecond.first + size, topLeftSecond.second),
        result, topLeftResult, size / 2
    )

    naiveDivideAndConquerRecursive(
        firstMatrix, topLeftFirst,
        secondMatrix, Pair(topLeftSecond.first, topLeftSecond.second + size),
        result, Pair(topLeftResult.first, topLeftResult.second + size), size / 2
    )

    naiveDivideAndConquerRecursive(
        firstMatrix, Pair(topLeftFirst.first, topLeftFirst.second + size),
        secondMatrix, Pair(topLeftSecond.first + size, topLeftSecond.second + size),
        result, Pair(topLeftResult.first, topLeftResult.second + size), size / 2
    )

    naiveDivideAndConquerRecursive(
        firstMatrix, Pair(topLeftFirst.first + size, topLeftFirst.second),
        secondMatrix, topLeftSecond,
        result, Pair(topLeftResult.first + size, topLeftResult.second), size / 2
    )

    naiveDivideAndConquerRecursive(
        firstMatrix, Pair(topLeftFirst.first + size, topLeftFirst.second + size),
        secondMatrix, Pair(topLeftSecond.first + size, topLeftSecond.second),
        result, Pair(topLeftResult.first + size, topLeftResult.second), size / 2
    )

    naiveDivideAndConquerRecursive(
        firstMatrix, Pair(topLeftFirst.first + size, topLeftFirst.second),
        secondMatrix, Pair(topLeftSecond.first, topLeftSecond.second + size),
        result, Pair(topLeftResult.first + size, topLeftResult.second + size), size / 2
    )

    naiveDivideAndConquerRecursive(
        firstMatrix, Pair(topLeftFirst.first + size, topLeftFirst.second + size),
        secondMatrix, Pair(topLeftSecond.first + size, topLeftSecond.second + size),
        result, Pair(topLeftResult.first + size, topLeftResult.second + size), size / 2
    )
}

private fun shtrassenVinogradInnerMultiplication(firstMatrix: Matrix, secondMatrix: Matrix): Matrix {
    if (firstMatrix.getRows() <= 32) {
        return naiveMultiplication(firstMatrix, secondMatrix)
    }

    val size = firstMatrix.getRows() / 2

    val a11 = firstMatrix.getSubMatrix(Pair(0, 0), size, size)
    val a12 = firstMatrix.getSubMatrix(Pair(0, size), size, size)
    val a21 = firstMatrix.getSubMatrix(Pair(size, 0), size, size)
    val a22 = firstMatrix.getSubMatrix(Pair(size, size), size, size)

    val b11 = secondMatrix.getSubMatrix(Pair(0, 0), size, size)
    val b12 = secondMatrix.getSubMatrix(Pair(0, size), size, size)
    val b21 = secondMatrix.getSubMatrix(Pair(size, 0), size, size)
    val b22 = secondMatrix.getSubMatrix(Pair(size, size), size, size)

    val s1 = a21 + a22
    val s2 = s1 - a11
    val s3 = a11 - a21
    val s4 = a12 - s2
    val s5 = b12 - b11
    val s6 = b22 - s5
    val s7 = b22 - b12
    val s8 = s6 - b21

    val p1 = s2 * s6
    val p2 = a11 * b11
    val p3 = a12 * b21
    val p4 = s3 * s7
    val p5 = s1 * s5
    val p6 = s4 * b22
    val p7 = a22 * s8

    val t1 = p1 + p2
    val t2 = t1 + p4

    val c11 = p2 + p3
    val c12 = t1 + p5 + p6
    val c21 = t2 - p7
    val c22 = t2 + p5

    val result = Matrix(Array(size * 2) { Array(size * 2) { 0 } })
    result.makeMatrix(c11, c12, c21, c22, size)
    return result
}

fun naiveMultiplication(firstMatrix: Matrix, secondMatrix: Matrix): Matrix {
    if (firstMatrix.isEmpty() || secondMatrix.isEmpty() || firstMatrix.getColumns() != secondMatrix.getRows()) {
        throw IllegalArgumentException("Wrong dimensions")
    }

    val result = Matrix(Array(firstMatrix.getRows()) { Array(secondMatrix.getColumns()) { 0 } })

    for (i in 0 until firstMatrix.getRows()) {
        for (k in 0 until secondMatrix.getRows()) {
            for (j in 0 until secondMatrix.getColumns()) {
                result[i, j] =
                    result[i, j].toFloat() + (firstMatrix[i, k].toFloat() * secondMatrix[k, j].toFloat()) // avoiding cache misses
            }
        }
    }

    return result
}

fun naiveDivideAndConquer(firstMatrix: Matrix, secondMatrix: Matrix): Matrix {
    if (firstMatrix.isEmpty() || secondMatrix.isEmpty() || firstMatrix.getColumns() != secondMatrix.getRows()) {
        throw IllegalArgumentException("Wrong dimensions")
    }

    val sideLength = closestTwoDegree(
        maxOf(
            firstMatrix.getColumns(),
            firstMatrix.getRows(),
            secondMatrix.getColumns(),
            secondMatrix.getColumns()
        )
    )

    val matrix = Matrix(Array(firstMatrix.getRows()) { Array(secondMatrix.getColumns()) { 0 } })
    naiveDivideAndConquerRecursive(
        firstMatrix, Pair(0, 0),
        secondMatrix, Pair(0, 0),
        matrix, Pair(0, 0), sideLength / 2
    )

    return matrix
}

fun shtrassenVinogradMultiplication(firstMatrix: Matrix, secondMatrix: Matrix): Matrix {
    if (firstMatrix.isEmpty() || secondMatrix.isEmpty() || firstMatrix.getColumns() != secondMatrix.getRows()) {
        throw IllegalArgumentException("Wrong dimensions")
    }

    val sideLength = closestTwoDegree(
        maxOf(
            firstMatrix.getColumns(),
            firstMatrix.getRows(),
            secondMatrix.getColumns(),
            secondMatrix.getColumns()
        )
    )

    if (sideLength == firstMatrix.getRows() && sideLength == firstMatrix.getColumns() &&
        sideLength == secondMatrix.getRows() && sideLength == secondMatrix.getColumns()
    ) {
        return shtrassenVinogradInnerMultiplication(firstMatrix, secondMatrix)
    }

    val squareFirstMatrix = Matrix(Array(sideLength) { Array(sideLength) { 0 } })
    squareFirstMatrix.setSubMatrix(Pair(0, 0), firstMatrix)
    val squareSecondMatrix = Matrix(Array(sideLength) { Array(sideLength) { 0 } })
    squareSecondMatrix.setSubMatrix(Pair(0, 0), secondMatrix)

    return shtrassenVinogradInnerMultiplication(squareFirstMatrix, squareSecondMatrix)
        .getSubMatrix(Pair(0, 0), firstMatrix.getRows(), secondMatrix.getColumns())
}