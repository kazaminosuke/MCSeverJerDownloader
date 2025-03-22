package com.example;

import java.util.prefs.Preferences;

public class AppConfig {
    private static final Preferences prefs = Preferences.userNodeForPackage(AppConfig.class);
    private static final String DOWNLOAD_PATH_KEY = "defaultDownloadPath";

    public static void setDefaultDownloadPath(String path) {
        prefs.put(DOWNLOAD_PATH_KEY, path);
    }

    public static String getDefaultDownloadPath() {
        return prefs.get(DOWNLOAD_PATH_KEY,
                System.getProperty("user.home") + "/Downloads");
    }
}