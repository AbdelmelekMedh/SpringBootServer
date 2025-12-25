package com.bezkoder.spring.jwt.mongodb.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CommentRequestDTO {

    @NotBlank
    private String content;

    // optional → reply
    private String parentCommentId;
}
