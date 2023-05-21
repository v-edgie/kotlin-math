import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MatrixTest {
    private val m: Matrix = Matrix(3,3)


    @Test
    fun testDefaultConstructor() {
        val a = Matrix(1,2) // no value for cells
        assertEquals(1, a.getRowDimension())
        assertEquals(2, a.getColumnDimension())
        assertEquals(a.m, a.getRowDimension())
        assertEquals(a.n, a.getColumnDimension())
        // Kotlin fills arrays with zeroes:
        for (i in 0 until 1) {
            for (j in 0 until 2) {
                assertEquals(0.0, a.getA()[0][0])
            }
        }
    }

    /*
        Test generated functions of data classes.
     */

    @Test
    fun testGetMN() {
        val a = Matrix(1,2, 13.0)
        assertEquals(1, a.m)
        assertEquals(2, a.n)
        // no a.values because values is a private property
        // no assigning e.g. a.m = 4 because m and n are val, not var.
        for (i in 0 until 1) {
            for (j in 0 until 2) {
                assertEquals(13.0, a.get(i,j))
            }
        }
    }

    @Test
    fun testConstructWithValue() {
        val a = Matrix(1,2, 13.0)
        assertEquals(1, a.getRowDimension())
        assertEquals(2, a.getColumnDimension())
        assertEquals(a.m, a.getRowDimension())
        assertEquals(a.n, a.getColumnDimension())
        for (i in 0 until 1) {
            for (j in 0 until 2) {
                assertEquals(13.0, a.get(i,j))
            }
        }
    }

    @Test
    fun testConstructWithZeroRowDimension() {
        val a = Matrix(0, 2, 13.0)
        assertEquals(0, a.getRowDimension())
        assertEquals(2, a.getColumnDimension())
        assertEquals(a.m, a.getRowDimension())
        assertEquals(a.n, a.getColumnDimension())
        for (i in 0 until 0) {
            for (j in 0 until 2) {
                assertEquals(13.0, a.get(i, j))
            }
        }
    }
    @Test
    fun testConstructWithZeroColumnDimension() {
        val a = Matrix(3,0, 12.0)
        assertEquals(3, a.getRowDimension())
        assertEquals(0, a.getColumnDimension())
        assertEquals(a.m, a.getRowDimension())
        assertEquals(a.n, a.getColumnDimension())
        for (i in 0 until 3) {
            for (j in 0 until 0) {
                assertEquals(12.0, a.get(i,j))
            }
        }
    }
    @Test
    fun testConstructWithNegativeRowDimension() {
        Assertions.assertThrows(NegativeArraySizeException::class.java) {
            val a = Matrix(-2, 2, 13.0)
        }
    }
    @Test
    fun testConstructWithNegativeColumnDimension() {
        Assertions.assertThrows(NegativeArraySizeException::class.java) {
            val a = Matrix(2, -2)
        }
    }
    @Test
    fun testConstructWithPackedRow() {
        val x: DoubleArray = DoubleArray(2) {0.0; 1.0}
        val a = Matrix(x, 2)
        assertEquals(2, a.m)
        assertEquals(1, a.n)
        val b = Matrix(x, 1)
        assertEquals(1, b.m)
        assertEquals(2, b.n)
    }
    @Test
    fun testConstructFromExistingMatrix() {
        val a = Matrix(2,2,22.0)
        val b = Matrix(a)
        for (i in 0 until a.m) {
            for (j in 0 until a.n) {
                assertEquals(a.getA()[i][j], b.get(i,j))
            }
        }
    }
    @Test
    fun testGetSubMatrix() {
        val a = Matrix(3,3)
        a.getA()[0] = DoubleArray(3) { 0.0; 1.0; 2.0}
        a.getA()[1] = DoubleArray(3) { 0.1; 1.1; 2.1}
        a.getA()[2] = DoubleArray(3) { 0.2; 1.2; 2.2}
        a.getA()[3] = DoubleArray(3) { 0.3; 1.3; 2.3}
        val b = a.getMatrix(0,1,2,3)
        assertEquals(b.getA()[])
    }
//    @Test
//    fun testSet() {
//        m.setCell(0,0,0.0)
//        m.setCell(1,0,1.0)
//        m.setCell(2,0,2.0)
//
//        m.setCell(0,1,0.1)
//        m.setCell(1,1,1.1)
//        m.setCell(2,1,2.1)
//
//        m.setCell(0,2,0.2)
//        m.setCell(1,2,1.2)
//        m.setCell(2,2,2.2)
//
//        val expected = 1.2
//        assertEquals(expected, m.getCell(1,2))
//    }
//
//    @Test
//    fun testGetA() {
//        assertEquals(0.0, m.getA())
//    }
}