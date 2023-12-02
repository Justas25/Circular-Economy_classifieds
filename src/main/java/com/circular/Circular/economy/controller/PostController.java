package com.circular.Circular.economy.controller;

import com.circular.Circular.economy.entity.Post;
import com.circular.Circular.economy.entity.ResourceType;
import com.circular.Circular.economy.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.circular.Circular.economy.service.PostService;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path="api/v1/post")
public class PostController {
    private final PostService postService;
    @Autowired //tells that variable postService has to be instantiated an injected into this constructor
    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping //rest endpoint //default, all posts
    public ResponseEntity<List<Post>> getPosts() {
        try{
            if(postService.getPosts().isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);  //204
                //return new ResponseEntity<>.ok().body(postService.all());
            }
            return new ResponseEntity<>(postService.getPosts(),HttpStatus.OK); //200
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); //500
        }
    }
  @GetMapping(path = "/id={postId}") //post by id
    public ResponseEntity<Optional> getPost(@PathVariable("postId") Long postId) {
        try{
            if(postService.getPostById(postId).isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT); //204
            }
            return new ResponseEntity<>(postService.getPostById(postId),HttpStatus.OK); //200
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); //500
        }
    }

    @GetMapping(path = "/{name}") //posts filtered by ResourceType
    public ResponseEntity<List<Post>> findByResourceType(@PathVariable String name){
        try {
            List<Post> allPosts = postService.getPosts();

            if (allPosts.isEmpty()) return new ResponseEntity<>(HttpStatus.NOT_FOUND); //404
            List<Post> filteredList = allPosts.stream().filter(post -> post.getResourceType().name().equals(name))
                    .collect(Collectors.toList());
            System.out.println(filteredList);
            return ResponseEntity.ok().body(filteredList);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); //500
        }

    }

    @PostMapping
    public void createNewPost(@RequestBody Post post) {
        postService.addNewPost(post);
        //return new ResponseEntity<>(post,HttpStatus.CREATED); //201
    }


    @DeleteMapping(path = "{postId}")
    public ResponseEntity<String> deletePost(@PathVariable("postId") Long postId) { //ar ResponseEntity<Long>
        var isRemoved = postService.deletePost(postId);
        if (!isRemoved) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND); //404
        }
        return new ResponseEntity<>("Post " +postId +" is deleted",HttpStatus.NO_CONTENT); //204
    }


    @PutMapping(path = "{postId}")
    public ResponseEntity<Optional<Post>> updatePost(
            @PathVariable("postId") Long postId,
            @RequestParam(required=false) String title,
            @RequestParam(required=false) String description
            ) {
                try {
                    if(postService.updatePost(postId,title,description))
                    return new ResponseEntity<>(postService.getPostById(postId),HttpStatus.OK); //200
                    else return new ResponseEntity<>(HttpStatus.BAD_REQUEST); //400
                }
                catch (Exception e){
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); //500
                }

    }

    @PatchMapping(path = "{postId}")  //turi buti 200
    public ResponseEntity<Optional<Post>> updatePostPartial(
            @PathVariable("postId") Long postId,
            @RequestParam(required=false) String title,
            @RequestParam(required=false) String description
    ) {
        try {
            if(postService.updatePost(postId,title,description))
                return new ResponseEntity<>(postService.getPostById(postId),HttpStatus.OK); //200
            else return new ResponseEntity<>(HttpStatus.BAD_REQUEST); //400
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR); //500
        }

    }





    
}
