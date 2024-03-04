package com.circular.Circular.economy.dto;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
public record PostDTO(Long postId,
                      String postTitle,
                      String postDescription,
                      String address,
                      Float price,
                      String resourceTypeName,
                      MultipartFile imageFile,
                      String resourceTypeDescription,
                      Float latitude,
                      Float longitude,
                      String imageName,
                      String dropboxTemporaryLink,
                      String username,
                      String phoneNumber,
                      String email) implements Serializable {

    public PostDTO(Long postId, String postTitle, String postDescription, String address, Float price, String resourceTypeName, MultipartFile imageFile, String resourceTypeDescription, Float latitude, Float longitude, String imageName, String dropboxTemporaryLink, String username, String phoneNumber, String email) {
        this.postId = postId;
        this.postTitle = postTitle;
        this.postDescription = postDescription;
        this.address = address;
        this.price = price;
        this.resourceTypeName = resourceTypeName;
        this.imageFile = imageFile;
        this.resourceTypeDescription = resourceTypeDescription;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imageName = imageName;
        this.dropboxTemporaryLink = dropboxTemporaryLink;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public PostDTO(Long postId, String title, String description, String resourceTypeName,
                   String address, Float price, String username, Float latitude, Float longitude, String imageName, String phoneNumber,
                   String email) {

        this(postId, title, description, address,price, resourceTypeName,null,null,latitude,
                longitude,imageName,null, username ,phoneNumber,email);
    }
    public PostDTO(Long postId, String title, String description, String resourceTypeName,
                   String address, Float price, String username, Float latitude, Float longitude, String imageName, String phoneNumber,
                   String email,String dropboxTemporaryLink) {

        this(postId, title, description, address,price, resourceTypeName,null,null,latitude,
                longitude,imageName,dropboxTemporaryLink, username ,phoneNumber,email);
    }
    public PostDTO(Long postId, String title, String description, String resourceTypeName, String resourceTypeDescription, Float price,
                   String address, String username, Float latitude, Float longitude, String imageName) {
        this(postId, title, description, address,price, resourceTypeName,null,resourceTypeDescription,latitude,
                longitude,imageName,null, username ,null,null);
    }
    public PostDTO(String title, String description, String address, Float price, String resourceTypeName) {
        this(null, title, description, address,price, resourceTypeName,null,null,null,
                null,null,null, null ,null,null);
    }
    public PostDTO(String postTitle, String postDescription, String address, Float price, String resourceTypeName, MultipartFile imageFile) {
        this(null, postTitle, postDescription, address,price, resourceTypeName,imageFile,null,null,
                null,null,null, null ,null,null);
    }
    public PostDTO(String postTitle, String postDescription, String address, Float price, String resourceTypeName, String imageName) {
        this(null, postTitle, postDescription, address,price, resourceTypeName,null,null,null,
                null,imageName,null, null ,null,null);
    }
    public PostDTO() {
        this(null, null, null, null,null, null,null,null,null,
                null,null,null, null ,null,null);
    }

}