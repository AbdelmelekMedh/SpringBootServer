package com.bezkoder.spring.jwt.mongodb.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import com.bezkoder.spring.jwt.mongodb.dto.ResourceFileStreamDTO;
import com.bezkoder.spring.jwt.mongodb.payload.response.MessageResponse;
import com.bezkoder.spring.jwt.mongodb.security.services.UserDetailsImpl;
import com.bezkoder.spring.jwt.mongodb.services.FileStreamService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/video")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class StreamVideoController {

    private final FileStreamService fileStreamService;

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> upload(
        @RequestParam("author_id") String authorId,
        @RequestParam("file") MultipartFile file,
        @RequestParam(required = false) String description,
        @RequestParam(required = false) List<String> tags) {

        fileStreamService.uploadFile(authorId, file, description, tags);
        return ResponseEntity.ok(new MessageResponse("File uploaded successfully"));
    }

    @PutMapping("/update/{id}")
public ResponseEntity<MessageResponse> update(
        @PathVariable String id,
        @RequestParam(required = false) String description,
        @RequestParam(required = false) List<String> tags,
        @RequestParam(required = false) Boolean isPublic
) {
    fileStreamService.updateFile(id, description, tags, isPublic);
    return ResponseEntity.ok(new MessageResponse("File updated successfully"));
}

    @GetMapping("/public")
    public ResponseEntity<List<ResourceFileStreamDTO>> getPublicVideos(
        @RequestParam(defaultValue = "3") int limit) {

    return ResponseEntity.ok(fileStreamService.getPublicVideos(limit));
    }

    @GetMapping("/feed")
    public ResponseEntity<List<ResourceFileStreamDTO>> getFeed(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @AuthenticationPrincipal UserDetailsImpl user
    ) {
    return ResponseEntity.ok(fileStreamService.getFeed(page, size, user.getId()));
    }


    @GetMapping("/{id}")
public ResponseEntity<ResourceFileStreamDTO> getById(
    @PathVariable String id,
    @AuthenticationPrincipal UserDetailsImpl user
) {
    return ResponseEntity.ok(
        fileStreamService.getFileById(id, user.getId())
    );
}


    @GetMapping("/author")
    public ResponseEntity<List<ResourceFileStreamDTO>> getByAuthor(@RequestParam("id") String authorId) {
        return ResponseEntity.ok(fileStreamService.getFilesByAuthor(authorId));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ResourceFileStreamDTO>> search(@RequestParam("keyword") String keyword) {
        return ResponseEntity.ok(fileStreamService.searchFiles(keyword));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String id, HttpServletRequest request) {

        return fileStreamService.downloadFile(id, request);
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<MessageResponse> delete(@PathVariable String id) {
        fileStreamService.deleteFile(id);
        return ResponseEntity.ok(new MessageResponse("File deleted successfully"));
    }

    @PostMapping("/{id}/like")
public ResponseEntity<?> likeFile(
  @PathVariable String id,
  @AuthenticationPrincipal UserDetailsImpl user
) {
  fileStreamService.toggleLikeFile(id, user.getId());
  return ResponseEntity.ok().build();
}

}
