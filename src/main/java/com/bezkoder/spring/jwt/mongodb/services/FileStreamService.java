package com.bezkoder.spring.jwt.mongodb.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.core.io.Resource;

import com.bezkoder.spring.jwt.mongodb.Exceptions.FileStreamNotFoundException;
import com.bezkoder.spring.jwt.mongodb.Exceptions.ForbiddenMimeTypeException;
import com.bezkoder.spring.jwt.mongodb.Exceptions.UserNotFoundException;
import com.bezkoder.spring.jwt.mongodb.dto.ResourceFileStreamDTO;
import com.bezkoder.spring.jwt.mongodb.models.ResourceFileStream;
import com.bezkoder.spring.jwt.mongodb.models.User;
import com.bezkoder.spring.jwt.mongodb.repository.FileStreamRepository;
import com.bezkoder.spring.jwt.mongodb.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class FileStreamService {

    private final FileStreamRepository fileStreamRepository;
    private final UserRepository userRepository;

    private static final Path VIDEO_DIR = Paths.get("resources/static/videos");

    public void uploadFile(String authorId, MultipartFile file, String description, List<String> tags) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new UserNotFoundException("Author not found: " + authorId));

        String mime = file.getContentType();
        boolean isVideo = "video/mp4".equalsIgnoreCase(mime);

        if (!isVideo) {
            throw new ForbiddenMimeTypeException("Only MP4 videos are allowed");
        }

        Path userFolder = VIDEO_DIR.resolve(authorId);
        createFolderIfMissing(userFolder);

        String savedFileName = LocalDate.now() + "_" + file.getOriginalFilename();
        Path savedFilePath = userFolder.resolve(savedFileName);

        try {
            Files.copy(file.getInputStream(), savedFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }

        ResourceFileStream fileStream = new ResourceFileStream();
        fileStream.setFilename(file.getOriginalFilename());
        fileStream.setStoredName(savedFileName);
        fileStream.setPath(savedFilePath.toString());
        fileStream.setAuthor(author);
        fileStream.setDescription(description);

        if (tags != null) {
        fileStream.getTags().addAll(tags);
        }

        fileStreamRepository.save(fileStream);
    }

    public void updateFile(String id, String description, List<String> tags, Boolean isPublic) {

    ResourceFileStream file = fileStreamRepository.findById(id)
            .orElseThrow(() -> new FileStreamNotFoundException("File not found: " + id));

    if (description != null) {
        file.setDescription(description);
    }

    if (tags != null) {
        file.getTags().clear();
        file.getTags().addAll(tags);
    }
    if (isPublic != null) {
        file.setPublic(isPublic);
    }

    fileStreamRepository.save(file);
}

    public List<ResourceFileStreamDTO> getPublicVideos(int limit) {
    return fileStreamRepository.findAll()
            .stream()
            .limit(limit)
            .map(ResourceFileStreamDTO::fromEntity)
            .toList();
}

    public List<ResourceFileStreamDTO> getFeed(int page, int size) {
    Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));

    return fileStreamRepository
            .findByIsPublicTrue(pageable)
            .getContent()
            .stream()
            .map(ResourceFileStreamDTO::fromEntity)
            .toList();
    }


    private void createFolderIfMissing(Path folder) {
        try {
            Files.createDirectories(folder);
        } catch (IOException e) {
            throw new RuntimeException("Cannot create folder: " + folder, e);
        }
    }

    // Get file metadata
    public ResourceFileStreamDTO getFileById(String id) {
        ResourceFileStream file = fileStreamRepository.findById(id)
                .orElseThrow(() -> new FileStreamNotFoundException("File not found: " + id));

        return ResourceFileStreamDTO.fromEntity(file);
    }

    // Search
    public List<ResourceFileStreamDTO> searchFiles(String keyword) {
        List<ResourceFileStream> files =
                keyword == null || keyword.isEmpty()
                        ? fileStreamRepository.findAll()
                        : fileStreamRepository.findByFilenameContaining(keyword);

        return files.stream().map(ResourceFileStreamDTO::fromEntity).toList();
    }

    // List by author
    public List<ResourceFileStreamDTO> getFilesByAuthor(String authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + authorId));

        return fileStreamRepository.findByAuthor(author)
                .orElseThrow(() -> new RuntimeException("No files for author"))
                .stream()
                .map(ResourceFileStreamDTO::fromEntity)
                .toList();
    }

    // Download
    public ResponseEntity<Resource> downloadFile(String id, HttpServletRequest request) {
        ResourceFileStream file = fileStreamRepository.findById(id)
                .orElseThrow(() -> new FileStreamNotFoundException("File not found: " + id));

        Path path = Paths.get(file.getPath());

        try {
            UrlResource resource = new UrlResource(path.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new FileStreamNotFoundException("Unreadable file: " + id);
            }

            String mimeType = request.getServletContext()
                    .getMimeType(resource.getFile().getAbsolutePath());

            if (mimeType == null) {
                mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(mimeType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);

        } catch (IOException e) {
            throw new RuntimeException("Could not read file", e);
        }
    }

    // Delete
    public void deleteFile(String id) {
        ResourceFileStream file = fileStreamRepository.findById(id)
                .orElseThrow(() -> new FileStreamNotFoundException("File not found: " + id));

        try {
            Files.deleteIfExists(Paths.get(file.getPath()));
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file", e);
        }

        fileStreamRepository.delete(file);
    }
}

