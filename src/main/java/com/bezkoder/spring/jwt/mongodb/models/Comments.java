package com.bezkoder.spring.jwt.mongodb.models;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Comments {

    @Id
    private String id;
    @NotBlank
    private String content;
    @NotNull
    private User author;
    
    private long createdAt;

    public Comments(@NotBlank String content, @NotNull User author, long createdAt) {
        this.content = content;
        this.author = author;
        this.createdAt = createdAt;
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public User getAuthor() {
        return author;
    }
    public void setAuthor(User author) {
        this.author = author;
    }
    public long getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }
}
