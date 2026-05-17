package org.example.cryptogg;

import org.example.cryptogg.linear.Gf2Matrix;
import org.example.cryptogg.util.BitVectorUtils;

/**
 * Дешифратор, использующий восстановленную обратную матрицу.
 * <p>
 * Реализует: P = M⁻¹ × C над полем GF(2)
 * </p>
 */
public class HackedDecryptor {

    private final Gf2Matrix inverseMatrix;
    private final int dimension;
    private final int blockBytes;

    public HackedDecryptor(Gf2Matrix inverseMatrix, int dimension) {
        if (!inverseMatrix.isSquare() || inverseMatrix.getRows() != dimension) {
            throw new IllegalArgumentException("Inverse matrix dimension mismatch");
        }
        this.inverseMatrix = inverseMatrix;
        this.dimension = dimension;
        this.blockBytes = dimension / 8;
    }

    /**
     * Дешифрует сообщение применением обратного линейного преобразования.
     */
    public byte[] decrypt(byte[] ciphertext) {
        if (ciphertext.length != blockBytes) {
            throw new IllegalArgumentException(
                    "Ciphertext length " + ciphertext.length +
                            " != expected " + blockBytes);
        }

        boolean[] cipherBits = BitVectorUtils.bytesToBits(ciphertext);
        boolean[] plainBits = new boolean[dimension];

        // Матричное умножение: P = M⁻¹ × C над GF(2)
        for (int i = 0; i < dimension; i++) {
            boolean bit = false;
            for (int j = 0; j < dimension; j++) {
                if (inverseMatrix.get(i, j) && cipherBits[j]) {
                    bit ^= true;
                }
            }
            plainBits[i] = bit;
        }

        return BitVectorUtils.bitsToBytes(plainBits);
    }

    public int getDimension() { return dimension; }
    public int getBlockBytes() { return blockBytes; }
}