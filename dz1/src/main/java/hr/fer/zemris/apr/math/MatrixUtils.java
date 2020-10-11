package hr.fer.zemris.apr.math;

import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Utility class for matrix manipulation.
 *
 * @author dbrcina
 */
public class MatrixUtils {

    /* ---------------------------------------------------------------- */
    /* ------------------------- PARSE METHODS ------------------------ */

    /**
     * Creates an instance of IMatrix whose definition is parsed from provided <code>file</code>. Each line in a file
     * represents one row and in each row values are separated by spaces or tabs and they represents columns.
     *
     * @param file matrix definition.
     * @return IMatrix as a parse result.
     * @throws Exception if something goes wrong or file definition if is invalid.
     */
    public static IMatrix parseFromFile(String file) throws Exception {
        List<String> lines = Files.readAllLines(Paths.get(file));
        int rows = lines.size();
        double[][] data = new double[rows][];
        try {
            for (int i = 0; i < rows; i++) {
                // this will break if string value is not parsable into double value
                double[] column = Arrays.stream(lines.get(i).split("\\s+"))
                        .mapToDouble(Double::parseDouble)
                        .toArray();
                data[i] = column;
            }
            // check if column numbers are consistent
            if (Arrays.stream(data).map(d -> d.length).distinct().count() != 1) {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            throw new RuntimeException("Parsing '" + file + "' failed! Matrix definition is invalid!");
        }
        return new Matrix(data);
    }

    /* ---------------------------------------------------------------- */
    /* ------------------------- PRINT METHODS ------------------------ */

    /**
     * Prints provided <code>matrix</code> to <code>out</code> stream.
     *
     * @param matrix matrix.
     * @param out    outputs stream.
     */
    public static void printMatrix(IMatrix matrix, PrintStream out) {
        for (int i = 0; i < matrix.getRowsCount(); i++) {
            double[] row = new double[matrix.getColumnsCount()];
            for (int j = 0; j < matrix.getColumnsCount(); j++) {
                row[j] = matrix.get(i, j);
            }
            String rowString = Arrays.stream(row)
                    .mapToObj(Double::toString)
                    .collect(Collectors.joining(" "));
            out.println(rowString);
        }
        out.flush();
    }

    /* ---------------------------------------------------------------- */
    /* --------------------- DECOMPOSITION METHODS -------------------- */

    /**
     * Performs forward substitution operation on provided matrix <code>L</code> and matrix (vector) <code>b</code>,
     * whose number of rows  should be equal to the dimensions of square matrix <code>L</code>. Changes are applied
     * to the <code>b</code>'s real reference without copying.
     *
     * @param L matrix L.
     * @param b vector b (n x 1).
     * @return IMatrix as a result of substitution.
     * @throws IllegalArgumentException if matrix <code>L</code> is not square matrix or if dimensions of vector
     *                                  <code>b</code> don't fit.
     */
    public static IMatrix fs(IMatrix L, IMatrix b) {
        if (!L.isSquareMatrix()) {
            throw new IllegalArgumentException(
                    "MatrixUtils::fs(IMatrix,IMatrix) cannot be performed on non square matrix!");
        }
        if (b.getRowsCount() != L.getRowsCount()) {
            throw new IllegalArgumentException("MatrixUtils::fs(IMatrix,IMatrix) vectors b dimensions are invalid!");
        }
        for (int i = 0; i < L.getRowsCount() - 1; i++) {
            for (int j = i + 1; j < L.getRowsCount(); j++) {
                b.set(j, 0, b.get(j, 0) - L.get(j, i) * b.get(i, 0));
            }
        }
        return b;
    }

    /**
     * Performs backward substitution operation on provided matrix <code>U</code> and matrix (vector) <code>y</code>,
     * whose number of rows should be equal to the dimensions of square matrix <code>U</code>. Changes are applied to
     * the <code>y</code>'s real reference without copying.
     *
     * @param U matrix U.
     * @param y vector y (n x 1).
     * @return IMatrix as a result of substitution.
     * @throws IllegalArgumentException if matrix <code>U</code> is not square matrix or if dimensions of vector
     *                                  <code>y</code> don't fit.
     */
    public static IMatrix bs(IMatrix U, IMatrix y) {
        if (!U.isSquareMatrix()) {
            throw new IllegalArgumentException(
                    "MatrixUtils::bs(IMatrix,IMatrix) cannot be performed on non square matrix!");
        }
        if (y.getRowsCount() != U.getRowsCount()) {
            throw new IllegalArgumentException("MatrixUtils::bs(IMatrix,IMatrix) vectors y dimensions are invalid!");
        }
        for (int i = U.getRowsCount() - 1; i >= 0; i--) {
            for (int j = i + 1; j < U.getRowsCount(); j++) {
                y.set(i, 0, y.get(i, 0) - U.get(i, j) * y.get(j, 0));
            }
            y.set(i, 0, y.get(i, 0) / U.get(i, i));
        }
        return y;
    }

    /**
     * Based on provided boolean flag <code>useLUP</code>, method performs <b>LUP Decomposition</b> if flag is set to
     * <code>true</code>, otherwise it performs <b>LU Decomposition</b> on provided matrix <code>A</code>.
     * <b>LUP Decomposition</b> is <b>LU Decomposition</b> + partial pivoting by columns.
     *
     * @param A      matrix A.
     * @param useLUP boolean flag.
     * @return an array that consists of matrix L, matrix U and (optionally) matrix P (it can be <code>null!</code>).
     * @throws IllegalArgumentException if provided matrix <code>A</code> is not square matrix.
     * @throws ArithmeticException      if a pivot element is zero.
     */
    public static IMatrix[] luDecomposition(IMatrix A, boolean useLUP) {
        if (!A.isSquareMatrix()) {
            throw new IllegalArgumentException(
                    "MatrixUtils::luDecomposition(IMatrix,boolean) cannot be performed on non square matrix!");
        }
        IMatrix P = useLUP ? A.identity() : null;
        for (int i = 0; i < A.getRowsCount() - 1; i++) {
            if (useLUP) {
                int maxPivotRowIndex = i;
                double maxPivot = A.get(i, i);
                for (int j = 1; j < A.getRowsCount(); j++) {
                    if (Math.abs(A.get(j, i)) > Math.abs(maxPivot)) {
                        maxPivot = A.get(j, i);
                        maxPivotRowIndex = j;
                    }
                }
                if (Math.abs(maxPivot) <= 1e-9) {
                    throw new ArithmeticException("MatrixUtils::luDecomposition(IMatrix,boolean) pivot is zero!");
                }
                A.swapRows(maxPivotRowIndex, i);
                P.swapRows(maxPivotRowIndex, i);
            }
            for (int j = i + 1; j < A.getRowsCount(); j++) {
                try {
                    A.set(j, i, A.get(j, i) / A.get(i, i));
                } catch (ArithmeticException e) {
                    throw new ArithmeticException("MatrixUtils::luDecomposition(IMatrix,boolean) division by zero!");
                }
                for (int k = i + 1; k < A.getRowsCount(); k++) {
                    A.set(j, k, A.get(j, k) - A.get(j, i) * A.get(i, k));
                }
            }
        }
        return new IMatrix[]{new LUMatrixView(A, true), new LUMatrixView(A, false), P};
    }

    /**
     * Calculates determinant by using results from <b>LU(P) Decomposition</b>.
     *
     * @param numOfRowsSwapped number of rows swapped in lup decomposition.
     * @param L                matrix L.
     * @param U                matrix U.
     * @return calculated determinant.
     */
    public static double lupDeterminant(int numOfRowsSwapped, IMatrix L, IMatrix U) {
        return Math.pow(-1, numOfRowsSwapped) * L.determinant() * U.determinant();
    }

}