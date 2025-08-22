package com.example.blog.dto;

import lombok.Data;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private Long authorId;
    private String authorName;
}
