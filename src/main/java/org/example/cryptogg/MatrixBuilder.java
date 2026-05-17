package org.example.cryptogg;

import org.example.cryptogg.util.BitVectorUtils;

import java.util.Map;

/**
 * Утилиты для операций с матрицами над полем GF(2).
 */
public class MatrixBuilder {
    private MatrixBuilder() {} // static only

    /**
     * Строит матрицу линейного преобразования из отображений базисных векторов.
     * <p>
     * Каждый зашифрованный базисный вектор становится столбцом результирующей матрицы.
     * </p>
     * @param transforms отображение: индекс бита → зашифрованный вектор
     * @param n размерность матрицы
     * @return матрица преобразования размера n×n над GF(2)
     */
    public static boolean[][] buildTransformationMatrix(Map<Integer, byte[]> transforms, int n) {
        final boolean[][] matrix = new boolean[n][n];

        for (int col = 0; col < n; col++) {
            final byte[] ciphertext = transforms.get(col);
            final boolean[] cipherBits = BitVectorUtils.bytesToBits(ciphertext);

            for (int row = 0; row < n; row++) {
                matrix[row][col] = cipherBits[row];
            }
        }
        return matrix;
    }
}