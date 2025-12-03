package com.bezkoder.spring.jwt.mongodb.models;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "resources")
public class ResourceFileStream {

    @Id
    private String id;

    @NotBlank
    @Size(min = 2)
    private String filename;

    @NotBlank
    private String storedName;

    private List<String> tags;

    @NotBlank
    private String path;

    @NotBlank
    private User author;

    public ResourceFileStream(@NotBlank @Size(min = 2) String filename, @NotBlank String storedName, List<String> tags, @NotBlank String path,
            @NotBlank User author) {
        // this.id = id;
        this.filename = filename;
        this.storedName = storedName;
        this.tags = tags;
        this.path = path;
        this.author = author;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getStoredName() {
        return storedName;
    }

    public void setStoredName(String storedName) {
        this.storedName = storedName;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

}