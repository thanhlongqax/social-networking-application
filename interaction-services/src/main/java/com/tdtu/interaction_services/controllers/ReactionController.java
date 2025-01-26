package com.tdtu.interaction_services.controllers;


import com.tdtu.interaction_services.dtos.ResDTO;
import com.tdtu.interaction_services.dtos.request.DoReactRequest;
import com.tdtu.interaction_services.services.Impl.ReactionServiceImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reacts")
@RequiredArgsConstructor
@Tag(name = "Reaction Service", description = "API For Upload File")
public class ReactionController {
    private final ReactionServiceImpl reactionServiceImpl;

    @PostMapping()
    public ResponseEntity<?> doReact(@RequestHeader("Authorization") String token,
                                     @RequestBody DoReactRequest request){
        ResDTO<?> response = reactionServiceImpl.doReaction(token, request);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @GetMapping()
    public ResponseEntity<?> findByPost(@RequestHeader(name = "Authorization", required = false, defaultValue = "") String token,
                                        @RequestParam("postId") String postId){
        ResDTO<?> response = reactionServiceImpl.getReactsByPostId(token, postId);
        return ResponseEntity.status(response.getCode()).body(response);
    }
}