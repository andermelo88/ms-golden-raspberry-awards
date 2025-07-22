package com.outsera.ms_golden_raspberry_awards.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CsvHashValidator {

    @Value("${csv.file.path}")
    private String csvFilePath;
    @Value("${csv.hash.path}")
    private String csvHashFilePath;
    
    public boolean csvIsNotModified() {
        try {
            Path csvPath = Paths.get(csvFilePath);
            Path csvHashPath = Paths.get(csvHashFilePath);

            if (!Files.exists(csvPath) || !Files.exists(csvHashPath)) {
                System.out.println("CSV or hash file does not exist.");
                return false;
            }

            return isCsvUnchanged(csvPath, csvHashPath);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
            return false;
        }
    }


    // Gera o hash SHA-256 de um arquivo
    public static String generateSHA256(Path filePath) throws IOException, NoSuchAlgorithmException {
        byte[] content = Files.readAllBytes(filePath);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(content);
        return bytesToHex(hashBytes);
    }

    // Salva o hash em um arquivo .sha256
    public static void saveHashToFile(Path hashFilePath, String hashValue) throws IOException {
        Files.writeString(hashFilePath, hashValue, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
    }

    // Lê o hash de um arquivo .sha256
    public static String readHashFromFile(Path hashFilePath) throws IOException {
        return Files.readString(hashFilePath, StandardCharsets.UTF_8).trim();
    }

    // Valida se o conteúdo do arquivo CSV é igual ao hash registrado
    public static boolean isCsvUnchanged(Path csvFilePath, Path hashFilePath) throws IOException, NoSuchAlgorithmException {
        String currentHash = generateSHA256(csvFilePath);
        String expectedHash = readHashFromFile(hashFilePath);
        return currentHash.equalsIgnoreCase(expectedHash);
    }

    // Converte bytes para hex string
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hex = new StringBuilder();
        for (byte b : bytes) {
            hex.append(String.format("%02x", b));
        }
        return hex.toString();
    }
}