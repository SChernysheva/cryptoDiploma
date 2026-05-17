package org.example.cryptogg.util;

/**
 * Утилиты для работы с битовыми векторами (MSB-first порядок).
 */
public final class BitVectorUtils {

    private BitVectorUtils() {}

    /**
     * Создаёт вектор с единственным установленным битом.
     * @param bitPosition позиция бита (0..n*8-1)
     * @param nBytes размер вектора в байтах
     */
    public static byte[] createSingleBitVector(int bitPosition, int nBytes) {
        validateBitPosition(bitPosition, nBytes);
        byte[] vector = new byte[nBytes];
        int byteIndex = bitPosition / 8;
        int bitIndex = 7 - (bitPosition % 8);
        vector[byteIndex] = (byte) (1 << bitIndex);
        return vector;
    }

    /**
     * Преобразует byte[] → boolean[] (MSB-first).
     */
    public static boolean[] bytesToBits(byte[] bytes) {
        if (bytes == null) throw new IllegalArgumentException("bytes cannot be null");
        boolean[] bits = new boolean[bytes.length * 8];
        for (int i = 0; i < bytes.length; i++) {
            for (int j = 0; j < 8; j++) {
                bits[i * 8 + j] = ((bytes[i] >> (7 - j)) & 1) == 1;
            }
        }
        return bits;
    }

    /**
     * Преобразует boolean[] → byte[] (MSB-first).
     */
    public static byte[] bitsToBytes(boolean[] bits) {
        if (bits == null || bits.length % 8 != 0) {
            throw new IllegalArgumentException("bits length must be multiple of 8");
        }
        byte[] bytes = new byte[bits.length / 8];
        for (int i = 0; i < bytes.length; i++) {
            byte b = 0;
            for (int j = 0; j < 8; j++) {
                if (bits[i * 8 + j]) {
                    b |= (1 << (7 - j));
                }
            }
            bytes[i] = b;
        }
        return bytes;
    }

    private static void validateBitPosition(int pos, int nBytes) {
        if (pos < 0 || pos >= nBytes * 8) {
            throw new IllegalArgumentException(
                    "Bit position " + pos + " out of range [0, " + (nBytes * 8 - 1) + "]");
        }
    }
}