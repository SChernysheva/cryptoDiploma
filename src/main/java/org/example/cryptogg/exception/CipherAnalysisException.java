package org.example.cryptogg.exception;

/**
 * Исключение, указывающее на ошибку в процессе криптоанализа.
 */
public class CipherAnalysisException extends RuntimeException {

    public CipherAnalysisException(String message) {
        super(message);
    }

    public CipherAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}