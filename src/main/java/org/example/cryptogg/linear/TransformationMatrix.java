package org.example.cryptogg.linear;

import org.example.cryptogg.Encryptor;
import org.example.cryptogg.config.CipherConfig;
import org.example.cryptogg.util.BitVectorUtils;

import java.util.Map;

/**
 * Сбор и построение матрицы линейного преобразования шифра.
 */
public class TransformationMatrix {

    private final Gf2Matrix matrix;
    private final int dimension;

    private TransformationMatrix(Gf2Matrix matrix, int dimension) {
        this.matrix = matrix;
        this.dimension = dimension;
    }

    public static TransformationMatrix buildFromBasis(
            Map<Integer, byte[]> basisEncryptions, int dimension) {

        Gf2Matrix matrix = new Gf2Matrix(dimension, dimension);

        for (int col = 0; col < dimension; col++) {
            byte[] ciphertext = basisEncryptions.get(col);
            if (ciphertext == null) {
                throw new IllegalStateException("Missing encryption for basis vector " + col);
            }
            boolean[] bits = BitVectorUtils.bytesToBits(ciphertext);
            for (int row = 0; row < dimension; row++) {
                matrix.set(row, col, bits[row]);
            }
        }

        return new TransformationMatrix(matrix, dimension);
    }

    /**
     * Собирает данные путём шифрования всех базисных векторов.
     * Принимает общий интерфейс Encryptor.
     */
    public static TransformationMatrix collect(Encryptor encryptor, int dimension) {
        Map<Integer, byte[]> basisEncryptions = new java.util.HashMap<>(dimension);
        int nBytes = CipherConfig.BLOCK_SIZE_BYTES;

        for (int bit = 0; bit < dimension; bit++) {
            byte[] plaintext = BitVectorUtils.createSingleBitVector(bit, nBytes);
            byte[] ciphertext = encryptor.encrypt(plaintext);
            basisEncryptions.put(bit, ciphertext);

            if ((bit + 1) % 50 == 0) {
                System.out.printf("   ✓ Обработано %d/%d базисных векторов%n",
                        bit + 1, dimension);
            }
        }

        return buildFromBasis(basisEncryptions, dimension);
    }

    public Gf2Matrix getMatrix() { return matrix; }
    public int getDimension() { return dimension; }
}