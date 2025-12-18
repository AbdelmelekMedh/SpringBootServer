package com.bezkoder.spring.jwt.mongodb.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.bezkoder.spring.jwt.mongodb.models.ImageProfile;
import com.bezkoder.spring.jwt.mongodb.models.Profile;
import com.bezkoder.spring.jwt.mongodb.services.FileStorageService;
import com.bezkoder.spring.jwt.mongodb.services.ProfileService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profile") // Base URL path for profile operations
@RequiredArgsConstructor
public class ProfileController {
    
    private final ProfileService profileService;
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping
    public ResponseEntity<Profile> createProfile(@Valid @RequestBody Profile profile) {
        try {
            Profile createdProfile = profileService.createProfile(profile);
            return new ResponseEntity<>(createdProfile, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            // Catches the "User not found" exception from the service layer
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Profile> getProfileByUserId(@PathVariable String userId) {
        Optional<Profile> profileOpt = profileService.getProfileByUserId(userId);
        return profileOpt.map(profile -> new ResponseEntity<>(profile, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(
    value = "/{userId}",
    consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Profile> updateProfile(
        @PathVariable String userId,
        @RequestPart("profile") @Valid Profile updatedProfile,
        @RequestPart(value = "image", required = false) MultipartFile imageFile
    ) {
        try {
            
            if (imageFile != null && !imageFile.isEmpty()) {
            String imageUrl = fileStorageService.save(imageFile); 
            ImageProfile image = new ImageProfile();
            image.setFilePathUrl(imageUrl);
            updatedProfile.setImageProfile(image);
            }

            Profile result = profileService.updateProfile(userId, updatedProfile);
            return new ResponseEntity<>(result, HttpStatus.OK);
            
        } catch (RuntimeException e) {
            // Catches the "Profile not found" exception
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<HttpStatus> deleteProfile(@PathVariable String userId) {
        try {
            profileService.deleteProfileByUserId(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 success with no content to return
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
