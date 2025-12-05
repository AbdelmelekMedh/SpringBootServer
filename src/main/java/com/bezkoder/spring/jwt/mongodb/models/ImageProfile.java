package com.bezkoder.spring.jwt.mongodb.models;

public class ImageProfile {
    private String filename;
    private String fileType;
    private String filePathUrl; // URL to access the image

    public ImageProfile() {
    }

    public ImageProfile(String filename, String fileType, String filePathUrl) {
        this.filename = filename;
        this.fileType = fileType;
        this.filePathUrl = filePathUrl;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFilePathUrl() {
        return filePathUrl;
    }

    public void setFilePathUrl(String filePathUrl) {
        this.filePathUrl = filePathUrl;
    }
}
