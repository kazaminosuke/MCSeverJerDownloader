package com.example;

import javax.swing.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class DownloadManager {
    private static final int BUFFER_SIZE = 8192;
    private static final int MAX_RETRIES = 3;
    private static final int CONNECT_TIMEOUT = 15000;

    public static void downloadFile(
            Software software,
            String version,
            ProgressMonitor monitor
    ) throws IOException {
        String downloadUrl = buildDownloadUrl(software, version);
        Path outputPath = buildOutputPath(software, version);

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                attemptDownload(downloadUrl, outputPath, monitor);
                return;
            } catch (IOException e) {
                if (attempt == MAX_RETRIES) throw e;
                Thread.sleep(1000 * attempt);
            }
        }
    }

    private static String buildDownloadUrl(Software software, String version) {
        return software.getDownloadUrlTemplate()
                .replace("{version}", version)
                .replace("{mc_version}", version.split("#")[0])
                .replace("{build}", version.split("#")[1]);
    }

    private static Path buildOutputPath(Software software, String version) {
        String fileName = String.format("%s-%s%s",
                software.getName(),
                version,
                determineFileExtension(software.getDownloadUrlTemplate()));

        return Paths.get(AppConfig.getDefaultDownloadPath(), fileName);
    }

    private static String determineFileExtension(String urlTemplate) {
        if (urlTemplate.contains(".jar")) return ".jar";
        if (urlTemplate.contains(".zip")) return ".zip";
        return ".bin";
    }

    private static void attemptDownload(
            String fileUrl,
            Path savePath,
            ProgressMonitor monitor
    ) throws IOException {
        HttpURLConnection connection = createConnection(fileUrl);

        try (InputStream in = connection.getInputStream();
             OutputStream out = Files.newOutputStream(savePath)) {

            monitor.setMaximum(connection.getContentLength());
            copyStreamWithProgress(in, out, monitor);
        } finally {
            connection.disconnect();
        }
    }

    private static HttpURLConnection createConnection(String fileUrl) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL(fileUrl).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(CONNECT_TIMEOUT);
        conn.setReadTimeout(CONNECT_TIMEOUT);
        return conn;
    }

    private static void copyStreamWithProgress(
            InputStream in,
            OutputStream out,
            ProgressMonitor monitor
    ) throws IOException {
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        long totalRead = 0;

        while ((bytesRead = in.read(buffer)) != -1) {
            if (monitor.isCanceled()) {
                throw new InterruptedIOException("ユーザーによりキャンセルされました");
            }
            out.write(buffer, 0, bytesRead);
            totalRead += bytesRead;
            monitor.setProgress((int) totalRead);
        }
    }
}