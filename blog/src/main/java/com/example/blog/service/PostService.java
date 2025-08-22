package com.example.blog.service;

import com.example.blog.model.*;
import com.example.blog.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepo;
    private final UserRepository userRepo;

    public Post createPost(String title, String content, String email) {
        User author = userRepo.findByEmail(email).orElseThrow();
        Post post = Post.builder().title(title).content(content).author(author).build();
        return postRepo.save(post);
    }
    public List<Post> getAllPosts() {
        return postRepo.findAll();
    }
    public Post getPost(Long id) {
        return postRepo.findById(id).orElseThrow();
    }
    public Post updatePost(Long id, String title, String content, String email) {
        Post post = postRepo.findById(id).orElseThrow();
        if (!post.getAuthor().getEmail().equals(email)) throw new RuntimeException("Unauthorized");
        post.setTitle(title);
        post.setContent(content);
        return postRepo.save(post);
    }
    public void deletePost(Long id, String email) {
        Post post = postRepo.findById(id).orElseThrow();
        if (!post.getAuthor().getEmail().equals(email)) throw new RuntimeException("Unauthorized");
        postRepo.delete(post);
    }
}
