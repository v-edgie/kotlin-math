/**
    This is a Kotlin implementation of the JAMA Matrix class written in Java.

    <h2>In Kotlin </h2>
    Classes, constructors and members are public by default.
    <P>
    The args m and n are properties.
    By using val for m and n we prevent them from being modified.
    Therefore we can keep them public to automatically generate getters
    without being concerned about being modified.
    <P>


 */
data class Matrix(val m: Int, val n: Int,) {
    /*
        A Property derived from class properties.
        Unlike the Java implementation we do not need to explicitly define
        class variables to hold m and n.
        Kotlin has native arrays such as IntArray and DoubleArray but only
        1-dimensional. To make a 2-D array create an Array of DoubleArray
        elements. DoubleArray Values are not boxed and we can use syntax like
        A[1][2] to access elements.
     */
    private val A = Array(m) { DoubleArray(n) }

    /* ------------------------
       Constructors
     * ------------------------ */

    /* 1st constructor is automatic by using (m: Int, n: Int) in the class
       statement along with the A property.
       The Java implementation leaves the array elements null, whereas
       Kotlin default array values are zero because the way A is defined the
       contents cannot be null. We could probably make the values nullable but
       it seems safer to not allow null values. In Java null values are not
       preventable without explicitly filling with zeroes or other values at
       construction time.
    */

    /**
     * Construct an array with each cell set to the same value.
     * Note that this(m,n) calls the class constructor to initialize the m,n
     * properties and create the A array.
     */
    constructor(m: Int, n: Int, value: Double) : this(m, n) {
        for (element in A) {
            element.fill(value)
        }
    }

    /*
     * Construct a matrix from a 2-D array (double[][]).
     * The arg should be double[][] A, not a Matrix. Can this be done in Kotlin?
     * The Java implementation assigns A to the provided double[][] along with
     * m and n from length of A and length of A[0], which assumes A is not null
     * and has length > 0.
     * In Kotlin A is created in the 1st constructor and cannot be re-assigned,
     * so the mechanism in the Java code cannot be replicated in Kotlin. This
     * is probably a good thing, because the effect is the same as let A = B
     * with a check that all the arrays in B are the same length. I.E. there is
     * no copying of the arrays or elements, just assignment of the provided
     * array to a new array. The Java implementation therefore is unnecessary
     * and obfuscates a simple memory assignment.
     */
//    constructor(B: double[][]) : this(B.size, B[0].size) {
//        this.A = B // Kotlin: A cannot be re-assigned
//    }

    /*
        Construct a matrix quickly without checking arguments.
        Similar to the previous constructor Kotlin cannot re-assign A to B.
     */
//     constructor(B: double[][]) : this() {
//         A = B
//     }

    /**
        Construct a matrix from a one-dimensional packed array.
        Checks:
        <OL>
            <LI> vals length could be zero: OK
            <LI> m could be zero: NO! because we divide by m to get n.
            <LI> vals cannot be null: OK (java does not check this, Kotlin enforces it)
        </OL>
     */
    constructor(vals: DoubleArray, m: Int) : this(m, vals.size/m) {
        // no ternary in Kotlin, use when expression instead:
        var cols: Int = when (m) {
            0 -> 0
            else -> vals.size/m
        }
        if (m * cols != vals.size)
            throw IllegalArgumentException("Array length must be a multiple of m")
        for (i in A.indices) {
            for (j in 0 until A[i].size) {
                A[i][j] = vals[i+j*m]
            }
        }

    }
    /**
     * Construct a matrix from a copy of a 2-D array.
     * This is named constructWithCopy in the Java implementation.
      */
    constructor(B: Matrix) : this(B.m, B.n) { // make A same dimensions as B
        // Fill A with B values, one cell at a time:
        for (i in A.indices) {
            for (j in 0 until A[i].size) {
                A[i][j] = B.get(i, j)
            }
        }
    }

    /**
     *  Make a deep copy of this matrix
     */
    fun copy(): Matrix {
        return Matrix(this)  // call the constructor that takes a Matrix
    }

    /** Clone the Matrix object, which just calls copy */
    fun clone(): Matrix {
        return this.copy()
    }

    /* ------------------------
       Public Methods
     * ------------------------ */

    /** get the internal A array */
    fun getA(): Array<DoubleArray> {
        return A
    }

    /**
     * Get a copy of the A array.
     * Since we cannot make double[][] this is the same as copy() and clone().
     */
    fun getArrayCopy(): Matrix {
        return this.copy()
    }

    /**
     * Make a one-dimensional column-packed copy of the internal array
      */
    fun getColumnPackedCopy(): DoubleArray {
        val vals: DoubleArray = DoubleArray(m * n)
        for (i in A.indices) {
            for (j in 0 until A[i].size) {
                vals[i+j*m] = A[i][j]
            }
        }
        return vals
    }
    /**
     *  Make a one-dimensional row-packed copy of the internal array.
     */
    fun getRowPackedCopy(): DoubleArray {
        val vals: DoubleArray = DoubleArray(m * n)
        for (i in A.indices) {
            for (j in 0 until A[i].size) {
                vals[i*n+j] = A[i][j]
            }
        }
        return vals
    }
    /**  get row dimension */
    fun getRowDimension(): Int {
        return m
    }
    /** get column dimension */
    fun getColumnDimension(): Int {
        return n
    }
    // TODO get col dimension
    // TODO get a single element

    /** Get a single element
     * @param row   row index
     * @param col   column index
     * @return  A(row, col)
     * @exception ArrayIndexOutOfBoundsException
     */
    fun get(row: Int, col: Int) : Double {
        return A[row][col]
    }

