package com.circular.Circular.economy.controller;

import com.circular.Circular.economy.dto.PostDTO;
import com.circular.Circular.economy.entity.Post;
import com.circular.Circular.economy.entity.ResourceType;
import com.circular.Circular.economy.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@CrossOrigin(origins = "http://localhost:3030")
@RestController
@RequestMapping(path="api/v1/post")
public class PostController {
    private final PostService postService;

    @Autowired //instantiate postService variable and inject into constructor
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping //default, all posts
    public ResponseEntity<?> getPosts() {
        try{
            if(postService.getPosts().isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);  //error 204
            }
            return ResponseEntity.ok().body(Map.of("data", postService.getPosts()));
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); //error 500
        }
    }

    @GetMapping("/personal")
    public ResponseEntity<?> getPersonalPosts(@RequestHeader(name = "Authorization") String token) {
        try {
            List<PostDTO> userPosts = postService.getPersonalPosts(token);
            if (userPosts.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); //204
            }
            return ResponseEntity.ok().body(Map.of("data", userPosts));
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); //500
        }
    }
    @GetMapping(path = "/id={postId}") //post by id
    public ResponseEntity<?> getPost(@PathVariable("postId") Long postId) {
        try{
            Optional<PostDTO> post = postService.getPostDTOById(postId);
            if(post.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); //204
            }
            return new ResponseEntity<>(post,HttpStatus.OK); //200
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); //500
        }
    }

    @GetMapping(path = "/{name}") //posts filtered by ResourceType
    public ResponseEntity<?> findByResourceType(@PathVariable String name){
        try {
            List<PostDTO> allPosts = postService.getPosts();
            if (allPosts.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND); //404
            List<PostDTO> filteredList = allPosts.stream().filter(post -> post.resourceTypeName().equals(name))
                    .toList();
            int index = Arrays.asList(ResourceType.values()).indexOf(ResourceType.valueOf(name));
            return ResponseEntity.ok().body(Map.of("data", filteredList, "index", index));
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); //500
        }
    }

    @PostMapping
    public ResponseEntity<?> createNewPost(
            //@ModelAttribute PostDTO postDTO,
            //Spring has trouble with automatic binding of request parameters to the RECORDS fields of model attribute object
            //need to specify parameters explicitly
            @RequestParam("postTitle") String postTitle,
            @RequestParam("postDescription") String postDescription,
            @RequestParam("address") String address,
            @RequestParam("price") Float price,
            @RequestParam("resourceTypeName") String resourceTypeName,
            @RequestParam(name = "imageFile",required = false) MultipartFile imageFile,
            @RequestHeader(name = "Authorization") String token
    ) {
        // Constructing the PostDTO object manually
        PostDTO postDTO;
        if (imageFile!=null) postDTO = new PostDTO(postTitle, postDescription, address, price, resourceTypeName,imageFile);
        else postDTO = new PostDTO(postTitle, postDescription, address, price, resourceTypeName);
        try {
            Post savedPost = postService.addNewPost(postDTO, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("post", savedPost));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Error creating a new post!"));
        }
    }


    @DeleteMapping(path = "{postId}")
    public ResponseEntity<?> deletePost(@PathVariable("postId") Long postId,
                                        @RequestHeader(name = "Authorization") String token) {
        var isRemoved = postService.deletePost(postId, token);
        if (!isRemoved) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Error deleting post"));
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT); //204
    }

    @DeleteMapping("/delete-multiple")
    public ResponseEntity<String> deletePosts(@RequestBody List<Long> postIds,
                                              @RequestHeader("Authorization") String token) {

        boolean allDeleted = postService.deletePosts(postIds, token);
        if (allDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access or some posts not found.");
        }
    }
    @PutMapping(path = "{postId}")
    public ResponseEntity<?> updatePostPartial(
            @PathVariable Long postId,
            @ModelAttribute PostDTO postDTO
    ) {
        try {
            Optional<Post> post = Optional.ofNullable(postService.updatePost(postDTO,postId));
            if(post.isEmpty()){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
            else{
                return new ResponseEntity<>(post,HttpStatus.OK); //200
            }
        }
        catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); //500
        }

    }
}
