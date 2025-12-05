package com.bezkoder.spring.jwt.mongodb.services;

import org.springframework.stereotype.Service;

import com.bezkoder.spring.jwt.mongodb.models.Profile;
import com.bezkoder.spring.jwt.mongodb.repository.ProfileRepository;
import com.bezkoder.spring.jwt.mongodb.repository.UserRepository;
import java.util.Optional;  
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final UserRepository userRepository; // To ensure user existence

    public Profile createProfile(Profile profile) {
        // Ensure the associated user exists
        if (userRepository.existsById(profile.getUserId())) {
            Optional<Profile> existing = profileRepository.findByUserId(profile.getUserId());
            if (existing.isPresent()) {
                throw new RuntimeException("This user already has a profile");
            }
            return profileRepository.save(profile);
        } else {
            throw new RuntimeException("User with ID " + profile.getUserId() + " not found.");
        }
    }

    public Optional<Profile> getProfileByUserId(String userId) {
        return profileRepository.findByUserId(userId);
    }

    public Profile updateProfile(String userId, Profile updatedProfile) {
        return profileRepository.findByUserId(userId).map(profile -> {
            profile.setName(updatedProfile.getName());
            profile.setAddress(updatedProfile.getAddress());
            profile.setPhoneNumber(updatedProfile.getPhoneNumber());
            profile.setBio(updatedProfile.getBio());
            profile.setGender(updatedProfile.getGender());
            profile.setDateOfBirth(updatedProfile.getDateOfBirth());
            profile.setImageProfile(updatedProfile.getImageProfile());
            profile.setSocialLinks(updatedProfile.getSocialLinks());
            profile.setInterests(updatedProfile.getInterests());
            profile.setLanguages(updatedProfile.getLanguages());
            profile.setUpdatedAt(updatedProfile.getUpdatedAt());
            return profileRepository.save(profile);
        }).orElseThrow(() -> new RuntimeException("Profile not found for user ID: " + userId));
    }

    public void deleteProfileByUserId(String userId) {
        profileRepository.findByUserId(userId).ifPresent(profileRepository::delete);
    }
}
