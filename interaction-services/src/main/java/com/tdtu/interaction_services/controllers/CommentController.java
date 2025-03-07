package com.tdtu.interaction_services.controllers;


import com.tdtu.interaction_services.dtos.ResDTO;
import com.tdtu.interaction_services.dtos.request.AddCommentRequest;
import com.tdtu.interaction_services.dtos.request.UpdateCommentRequest;
import com.tdtu.interaction_services.services.CommentService;
import com.tdtu.interaction_services.services.Impl.CommentsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
@Tag(name = "Interaction Service", description = "API For Interaction")
public class CommentController {
    private final CommentService commentsServiceImpl;

    @Operation(summary = "add comment", description = "Returns a list of users")
    @PostMapping
    public ResponseEntity<?> addComment(@RequestHeader("Authorization") String token,
                                        @RequestBody AddCommentRequest comment) {
        ResDTO<?> response = commentsServiceImpl.addComment(token, comment);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateComment(@RequestHeader("Authorization") String token,
                                           @PathVariable("id") String id,
                                           @RequestBody UpdateCommentRequest comment) {
        ResDTO<?> response = commentsServiceImpl.updateComment(token, id, comment);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteComment(@RequestHeader("Authorization") String token,
                                           @PathVariable("id") String id) {
        ResDTO<?> response = commentsServiceImpl.deleteComment(token, id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findCommentById(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                             @PathVariable("id") String id) {
        ResDTO<?> response = commentsServiceImpl.findCommentById(token, id);
        return ResponseEntity.ok(response);
    }
    @GetMapping
    public ResponseEntity<?> findAllComments(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                             @RequestParam(name = "postId", required = false) String postId) {
        if (postId != null) {
            ResDTO<?> response = commentsServiceImpl.findCommentsByPostId(token, postId);
            return ResponseEntity.ok(response);
        } else {
            ResDTO<?> response = commentsServiceImpl.findAllComments(token);
            return ResponseEntity.ok(response);
        }
    }
}