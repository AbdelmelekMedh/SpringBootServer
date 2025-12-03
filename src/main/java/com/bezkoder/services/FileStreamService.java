package com.bezkoder.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;

import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.core.io.Resource;

import com.bezkoder.dto.ResourceFileStreamDTO;
import com.bezkoder.spring.jwt.mongodb.Exceptions.FileStreamNotFoundException;
import com.bezkoder.spring.jwt.mongodb.Exceptions.ForbiddenMimeTypeException;
import com.bezkoder.spring.jwt.mongodb.Exceptions.UserNotFoundException;
import com.bezkoder.spring.jwt.mongodb.models.ResourceFileStream;
import com.bezkoder.spring.jwt.mongodb.models.User;
import com.bezkoder.spring.jwt.mongodb.repository.FileStreamRepository;
import com.bezkoder.spring.jwt.mongodb.repository.UserRepository;
import com.jetbrains.exported.JBRApi.Service;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class FileStreamService {

    private final FileStreamRepository fileStreamRepository;
    private final UserRepository userRepository;

    private static final Path VIDEO_DIR = Paths.get("resources/static/videos");
    private static final Path AUDIO_DIR = Paths.get("resources/static/audios");

    public void uploadFile(String authorId, MultipartFile file) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new UserNotFoundException("Author not found: " + authorId));

        String mime = file.getContentType();
        boolean isVideo = "video/mp4".equalsIgnoreCase(mime);
        boolean isAudio = "audio/mpeg".equalsIgnoreCase(mime);

        if (!isVideo && !isAudio) {
            throw new ForbiddenMimeTypeException("Only MP4 videos and MP3 audios are allowed");
        }

        Path rootFolder = isVideo ? VIDEO_DIR : AUDIO_DIR;
        Path userFolder = rootFolder.resolve(authorId);
        createFolderIfMissing(userFolder);

        String savedFileName = LocalDate.now() + "_" + file.getOriginalFilename();
        Path savedFilePath = userFolder.resolve(savedFileName);

        try {
            Files.copy(file.getInputStream(), savedFilePath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file", e);
        }

        ResourceFileStream fileStream = new ResourceFileStream(
                file.getOriginalFilename(),
                savedFileName,
                null,
                savedFilePath.toString(),
                author
        );

        fileStreamRepository.save(fileStream);
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

