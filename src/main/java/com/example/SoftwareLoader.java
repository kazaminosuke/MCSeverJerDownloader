package com.example;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class SoftwareLoader {
    private static final String GITHUB_RAW_URL = "YOUR_GITHUB_RAW_CSV_URL";
    private static final String CACHE_PATH = System.getProperty("user.home") + "/.software_manager/cache.csv";
    private static final long CACHE_EXPIRE_MS = 86400000;

    public static List<Software> loadSoftwareList() throws Exception {
        Path cachePath = Paths.get(CACHE_PATH);
        createParentDirectories(cachePath);

        if (!isCacheValid(cachePath)) {
            downloadFromGitHub(cachePath);
        }
        return parseCSV(Files.newBufferedReader(cachePath));
    }

    private static void createParentDirectories(Path path) throws IOException {
        Files.createDirectories(path.getParent());
    }

    private static boolean isCacheValid(Path path) {
        return Files.exists(path) &&
                (System.currentTimeMillis() - path.toFile().lastModified()) < CACHE_EXPIRE_MS;
    }

    private static void downloadFromGitHub(Path savePath) throws Exception {
        URL url = new URL(GITHUB_RAW_URL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        try (InputStream in = conn.getInputStream()) {
            Files.copy(in, savePath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static List<Software> parseCSV(BufferedReader br) throws IOException {
        List<Software> softwareList = new ArrayList<>();
        String line;

        while ((line = br.readLine()) != null) {
            String[] parts = line.split(",", 7);
            if (parts.length < 7) continue;

            List<String> versions = Arrays.asList(parts[5].split(" "));
            softwareList.add(new Software(
                    parts[0],
                    parts[1],
                    parts[2],
                    versions,
                    parts[3],
                    parts[4],
                    parts[6]
            ));
        }
        return softwareList;
    }
}