package com.example.blog.service;

import com.example.blog.model.*;
import com.example.blog.repository.*;
import com.example.blog.messaging.producer.CommentNotificationProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepo;
    private final PostRepository postRepo;
    private final UserRepository userRepo;
    private final CommentNotificationProducer notificationProducer;

    public Comment addComment(Long postId, String content, String userEmail) {
        User user = userRepo.findByEmail(userEmail).orElseThrow();
        Post post = postRepo.findById(postId).orElseThrow();
        Comment comment = Comment.builder().post(post).author(user).content(content).build();
        Comment saved = commentRepo.save(comment);
        notificationProducer.sendNotification(post.getAuthor().getId(),
                user.getName() + " commented on your post: " + content);
        return saved;
    }

    public void deleteComment(Long commentId, String email) {
        Comment comment = commentRepo.findById(commentId).orElseThrow();
        if (!comment.getAuthor().getEmail().equals(email)) throw new RuntimeException("Unauthorized");
        commentRepo.delete(comment);
    }
}
