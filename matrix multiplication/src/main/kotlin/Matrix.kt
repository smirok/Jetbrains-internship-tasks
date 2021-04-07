import multiplier.shtrassenVinogradMultiplication

class Matrix(private var array: Array<out Array<Number>>) {
    private var rows: Int = array.size
    private var columns: Int = if (array.isEmpty()) 0 else array[0].size

    operator fun get(i: Int, j: Int): Number {
        return if (i in 0 until rows && j in 0 until columns) {
            array[i][j]
        } else {
            0
        }
    }

    operator fun set(i: Int, j: Int, value: Number) {
        if (i in 0 until rows && j in 0 until columns) {
            array[i][j] = value
        }
    }

    fun isEmpty(): Boolean {
        return array.isEmpty()
    }

    fun getRows(): Int {
        return rows
    }

    fun getColumns(): Int {
        return columns
    }

    fun getArray(): Array<out Array<Number>> {
        return array
    }

    fun getSubMatrix(topLeft: Pair<Int, Int>, rows: Int, columns: Int): Matrix {
        val matrix = Matrix(Array(rows) { Array(columns) { 0 } })
        for (i in 0 until getRows()) {
            for (j in 0 until getColumns()) {
                matrix[i, j] = this[i + topLeft.first, j + topLeft.second]
            }
        }
        return matrix
    }

    fun setSubMatrix(topLeft: Pair<Int, Int>, matrix: Matrix) {
        for (i in 0 until matrix.getRows()) {
            for (j in 0 until matrix.getColumns()) {
                this[i + topLeft.first, j + topLeft.second] = matrix[i, j]
            }
        }
    }

    fun makeMatrix(
        topLeftMatrix: Matrix,
        topRightMatrix: Matrix,
        lowerLeftMatrix: Matrix,
        lowerRightMatrix: Matrix,
        size: Int
    ) {
        setSubMatrix(Pair(0, 0), topLeftMatrix)
        setSubMatrix(Pair(0, size), topRightMatrix)
        setSubMatrix(Pair(size, 0), lowerLeftMatrix)
        setSubMatrix(Pair(size, size), lowerRightMatrix)
    }

    operator fun plus(m: Matrix): Matrix {
        val result = Matrix(Array(getRows()) { Array(getColumns()) { 0 } })

        for (i in 0 until m.getRows()) {
            for (j in 0 until m.getColumns()) {
                result[i, j] = this[i, j].toFloat() + m[i, j].toFloat()
            }
        }

        return result
    }

    operator fun minus(m: Matrix): Matrix {
        val result = Matrix(Array(getRows()) { Array(getColumns()) { 0 } })

        for (i in 0 until m.getRows()) {
            for (j in 0 until m.getColumns()) {
                result[i, j] = this[i, j].toFloat() - m[i, j].toFloat()
            }
        }

        return result
    }

    operator fun times(m: Matrix): Matrix {
        return shtrassenVinogradMultiplication(this, m)
    }
}
