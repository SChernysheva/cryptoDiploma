package org.example.cryptogg.linear;

import org.example.cryptogg.exception.CipherAnalysisException;

;

/**
 * Инвертирование матриц над GF(2) методом Гаусса-Жордана.
 */
public final class MatrixInverter {

    private MatrixInverter() {}

    /**
     * Вычисляет обратную матрицу для квадратной матрицы над GF(2).
     * @throws CipherAnalysisException если матрица вырождена
     */
    public static Gf2Matrix invert(Gf2Matrix matrix) {
        if (!matrix.isSquare()) {
            throw new IllegalArgumentException("Matrix must be square");
        }
        int n = matrix.getRows();
        Gf2Matrix augmented = matrix.augmentWithIdentity();

        for (int col = 0; col < n; col++) {
            int pivot = findPivot(augmented, col, col);
            if (pivot == -1) {
                throw new CipherAnalysisException(
                        String.format("Matrix is singular: no pivot in column %d", col));
            }

            if (pivot != col) {
                augmented.swapRows(col, pivot);
            }

            for (int row = 0; row < n; row++) {
                if (row != col && augmented.get(row, col)) {
                    augmented.xorRowInto(row, col);
                }
            }
        }

        return augmented.extractRightHalf();
    }

    /**
     * Поиск опорного элемента начиная с указанной строки.
     * @return индекс строки с опорным элементом или -1 если не найден
     */
    private static int findPivot(Gf2Matrix matrix, int col, int startRow) {
        for (int row = startRow; row < matrix.getRows(); row++) {
            if (matrix.get(row, col)) {
                return row;
            }
        }
        return -1;
    }
}