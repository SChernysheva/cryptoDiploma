package org.example.cryptogg.util;

import java.util.Random;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Криптографические утилиты: генерация данных, базовые операции.
 */
public final class CryptoUtils {

    private CryptoUtils() {}

    /**
     * Генерирует случайные байты (потокобезопасно).
     */
    public static byte[] generateRandomBytes(int length) {
        byte[] bytes = new byte[length];
        ThreadLocalRandom.current().nextBytes(bytes);
        return bytes;
    }

    /**
     * Генерирует случайные байты с фиксированным seed (для тестов).
     */
    public static byte[] generateRandomBytes(int length, long seed) {
        byte[] bytes = new byte[length];
        new Random(seed).nextBytes(bytes);
        return bytes;
    }

    public static byte[] xor(byte[] a, byte[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Arrays must have equal length: "
                    + a.length + " vs " + b.length);
        }
        byte[] result = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            result[i] = (byte) (a[i] ^ b[i]);
        }
        return result;
    }

    public static boolean arraysEqual(byte[] a, byte[] b) {
        return Arrays.equals(a, b);
    }
}