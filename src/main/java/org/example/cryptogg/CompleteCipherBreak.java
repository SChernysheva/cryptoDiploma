package org.example.cryptogg;

import org.example.cryptogg.config.CipherConfig;
import org.example.cryptogg.demo.DemoRunner;
import org.example.cryptogg.linear.Gf2Matrix;
import org.example.cryptogg.linear.MatrixInverter;
import org.example.cryptogg.linear.TransformationMatrix;
import org.example.cryptogg.util.FormatUtils;

import org.reminstant.crypto.symmetric.HypergraphEncryptor;
import org.reminstant.math.graphtheory.hyper.HomogenousHypergraph;
import org.reminstant.math.graphtheory.hyper.HyperTreeRandomGenerator;

/**
 * Точка входа: полная демонстрация криптоанализа линейного гиперграфового шифра.
 */
public class CompleteCipherBreak {

    public static void main(String[] args) {
        logHeader();

        HypergraphEncryptor encryptor = createEncryptor();

        Encryptor encryptorAdapter = encryptor::encrypt;

        System.out.println("Сбор данных о преобразовании...");
        TransformationMatrix transform = TransformationMatrix.collect(
                encryptorAdapter,
                CipherConfig.BLOCK_SIZE_BITS);

        System.out.println("Вычисление обратной матрицы...");
        Gf2Matrix inverse = MatrixInverter.invert(transform.getMatrix());

        HackedDecryptor hackedDecryptor = new HackedDecryptor(
                inverse, CipherConfig.BLOCK_SIZE_BITS);

        DemoRunner.run(encryptorAdapter, hackedDecryptor);

    }

    private static HypergraphEncryptor createEncryptor() {
        System.out.println("Генерация ключа...");
        var generator = new HyperTreeRandomGenerator(
                CipherConfig.BLOCK_SIZE_BITS,
                CipherConfig.HYPERGRAPH_ARITY,
                CipherConfig.HYPERGRAPH_DENSITY);
        HomogenousHypergraph key = generator.next();

        return new HypergraphEncryptor(
                key,
                CipherConfig.SMALL_BLOCK_SIZE,
                HypergraphEncryptor.SmallBlockSizeUnit.BIT);
    }

    private static void logHeader() {
        System.out.println("\n" + FormatUtils.separator('★', 70));
        System.out.println("   CRYPTOANALYSIS: Hypergraph Cipher Linear Attack");
        System.out.printf("   Block: %d bits (%d bytes)%n",
                CipherConfig.BLOCK_SIZE_BITS, CipherConfig.BLOCK_SIZE_BYTES);
        System.out.println(FormatUtils.separator('★', 70) + "\n");
    }
}