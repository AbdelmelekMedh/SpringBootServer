package com.bezkoder.spring.jwt.mongodb.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.core.mapping.Document;

@EnableMongoAuditing
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "resources")
public class ResourceFileStream {

    @Id
    private String id;

    @NotBlank
    @Size(min = 2)
    private String filename;

    @NotBlank
    private String storedName;

    private List<String> tags = new ArrayList<>();

    @NotBlank
    private String path;

    @NotNull
    private User author;

    @NotBlank
    private String description;

    private boolean isPublic = true;

    private int views = 0;
    private int likes = 0;
    private int commentsCount = 0;

    private List<Comments> comments = new ArrayList<>();

    private int shares = 0;
    private int downloads = 0;

    @CreatedDate
    private Instant createdAt;
}