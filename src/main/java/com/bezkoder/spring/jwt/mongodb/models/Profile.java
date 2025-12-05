package com.bezkoder.spring.jwt.mongodb.models;

import java.util.Date;
import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "profiles")
public class Profile {
    @Id
    private String id;

    private String userId; // Link to the User document

    @Size(max = 50)
    private String Name;

    @Size(max = 100)
    private String address;

    @Size(max = 15)
    private String phoneNumber;

    @Size(max = 500)
    private String bio;

    @Size(max = 20)
    private String gender;

    private Date dateOfBirth;

    private ImageProfile imageProfile;

    private Date createdAt;

    private Date updatedAt;

    private boolean isActive;

    private Map<String, String> socialLinks;

    private List<String> interests;

    private List<String> languages;

    // Constructors, Getters, and Setters
    public Profile() {
    }

    public Profile(String userId, String name, String address, String phoneNumber, String bio,String gender, Date dateOfBirth,
                   ImageProfile imageProfile, Date createdAt, Date updatedAt, boolean isActive,
                   Map<String, String> socialLinks, List<String> interests, List<String> languages) {
        this.userId = userId;
        Name = name;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.bio = bio;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.imageProfile = imageProfile;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isActive = isActive;
        this.socialLinks = socialLinks;
        this.interests = interests;
        this.languages = languages;
    }   

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    public String getBio() {
        return bio;
    }
    public void setBio(String bio) {
        this.bio = bio;
    }
    public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }
    public ImageProfile getImageProfile() {
        return imageProfile;
    }
    public void setImageProfile(ImageProfile imageProfile) {
        this.imageProfile = imageProfile;
    }
    public Date getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public Date getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean isActive) {
        this.isActive = isActive;
    }
    public Map<String, String> getSocialLinks() {
        return socialLinks;
    }
    public void setSocialLinks(Map<String, String> socialLinks) {
        this.socialLinks = socialLinks;
    }
    public List<String> getInterests() {
        return interests;
    }
    public void setInterests(List<String> interests) {
        this.interests = interests;
    }
    public List<String> getLanguages() {
        return languages;
    }
    public void setLanguages(List<String> languages) {
        this.languages = languages;
    }
    
}