package hr.fer.zemris.apr.math;

/**
 * Simple interface that provides some generic methods for matrix manipulation.
 *
 * @author dbrcina
 */
public interface IMatrix {

    /**
     * @return number of rows.
     */
    int getRowsCount();

    /**
     * @return number of columns.
     */
    int getColumnsCount();

    /**
     * Retrieves a value at position <code>[row][column]</code>.
     *
     * @param row    row index.
     * @param column column index.
     * @return a value from provided position.
     * @throws IndexOutOfBoundsException if <code>row</code> or <code>column</code> index is invalid.
     */
    double get(int row, int column);

    /**
     * Sets a value at position <code>[row][column]</code>.
     *
     * @param row    row index.
     * @param column column index.
     * @param value  value.
     * @throws IndexOutOfBoundsException if <code>row</code> or <code>column</code> index is invalid.
     */
    void set(int row, int column, double value);

    /**
     * Adds <b>this</b> IMatrix with <code>other</code> IMatrix. Method modifies <b>this</b> IMatrix.
     *
     * @param other other IMatrix.
     * @return <b>this</b> IMatrix.
     * @throws IllegalArgumentException if dimensions of both IMatrix aren't equal.
     */
    IMatrix add(IMatrix other);

    /**
     * Creates a new IMatrix object and then applies {@link #add(IMatrix)} method.
     *
     * @param other other IMatrix.
     * @return new instance of IMatrix as a result.
     */
    default IMatrix nAdd(IMatrix other) {
        return copy().add(other);
    }

    /**
     * Substitutes <b>this</b> IMatrix with <code>other</code> IMatrix. Method modifies <b>this</b> IMatrix.
     *
     * @param other other IMatrix.
     * @return <b>this</b> IMatrix.
     * @throws IllegalArgumentException if dimensions of both IMatrix aren't equal.
     */
    IMatrix sub(IMatrix other);

    /**
     * Creates a new IMatrix object and then applies {@link #sub(IMatrix)} method.
     *
     * @param other other IMatrix.
     * @return new instance of IMatrix as a result.
     */
    default IMatrix nSub(IMatrix other) {
        return copy().sub(other);
    }

    /**
     * Multiplies each element of <b>this</b> IMatrix by provided <code>scalar</code> value.
     *
     * @param scalar scalar value.
     * @return <b>this</b> IMatrix.
     */
    IMatrix scalar(double scalar);

    /**
     * Creates a new IMatrix object and then applies {@link #scalar(double)} method.
     *
     * @param scalar scalar value.
     * @return new instance of IMatrix as a result.
     */
    default IMatrix nScalar(double scalar) {
        return copy().scalar(scalar);
    }

    /**
     * Performs matrix transpose operation.
     *
     * @return new instance of IMatrix as a result.
     */
    IMatrix transpose();

    /**
     * Performs matrix multiplication operation.
     *
     * @param other other IMatrix.
     * @return new instance of IMatrix as a result.
     * @throws IllegalArgumentException if number of columns and number of rows of <b>this</b> and <code>other</code>
     *                                  matrices aren't equal.
     */
    IMatrix mul(IMatrix other);

    /**
     * Calculates matrix determinant.
     *
     * @return matrix determinant.
     * @throws RuntimeException if matrix is not square matrix.
     */
    double determinant();

    /**
     * Creates a new IMatrix object without provided <code>row</code> and <code>column</code>.
     *
     * @return new instance of IMatrix as a result.
     * @throws IndexOutOfBoundsException if <code>row</code> or <code>column</code> index is invalid.
     */
    IMatrix subMatrix(int row, int column);

    /**
     * Swaps row <code>r1</code> with row <code>r2</code>.
     *
     * @param r1 row 1 index.
     * @param r2 row 2 index.
     * @return <b>this</b> matrix.
     * @throws IndexOutOfBoundsException if row indexes are invalid.
     */
    IMatrix swapRows(int r1, int r2);

    /**
     * @return number of rows swapped by {@link #swapRows(int, int)} method.
     */
    int getNumOfRowsSwapped();

    boolean isSquareMatrix();

    /**
     * Makes a copy of <b>this</b> IMatrix.
     *
     * @return copied IMatrix.
     */
    IMatrix copy();

    /**
     * Creates a new IMatrix object which dimensions set to provided <code>rows</code> and <code>columns</code> and
     * with all values set to zero.
     *
     * @return new instance of IMatrix as a result.
     * @throws IllegalArgumentException if value < 1 is provided.
     */
    IMatrix newInstance(int rows, int columns);

    /**
     * Creates an identity matrix based on <b>this</b> matrix.
     *
     * @return new instance of IMatrix as a result.
     */
    IMatrix identity();

}