package com.outsera.ms_golden_raspberry_awards.util;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;

import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;

public class CsvHashValidatorTest {

    private String calculateSHA256(Path filePath) throws Exception {
        byte[] fileBytes = Files.readAllBytes(filePath);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(fileBytes);

        StringBuilder sb = new StringBuilder();
        for (byte b : hashBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @Test
    void shouldFailIfCsvFileWasModified() throws Exception {
        // Caminhos dos arquivos
        Path csvPath = new ClassPathResource("Movielist.csv").getFile().toPath();
        Path hashPath = new ClassPathResource("movie_hash.sha256").getFile().toPath();

        // Leitura do hash esperado
        String expectedHash = Files.readString(hashPath, StandardCharsets.UTF_8).trim();

        // Hash atual do CSV
        String currentHash = calculateSHA256(csvPath);

        // Validação: falha se os hashes forem diferentes
        assertFalse(
            !expectedHash.equals(currentHash),
            () -> String.format("O arquivo CSV foi modificado!\nEsperado: %s\nAtual:   %s", expectedHash, currentHash)
        );
    }
}

