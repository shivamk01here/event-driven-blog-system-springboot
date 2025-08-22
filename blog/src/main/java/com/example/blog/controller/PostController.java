package com.example.blog.controller;

import com.example.blog.dto.PostDTO;
import com.example.blog.model.Post;
import com.example.blog.messaging.producer.PostViewProducer;
import com.example.blog.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final PostViewProducer postViewProducer;

    @PostMapping
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDto,
                                              @AuthenticationPrincipal UserDetails principal) {
        Post post = postService.createPost(postDto.getTitle(), postDto.getContent(), principal.getUsername());
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setAuthorName(post.getAuthor().getName());
        dto.setAuthorId(post.getAuthor().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @GetMapping
    public ResponseEntity<List<PostDTO>> getAll() {
        List<PostDTO> dtos = postService.getAllPosts().stream().map(post -> {
            PostDTO dto = new PostDTO();
            dto.setId(post.getId());
            dto.setTitle(post.getTitle());
            dto.setContent(post.getContent());
            dto.setAuthorId(post.getAuthor().getId());
            dto.setAuthorName(post.getAuthor().getName());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDTO> getOne(@PathVariable Long id) {
        Post post = postService.getPost(id);
        postViewProducer.sendViewEvent(id);
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setAuthorId(post.getAuthor().getId());
        dto.setAuthorName(post.getAuthor().getName());
        return ResponseEntity.ok(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDTO> update(@PathVariable Long id,
                                          @RequestBody PostDTO postDto, @AuthenticationPrincipal UserDetails principal) {
        Post post = postService.updatePost(id, postDto.getTitle(), postDto.getContent(), principal.getUsername());
        PostDTO dto = new PostDTO();
        dto.setId(post.getId());
        dto.setTitle(post.getTitle());
        dto.setContent(post.getContent());
        dto.setAuthorId(post.getAuthor().getId());
        dto.setAuthorName(post.getAuthor().getName());
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails principal) {
        postService.deletePost(id, principal.getUsername());
        return ResponseEntity.ok("Post deleted");
    }
}
