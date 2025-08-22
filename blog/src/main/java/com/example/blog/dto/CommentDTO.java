package com.example.blog.dto;

import lombok.Data;

@Data
public class CommentDTO {
    private Long id;
    private String content;
    private Long postId;
    private String authorName;
}
