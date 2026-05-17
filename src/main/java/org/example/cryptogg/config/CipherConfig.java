package org.example.cryptogg.config;


/**
 * Централизованная конфигурация параметров криптоанализа.
 */
public final class CipherConfig {

    private CipherConfig() {} // static only

    // Параметры блока
    public static final int BLOCK_SIZE_BITS = 208;
    public static final int BLOCK_SIZE_BYTES = BLOCK_SIZE_BITS / 8;

    // Параметры гиперграфа
    public static final int HYPERGRAPH_ARITY = 4;
    public static final int HYPERGRAPH_DENSITY = 10;

    // Параметры шифратора
    public static final int SMALL_BLOCK_SIZE = 1;

    // Параметры тестирования
    public static final int TEST_ITERATIONS = 100;
    public static final long RANDOM_SEED = 42L; // для воспроизводимости

    // Визуализация
    public static final int PREVIEW_COLUMNS = 10;
    public static final String HEX_FORMAT = "%02X";
}