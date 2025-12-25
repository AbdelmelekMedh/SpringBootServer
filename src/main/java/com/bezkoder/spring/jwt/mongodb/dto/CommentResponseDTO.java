package com.bezkoder.spring.jwt.mongodb.dto;

import java.time.Instant;

import com.bezkoder.spring.jwt.mongodb.models.ImageProfile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentResponseDTO {

    private String id;
    private String content;
    private String authorId;
    private String authorName;
    private ImageProfile authorAvatar;
    private String parentCommentId;
    private Instant createdAt;
    private Instant updatedAt;
}
