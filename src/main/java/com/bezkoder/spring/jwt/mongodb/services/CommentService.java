package com.bezkoder.spring.jwt.mongodb.services;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import com.bezkoder.spring.jwt.mongodb.Exceptions.FileStreamNotFoundException;
import com.bezkoder.spring.jwt.mongodb.Exceptions.UserNotFoundException;
import com.bezkoder.spring.jwt.mongodb.dto.CommentRequestDTO;
import com.bezkoder.spring.jwt.mongodb.dto.CommentResponseDTO;
import com.bezkoder.spring.jwt.mongodb.models.Comments;
import com.bezkoder.spring.jwt.mongodb.models.Profile;
import com.bezkoder.spring.jwt.mongodb.models.ResourceFileStream;
import com.bezkoder.spring.jwt.mongodb.models.User;
import com.bezkoder.spring.jwt.mongodb.repository.FileStreamRepository;
import com.bezkoder.spring.jwt.mongodb.repository.ProfileRepository;
import com.bezkoder.spring.jwt.mongodb.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final FileStreamRepository fileRepo;
    private final UserRepository userRepo;
        private final ProfileRepository profileRepo;

    // ADD COMMENT / REPLY
    public void addComment(String videoId, String userId, CommentRequestDTO dto) {

        ResourceFileStream video = fileRepo.findById(videoId)
                .orElseThrow(() -> new FileStreamNotFoundException("Video not found"));

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        Profile profile = profileRepo.findByUserId(userId).orElseThrow(() -> new UserNotFoundException("profile not found"));

        Comments comment = new Comments();
        comment.setId(new ObjectId().toString());
        comment.setContent(dto.getContent());
        comment.setAuthor(user);
        comment.setParentCommentId(dto.getParentCommentId());
        comment.setAuthorAvatar(profile.getImageProfile());
        

        video.getComments().add(comment);
        video.setCommentsCount(video.getCommentsCount() + 1);

        fileRepo.save(video);
    }

    public List<CommentResponseDTO> getCommentsByVideoId(String videoId) {
        return getCommentsByVideoId(videoId, null);
    }

    public List<CommentResponseDTO> getCommentsByVideoId(String videoId, String userId) {
        ResourceFileStream video = fileRepo.findById(videoId)
                .orElseThrow(() -> new FileStreamNotFoundException("Video not found: " + videoId));

        return video.getComments()
                .stream()
                .map(c -> new CommentResponseDTO(
                        c.getId(),
                        c.getContent(),
                        c.getAuthor().getId(),
                        c.getAuthor().getUsername(),
                        c.getAuthorAvatar(),
                        c.getParentCommentId(),
                        c.getCreatedAt(),
                        c.getUpdatedAt(),
                        c.getLikedBy() != null ? c.getLikedBy().size() : 0,
                        userId != null && c.getLikedBy() != null && c.getLikedBy().contains(userId)
                ))
                .collect(Collectors.toList());
    }

    public void toggleLikeComment(String videoId, String commentId, String userId) {
        ResourceFileStream video = fileRepo.findById(videoId)
                .orElseThrow(() -> new FileStreamNotFoundException("Video not found"));

        Comments comment = video.getComments().stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (comment.getLikedBy() == null) {
            comment.setLikedBy(new java.util.ArrayList<>());
        }

        if (comment.getLikedBy().contains(userId)) {
            comment.getLikedBy().remove(userId);
            comment.setLikeCount(Math.max(0, comment.getLikeCount() - 1));
        } else {
            comment.getLikedBy().add(userId);
            comment.setLikeCount(comment.getLikeCount() + 1);
        }

        comment.setUpdatedAt(Instant.now());
        fileRepo.save(video);
    }


    // UPDATE COMMENT (ONLY AUTHOR)
    public void updateComment(String videoId, String commentId, String userId, String content) {

        ResourceFileStream video = fileRepo.findById(videoId)
                .orElseThrow(() -> new FileStreamNotFoundException("Video not found"));

        Comments comment = video.getComments().stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("You can update only your own comment");
        }

        comment.setContent(content);
        comment.setUpdatedAt(Instant.now());

        fileRepo.save(video);
    }

    // DELETE COMMENT (ONLY AUTHOR)
    public void deleteComment(String videoId, String commentId, String userId) {

        ResourceFileStream video = fileRepo.findById(videoId)
                .orElseThrow(() -> new FileStreamNotFoundException("Video not found"));

        Comments comment = video.getComments().stream()
                .filter(c -> c.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        if (!comment.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("You can delete only your own comment");
        }

        // remove comment + its replies
        video.getComments().removeIf(c ->
                c.getId().equals(commentId) ||
                commentId.equals(c.getParentCommentId())
        );

        video.setCommentsCount(video.getComments().size());
        fileRepo.save(video);
    }
}
