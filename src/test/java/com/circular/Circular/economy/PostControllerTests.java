package com.circular.Circular.economy;

import com.circular.Circular.economy.controller.PostController;
import com.circular.Circular.economy.dto.PostDTO;
import com.circular.Circular.economy.dto.geocoding.Coordinates;
import com.circular.Circular.economy.entity.Post;
import com.circular.Circular.economy.entity.ResourceType;
import com.circular.Circular.economy.service.DropboxService;
import com.circular.Circular.economy.service.GeocodingService;
import com.circular.Circular.economy.service.PostService;
import com.dropbox.core.DbxException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTests {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;
    @Mock
    private GeocodingService geocodingService;
    @Mock
    private DropboxService dropboxService;
    @Test
    void testGetPosts_Success() throws DbxException {
        // Mocking
        when(postService.getPosts()).thenReturn(Collections.singletonList(new PostDTO()));

        // Test the method
        ResponseEntity<?> responseEntity = postController.getPosts();

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }

    @Test
    void testGetPosts_EmptyList() throws DbxException {
        // Mocking
        when(postService.getPosts()).thenReturn(Collections.emptyList());

        // Test the method
        ResponseEntity<?> responseEntity = postController.getPosts();

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
    @Test
    void testDeletePost_Success() {
        // Mocking
        when(postService.deletePosts(anyList(), anyString())).thenReturn(true);

        // Test the method
        ResponseEntity<String> responseEntity = postController.deletePosts(Collections.singletonList(1L), "testToken");

        // Assertions
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());
    }
    @Test
    void testCreateNewPost_Success() throws IOException, DbxException {
        // Mocking
        when(postService.addNewPost(any(), anyString())).thenReturn(new Post());

        // Test the method
        ResponseEntity<?> responseEntity = postController.createNewPost("Red bricks", "Very old red bricks","Washington stree",15f,"BRICKS",null, "testToken");

        // Assertions
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());
    }


    @Test
    void testUpdatePostPartial_Success() {
       /*when(postService.updatePost(any())).thenReturn(new Post());

        // Test the method
        ResponseEntity<?> responseEntity = postController.updatePostPartial(1L, new PostDTO());

        // Assertions
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());*/
    }

}
