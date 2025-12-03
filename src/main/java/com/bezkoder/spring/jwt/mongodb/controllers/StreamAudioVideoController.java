package com.bezkoder.spring.jwt.mongodb.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;


import com.bezkoder.dto.ResourceFileStreamDTO;
import com.bezkoder.services.FileStreamService;
import com.bezkoder.spring.jwt.mongodb.payload.response.MessageResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/audiovideo")
@CrossOrigin(origins = "*", maxAge = 3600)
@RequiredArgsConstructor
public class StreamAudioVideoController {

    private final FileStreamService fileStreamService;

    @PostMapping("/upload")
    public ResponseEntity<MessageResponse> upload(
            @RequestParam("author_id") String authorId,
            @RequestParam("file") MultipartFile file) {

        fileStreamService.uploadFile(authorId, file);
        return ResponseEntity.ok(new MessageResponse("File uploaded successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceFileStreamDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(fileStreamService.getFileById(id));
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
}
