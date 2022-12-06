package com.devjaewoo.openroadmaps.domain.blog.controller;

import com.devjaewoo.openroadmaps.domain.blog.dto.CategoryDto;
import com.devjaewoo.openroadmaps.domain.blog.dto.PostDto;
import com.devjaewoo.openroadmaps.domain.blog.service.BlogService;
import com.devjaewoo.openroadmaps.domain.client.dto.SessionClient;
import com.devjaewoo.openroadmaps.global.utils.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

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

    @GetMapping("/{clientName}/categories")
    public ResponseEntity<?> getCategories(@PathVariable @Pattern(regexp = "^@[A-Za-z0-9#_-]{1,10}$") String clientName) {
        String name = clientName.substring(1).toLowerCase();

        List<CategoryDto.ListItem.Response> result = blogService.getCategoryList(name).stream()
                .map(CategoryDto.ListItem.Response::from)
                .toList();

        return ResponseEntity.ok(result);
    }
}
