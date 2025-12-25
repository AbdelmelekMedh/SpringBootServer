package com.bezkoder.spring.jwt.mongodb.models;

import java.time.Instant;

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

    // null → root comment, not null → reply
    private String parentCommentId;

    private Instant createdAt = Instant.now();
    private Instant updatedAt;
}
