import org.junit.jupiter.api.*;
import kotlin.random.Random

class MultiplicationTest {
    @Test
    fun testBasics() {
        val leftMatrix = Matrix(arrayOf(arrayOf(1, 2, 3), arrayOf(5, 4, 3)))
        val rightMatrix = Matrix(arrayOf(arrayOf(1, 2), arrayOf(3, 8), arrayOf(2, 1)))

        val resultMatrixNaive = multiplier.naiveMultiplication(leftMatrix, rightMatrix)
        Assertions.assertEquals(resultMatrixNaive[0, 0], 13.0f)
        Assertions.assertEquals(resultMatrixNaive[0, 1], 21.0f)
        Assertions.assertEquals(resultMatrixNaive[1, 0], 23.0f)
        Assertions.assertEquals(resultMatrixNaive[1, 1], 45.0f)

        val resultMatrixDC = multiplier.naiveDivideAndConquer(leftMatrix, rightMatrix)
        Assertions.assertEquals(resultMatrixDC[0, 0], 13.0f)
        Assertions.assertEquals(resultMatrixDC[0, 1], 21.0f)
        Assertions.assertEquals(resultMatrixDC[1, 0], 23.0f)
        Assertions.assertEquals(resultMatrixDC[1, 1], 45.0f)

        val resultMatrixSV = multiplier.shtrassenVinogradMultiplication(leftMatrix, rightMatrix)
        Assertions.assertEquals(resultMatrixSV[0, 0], 13.0f)
        Assertions.assertEquals(resultMatrixSV[0, 1], 21.0f)
        Assertions.assertEquals(resultMatrixSV[1, 0], 23.0f)
        Assertions.assertEquals(resultMatrixSV[1, 1], 45.0f)
    }

    @Test
    fun testRandom() {
        val random = Random(2)

        val leftArray: Array<Array<Number>> = Array(100) { Array(100) { 0 } }
        val rightArray: Array<Array<Number>> = Array(100) { Array(100) { 0 } }

        for (i in 0 until 100) {
            for (j in 0 until 100) {
                leftArray[i][j] = random.nextInt() % 5
                rightArray[i][j] = random.nextInt() % 5
            }
        }

        val resultMatrixNaive = multiplyNaive(leftArray, rightArray)
        val resultMatrixDC = multiplyDivideAndConquer(leftArray, rightArray)
        val resultMatrixSV = multiplyShtrassenVinograd(leftArray, rightArray)

        for (i in 0 until 100) {
            for (j in 0 until 100) {
                Assertions.assertEquals(resultMatrixNaive[i][j], resultMatrixDC[i][j])
                Assertions.assertEquals(resultMatrixNaive[i][j], resultMatrixSV[i][j])
            }
        }
    }
}