    /**
     * Get a sub-matrix
     * @param i0 Initial row index
     * @param i1 Final row index
     * @param j0 Initial column index
     * @param j1 Final column index
     */
    fun getMatrix(i0: Int, i1: Int, j0: Int, j1: Int): Matrix {
        val B = Matrix(i1 - i0 + 1, j1 - j0 + 1)
        for (i in i0 until i1) {
            for (j in j0 until j1) {
                B.set(i-i0,j-j0, A[i][j])
            }
        }
        return B
    }
    // TODO Get a submatrix from arrays of row and column indexes


    /** Set a single element
     * @param row   row index
     * @param col   column index
     * @param value value for A(row,col)
     * @exception ArrayIndexOutOfBoundsException
     */
    fun set(row: Int, col: Int, value: Double) {
        A[row][col] = value
    }

    /**
     * Set a submatrix.
     *
     * Java implementation assumes row1>=row0 and col1>=col0 in the
     * for(..) loop. Kotlin appears to not care, the i and j integers
     * could increase or decrease between row0 and row1, and likewise
     * between col0 and col1.
     *
     * @param row0 initial row index
     * @param row1 final row index
     * @param col0 initial column index
     * @param col1 final column index
     * @param X    values for A(row0:row1,col0:col1)
     * @exception ArrayIndexOutOfBoundsException
     */
    fun setMatrix(i0: Int, i1: Int, j0: Int, j1: Int, X: Matrix) {
        // TODO check row1>=row0 ? and col1>=col0?
        for (i in i0 until i1) {
            for (j in j0 until j1) {
                A[i][j] = X.get(i-i0, j-j0)
            }
        }
    }
    // TODO set a submatrix from an array of row indices and an array of col indices
    // TODO set a submatrix from an array of row indices and an initial and final column index
    // TODO set a submatrix from initial and final row indices and an array of column indices

    /**
     * transpose A
     */
    fun transpose(): Matrix {
        val X: Matrix = Matrix(n,m)
        for (i in 0 until m) {
            for (j in 0 until n) {
                X.set(j,i, A[i][j])
            }
        }
        return X
    }

    // TODO one norm
    // TODO two norm
    // TODO infinity norm
    // TODO frobenius norm

    /**
     * unary minus
     * @return -A
     */
    fun uminus(): Matrix {
        val X: Matrix = Matrix(m,n)
        for (i in 0 until m) {
            for (j in 0 until n) {
                X.set(i,j, -A[i][j])
            }
        }
        return X
    }

    /**
     * plus: C = A + B
     * @param B another Matrix
     * @return A + B
      */
    fun plus(B: Matrix): Matrix {
        checkMatrixDimensions(B)
        val X: Matrix = Matrix(m,n)
        for (i in 0 until m) {
            for (j in 0 until n) {
                X.set(i,j, A[i][j] + B.get(i,j))
            }
        }
        return X
    }
    /**
     * plusEquals: A = A+B
     * @param B another Matrix
     * @return A + B
      */
    fun plusEquals(B: Matrix): Matrix {
        checkMatrixDimensions(B)
        for (i in 0 until m) {
            for (j in 0 until n) {
                A[i][j] = A[i][j] + B.get(i,j)
            }
        }
        return this
    }

    /**
     * minus: C = A-B
     * @param B another Matrix
     * @return  A - B
     */
    fun minus(B: Matrix): Matrix {
        checkMatrixDimensions(B)
        val X: Matrix = Matrix(m,n)
        for (i in 0 until m) {
            for (j in 0 until n) {
                X.set(i,j,A[i][j] - B.get(i,j))
            }
        }
        return X
    }

    /**
     * minusEquals: A = A-B
     * @param B another Matrix
     * @return A - B
     */
    fun minusEquals(B: Matrix): Matrix {
        checkMatrixDimensions(B)
        for (i in 0 until m) {
            for (j in 0 until n) {
                A[i][j] = A[i][j] - B.get(i,j)
            }
        }
        return this
    }
    /**
     * Element-by-element multiplication: C = A.*B
     * @param B another Matrix
     * @return A.*B
     */
    fun arrayTimes(B: Matrix): Matrix {
        checkMatrixDimensions(B)
        val X: Matrix = Matrix(m,n)
        for (i in 0 until m) {
            for (j in 0 until n) {
                X.set(i,j,A[i][j] * B.get(i,j))
            }
        }
        return X
    }
    // TODO Element-by-element multiplication in place: A = A.*B
    // TODO Element-by-element right division: C = A./B
    // TODO Element-by-element right division in place: A = A./B
    // TODO Element-by-element left division: C = A.\B
    // TODO Element-by-element left division in place: A = A.\B
    // TODO Multiply matrix by scalar: C = s*A
    // TODO Multiply matrix by scalar in place: A = s*A
    // TODO Linear algebraic multiplication, A * B
    // TODO LU decomposition
    // TODO QR Decomposition
    // TODO Cholesky decomposition
    // TODO Singular value decomposition
    // TODO Eigenvalue decomposition
    // TODO solve A*X = B
    // TODO Solve transpose  X*A = B, aka A'*X' = B'
    // TODO inverse
    // TODO determinant
    // TODO ank, from SVD
    // TODO Condition (2 norm)
    // TODO Trace (sum of diagonals)
    // TODO random elements
    // TODO identity
    // TODO print to stdout
    // TODO print to output stream
    // TODO print to stdout formatted
    // TODO print to output stream formatted
    // TODO read matrix from a stream

    /**
     * check dimensions
      */
    private fun checkMatrixDimensions(B: Matrix) {
        if (B.m != m || B.n != n)
            throw IllegalArgumentException("Matrix dimensions must agree.")
    }




}