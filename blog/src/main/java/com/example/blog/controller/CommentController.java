package com.example.blog.controller;

import com.example.blog.dto.CommentDTO;
import com.example.blog.model.Comment;
import com.example.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<CommentDTO> addComment(@PathVariable Long postId,
                                                 @RequestBody CommentDTO commentDto,
                                                 @AuthenticationPrincipal UserDetails principal) {
        Comment comment = commentService.addComment(postId, commentDto.getContent(), principal.getUsername());
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setContent(comment.getContent());
        dto.setPostId(comment.getPost().getId());
        dto.setAuthorName(comment.getAuthor().getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @DeleteMapping("/api/comments/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id, @AuthenticationPrincipal UserDetails principal) {
        commentService.deleteComment(id, principal.getUsername());
        return ResponseEntity.ok("Comment deleted");
    }
}
