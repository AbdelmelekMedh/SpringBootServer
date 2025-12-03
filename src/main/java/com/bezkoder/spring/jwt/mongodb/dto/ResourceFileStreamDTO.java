package com.bezkoder.spring.jwt.mongodb.dto;

import com.bezkoder.spring.jwt.mongodb.models.ResourceFileStream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceFileStreamDTO {

    private String id;
    private String filename;
    private String storedName;
    private String authorId;
    private String path;
    

    public static ResourceFileStreamDTO fromEntity(ResourceFileStream file) {
        return new ResourceFileStreamDTO(
                file.getId(),
                file.getFilename(),
                file.getStoredName(),
                file.getAuthor().getId(),
                file.getPath()
        );
    }
}

