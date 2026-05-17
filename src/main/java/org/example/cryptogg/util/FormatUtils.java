package org.example.cryptogg.util;

/**
 * Утилиты форматирования для консольного вывода.
 */
public final class FormatUtils {

    private FormatUtils() {}

    public static String bytesToHex(byte[] bytes) {
        if (bytes == null) return "null";
        StringBuilder hex = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            hex.append(String.format("%02X", b));
        }
        return hex.toString();
    }

    public static String bytesToHexPreview(byte[] bytes, int maxBytes) {
        if (bytes == null) return "null";
        int limit = Math.min(bytes.length, maxBytes);
        String preview = bytesToHex(java.util.Arrays.copyOfRange(bytes, 0, limit));
        return bytes.length > limit ? preview + "..." : preview;
    }

    public static String center(String text, int width) {
        if (text.length() >= width) return text;
        int padding = (width - text.length()) / 2;
        return " ".repeat(padding) + text;
    }

    public static String separator(char ch, int length) {
        return String.valueOf(ch).repeat(length);
    }

    public static String boxedHeader(String title, int width) {
        String border = "═".repeat(width);
        return "\n" + border + "\n" + center(title, width) + "\n" + border + "\n";
    }
}