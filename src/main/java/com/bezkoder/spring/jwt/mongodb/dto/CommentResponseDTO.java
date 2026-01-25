package com.bezkoder.spring.jwt.mongodb.dto;

import java.time.Instant;

import com.bezkoder.spring.jwt.mongodb.models.ImageProfile;

import lombok.Data;

@Data
public class CommentResponseDTO {

    private String id;
    private String content;
    private String authorId;
    private String authorName;
    private ImageProfile authorAvatar;
    private String parentCommentId;
    private Instant createdAt;
    private Instant updatedAt;
    private int likeCount;
    private boolean isLiked;
    
    public CommentResponseDTO(String id, String content, String authorId, String authorName, ImageProfile authorAvatar, String parentCommentId, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorAvatar = authorAvatar;
        this.parentCommentId = parentCommentId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.likeCount = 0;
        this.isLiked = false;
    }

    public CommentResponseDTO(String id, String content, String authorId, String authorName, ImageProfile authorAvatar, String parentCommentId, Instant createdAt, Instant updatedAt, int likeCount, boolean isLiked) {
        this.id = id;
        this.content = content;
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorAvatar = authorAvatar;
        this.parentCommentId = parentCommentId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.likeCount = likeCount;
        this.isLiked = isLiked;
    }

}
