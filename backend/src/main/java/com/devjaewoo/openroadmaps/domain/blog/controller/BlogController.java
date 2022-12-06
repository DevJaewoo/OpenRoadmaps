package com.devjaewoo.openroadmaps.domain.blog.controller;

import com.devjaewoo.openroadmaps.domain.blog.dto.PostDto;
import com.devjaewoo.openroadmaps.domain.blog.service.BlogService;
import com.devjaewoo.openroadmaps.domain.client.dto.SessionClient;
import com.devjaewoo.openroadmaps.global.utils.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/blog")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @PostMapping("/post")
    public ResponseEntity<?> post(@Valid @RequestBody PostDto.CreateRequest request) {
        SessionClient currentClient = SessionUtil.getCurrentClient();
        PostDto result = blogService.save(request, currentClient.getId());
        return ResponseEntity.ok(new PostDto.CreateResponse(result.id()));
    }
}
