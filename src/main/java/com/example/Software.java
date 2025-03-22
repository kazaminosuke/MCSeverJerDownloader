package com.example;

import java.util.List;

public class Software {
    private final String name;
    private final String category;
    private final String subCategory;
    private final List<String> versions;
    private final String iconUrl;
    private final String downloadUrlTemplate;
    private final String description;

    public Software(String name, String category, String subCategory,
                    List<String> versions, String iconUrl,
                    String downloadUrlTemplate, String description) {
        this.name = name;
        this.category = category;
        this.subCategory = subCategory;
        this.versions = versions;
        this.iconUrl = iconUrl;
        this.downloadUrlTemplate = downloadUrlTemplate;
        this.description = description;
    }

    // Getters
    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getSubCategory() { return subCategory; }
    public List<String> getVersions() { return versions; }
    public String getIconUrl() { return iconUrl; }
    public String getDownloadUrlTemplate() { return downloadUrlTemplate; }
    public String getDescription() { return description; }
}