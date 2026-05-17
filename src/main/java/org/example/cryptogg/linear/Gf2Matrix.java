package org.example.cryptogg.linear;

import java.util.Arrays;

/**
 * Матрица над полем Галуа GF(2) с базовыми операциями.
 * <p>
 * Элементы хранятся как boolean: true = 1, false = 0.
 * Операции: сложение = XOR, умножение = AND.
 * </p>
 */
public class Gf2Matrix {

    private final boolean[][] data;
    private final int rows;
    private final int cols;

    public Gf2Matrix(int rows, int cols) {
        if (rows <= 0 || cols <= 0) {
            throw new IllegalArgumentException("Dimensions must be positive");
        }
        this.rows = rows;
        this.cols = cols;
        this.data = new boolean[rows][cols];
    }

    public Gf2Matrix(boolean[][] data) {
        if (data == null || data.length == 0) {
            throw new IllegalArgumentException("Data cannot be empty");
        }
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = new boolean[rows][cols];
        for (int i = 0; i < rows; i++) {
            if (data[i].length != cols) {
                throw new IllegalArgumentException("Jagged arrays not supported");
            }
            System.arraycopy(data[i], 0, this.data[i], 0, cols);
        }
    }

    public boolean get(int row, int col) {
        validateIndices(row, col);
        return data[row][col];
    }

    public void set(int row, int col, boolean value) {
        validateIndices(row, col);
        data[row][col] = value;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public boolean isSquare() { return rows == cols; }

    /**
     * XOR двух строк: target ^= source.
     */
    public void xorRowInto(int targetRow, int sourceRow) {
        validateRowIndex(targetRow);
        validateRowIndex(sourceRow);
        for (int col = 0; col < cols; col++) {
            data[targetRow][col] ^= data[sourceRow][col];
        }
    }

    public void swapRows(int i, int j) {
        validateRowIndex(i);
        validateRowIndex(j);
        if (i == j) return;
        boolean[] temp = data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    /**
     * Извлекает столбец как массив битов.
     */
    public boolean[] getColumn(int col) {
        validateColumnIndex(col);
        boolean[] column = new boolean[rows];
        for (int row = 0; row < rows; row++) {
            column[row] = data[row][col];
        }
        return column;
    }

    /**
     * Создаёт копию матрицы.
     */
    public Gf2Matrix copy() {
        return new Gf2Matrix(data);
    }

    /**
     * Создаёт расширенную матрицу [this | I] для метода Гаусса-Жордана.
     */
    public Gf2Matrix augmentWithIdentity() {
        if (!isSquare()) {
            throw new IllegalStateException("Matrix must be square for augmentation");
        }
        Gf2Matrix augmented = new Gf2Matrix(rows, cols * 2);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                augmented.set(i, j, data[i][j]);
            }
            augmented.set(i, cols + i, true);
        }
        return augmented;
    }

    /**
     * Извлекает правую половину квадратной расширенной матрицы.
     */
    public Gf2Matrix extractRightHalf() {
        if (cols % 2 != 0) {
            throw new IllegalStateException("Matrix must have even number of columns");
        }
        int half = cols / 2;
        Gf2Matrix result = new Gf2Matrix(rows, half);
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < half; j++) {
                result.set(i, j, data[i][half + j]);
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                sb.append(data[i][j] ? '1' : '0').append(' ');
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    private void validateIndices(int row, int col) {
        validateRowIndex(row);
        validateColumnIndex(col);
    }

    private void validateRowIndex(int row) {
        if (row < 0 || row >= rows) {
            throw new IndexOutOfBoundsException("Row " + row + " out of [0, " + (rows-1) + "]");
        }
    }

    private void validateColumnIndex(int col) {
        if (col < 0 || col >= cols) {
            throw new IndexOutOfBoundsException("Column " + col + " out of [0, " + (cols-1) + "]");
        }
    }
}