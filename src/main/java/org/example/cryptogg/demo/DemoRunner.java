package org.example.cryptogg.demo;

import org.example.cryptogg.Encryptor;
import org.example.cryptogg.HackedDecryptor;
import org.example.cryptogg.config.CipherConfig;
import org.example.cryptogg.util.CryptoUtils;
import org.example.cryptogg.util.FormatUtils;

import java.util.Arrays;

/**
 * Запуск практических демонстраций взлома.
 */
public final class DemoRunner {

    private DemoRunner() {}

    /**
     * Запускает все демо-сценарии.
     * @param encryptor шифратор (общий интерфейс)
     * @param decryptor взломанный дешифратор
     */
    public static void run(Encryptor encryptor, HackedDecryptor decryptor) {
        System.out.println(FormatUtils.boxedHeader("ПРАКТИЧЕСКАЯ ДЕМОНСТРАЦИЯ", 70));

        demoTextMessage(encryptor, decryptor);
        demoNumericData(encryptor, decryptor);
        demoUnknownCiphertext(encryptor, decryptor);
        runStatisticalTests(encryptor, decryptor);
    }

    private static void demoTextMessage(Encryptor enc, HackedDecryptor dec) {
        System.out.println("Пример 1: Текстовое сообщение");
        System.out.println("─".repeat(50));

        String message = "TOP SECRET MESSAGE!";
        byte[] plaintext = Arrays.copyOf(message.getBytes(), CipherConfig.BLOCK_SIZE_BYTES);

        System.out.printf("Оригинал:   \"%s\"%n", message);
        System.out.printf("HEX:        %s%n", FormatUtils.bytesToHex(plaintext));

        byte[] encrypted = enc.encrypt(plaintext);
        System.out.printf("Зашифровано: %s%n", FormatUtils.bytesToHex(encrypted));

        byte[] decrypted = dec.decrypt(encrypted);
        System.out.printf("Взломано:   %s%n", FormatUtils.bytesToHex(decrypted));
        System.out.printf("Текст:      \"%s\"%n%n", new String(decrypted).trim());
    }

    private static void demoNumericData(Encryptor enc, HackedDecryptor dec) {
        System.out.println("Пример 2: Числовые данные");
        System.out.println("─".repeat(50));

        byte[] data = new byte[CipherConfig.BLOCK_SIZE_BYTES];
        for (int i = 0; i < data.length; i++) {
            data[i] = (byte) ((i * 17) & 0xFF);
        }

        System.out.printf("Оригинал:   %s%n", FormatUtils.bytesToHex(data));
        byte[] encrypted = enc.encrypt(data);
        System.out.printf("Зашифровано: %s%n", FormatUtils.bytesToHex(encrypted));
        byte[] decrypted = dec.decrypt(encrypted);
        System.out.printf("Взломано:   %s%n", FormatUtils.bytesToHex(decrypted));

        boolean ok = Arrays.equals(data, decrypted);
        System.out.printf("   %s Верификация: %s%n%n",
                ok ? "✓" : "✗", ok ? "УСПЕШНО" : "ПРОВАЛ");
    }

    private static void demoUnknownCiphertext(Encryptor enc, HackedDecryptor dec) {
        System.out.println("Пример 3: Атака на неизвестное сообщение");
        System.out.println("─".repeat(50));

        byte[] plaintext = CryptoUtils.generateRandomBytes(CipherConfig.BLOCK_SIZE_BYTES);
        byte[] ciphertext = enc.encrypt(plaintext);

        System.out.printf("Перехвачен: %s%n", FormatUtils.bytesToHex(ciphertext));
        byte[] recovered = dec.decrypt(ciphertext);
        System.out.printf("Восстановлен: %s%n", FormatUtils.bytesToHex(recovered));

        byte[] reencrypted = enc.encrypt(recovered);
        boolean verified = Arrays.equals(reencrypted, ciphertext);
        System.out.printf("   %s Целостность: %s%n%n",
                verified ? "✓" : "✗", verified ? "ПОДТВЕРЖДЕНА" : "НАРУШЕНА");
    }

    private static void runStatisticalTests(Encryptor enc, HackedDecryptor dec) {
        System.out.println("Статистическое тестирование");
        System.out.println("─".repeat(50));
        System.out.printf("   Запуск %d тестов...%n", CipherConfig.TEST_ITERATIONS);

        int successes = 0;

        for (int i = 0; i < CipherConfig.TEST_ITERATIONS; i++) {
            byte[] plaintext = CryptoUtils.generateRandomBytes(
                    CipherConfig.BLOCK_SIZE_BYTES,
                    CipherConfig.RANDOM_SEED + i  // уникальный seed для каждого теста
            );
            byte[] ciphertext = enc.encrypt(plaintext);
            byte[] decrypted = dec.decrypt(ciphertext);
            if (Arrays.equals(plaintext, decrypted)) successes++;
        }

        double rate = successes * 100.0 / CipherConfig.TEST_ITERATIONS;
        System.out.printf("   Результат: %d/%d (%.2f%%)",
                successes, CipherConfig.TEST_ITERATIONS, rate);
    }
}