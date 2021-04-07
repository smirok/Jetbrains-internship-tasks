import kotlin.math.abs
import kotlin.random.Random

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

fun main(args: Array<String>) {
    var time = System.nanoTime()

    val A: Array<Array<Number>> = Array(1000) { Array(1000) { 0 } }
    val B: Array<Array<Number>> = Array(1000) { Array(1000) { 0 } }

    var random = Random(3);
    for (i in A.indices)
        for (j in A[0].indices) {
            A[i][j] = (random.nextInt() % 2)
            B[i][j] = (random.nextInt() % 2)
        }

    val C = multiplyNaive(A, B)

    time = System.nanoTime() - time
    System.out.printf("Elapsed %,9.3f ms\n", time / 1000000.0)

    time = System.nanoTime()
    val D = multiplyShtrassenVinograd(A, B)

    time = System.nanoTime() - time
    System.out.printf("Elapsed %,9.3f ms\n", time / 1000000.0)

    var cnt = 0
    for (i in C.indices) {
        for (j in C[0].indices) {
            if (abs(C[i][j].toFloat() - D[i][j].toFloat()) > 1e-1) {
                cnt++
            }
        }
    }
    print(cnt)
}
