package com.bezkoder.spring.jwt.mongodb.dto;

import java.time.Instant;
import java.util.List;

import com.bezkoder.spring.jwt.mongodb.models.Comments;
import com.bezkoder.spring.jwt.mongodb.models.ResourceFileStream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceFileStreamDTO {

    private String id;
    private String filename;
    private String storedName;
    private String authorId;
    private String authorName;
    private String path;
    private int downloads;
    private int shares;
    private int views;
    private int likes;
    private int commentsCount;
    private String description;
    private boolean isPublic;
    private List<String> tags;
    private List<Comments> comments;
    private Instant createdAt;

    public static ResourceFileStreamDTO fromEntity(ResourceFileStream file) {
        return new ResourceFileStreamDTO(
                file.getId(),
                file.getFilename(),
                file.getStoredName(),
                file.getAuthor().getId(),
                file.getAuthor().getUsername(),
                file.getPath(),
                file.getDownloads(),
                file.getShares(),
                file.getViews(),
                file.getLikes(),
                file.getCommentsCount(),
                file.getDescription(),
                file.isPublic(),
                file.getTags(),
                file.getComments(),
                file.getCreatedAt()
        );
    }
}

