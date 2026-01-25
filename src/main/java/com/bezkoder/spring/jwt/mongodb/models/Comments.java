package com.bezkoder.spring.jwt.mongodb.models;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Comments {

    @Id
    private String id;

    @NotBlank
    private String content;

    @NotNull
    private User author;

    @NotBlank
    private ImageProfile authorAvatar;

    private String parentCommentId;

    private Instant createdAt = Instant.now();

    private Instant updatedAt;

    private int likeCount = 0;
    
    private List<String> likedBy = new ArrayList<>();

    private boolean isLiked = false;
}
