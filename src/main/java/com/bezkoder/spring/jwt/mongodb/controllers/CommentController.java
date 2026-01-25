package com.bezkoder.spring.jwt.mongodb.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bezkoder.spring.jwt.mongodb.dto.CommentRequestDTO;
import com.bezkoder.spring.jwt.mongodb.dto.CommentResponseDTO;
import com.bezkoder.spring.jwt.mongodb.payload.response.MessageResponse;
import com.bezkoder.spring.jwt.mongodb.services.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments/{videoId}")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/addComment")
    public ResponseEntity<?> add(
            @PathVariable String videoId,
            @RequestParam String userId,
            @RequestBody CommentRequestDTO dto) {

        commentService.addComment(videoId, userId, dto);
        return ResponseEntity.ok(new MessageResponse("Comment added"));
    }

    @GetMapping
    public ResponseEntity<List<CommentResponseDTO>> getComments(@PathVariable String videoId, @RequestParam(required = false) String userId) {
    List<CommentResponseDTO> comments = commentService.getCommentsByVideoId(videoId, userId);
    return ResponseEntity.ok(comments);
    }

    @PostMapping("/{commentId}/like")
    public ResponseEntity<?> likeComment(@PathVariable String videoId, @PathVariable String commentId, @RequestParam String userId) {
        commentService.toggleLikeComment(videoId, commentId, userId);
        return ResponseEntity.ok(new MessageResponse("Toggled like"));
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<?> update(
            @PathVariable String videoId,
            @PathVariable String commentId,
            @RequestParam String userId,
            @RequestBody CommentRequestDTO dto) {

        commentService.updateComment(videoId, commentId, userId, dto.getContent());
        return ResponseEntity.ok(new MessageResponse("Comment updated"));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> delete(
            @PathVariable String videoId,
            @PathVariable String commentId,
            @RequestParam String userId) {

        commentService.deleteComment(videoId, commentId, userId);
        return ResponseEntity.ok(new MessageResponse("Comment deleted"));
    }
}

