package com.devjaewoo.openroadmaps.domain.blog.controller;

import com.devjaewoo.openroadmaps.domain.blog.dto.CategoryDto;
import com.devjaewoo.openroadmaps.domain.blog.dto.PostDto;
import com.devjaewoo.openroadmaps.domain.blog.dto.PostSearch;
import com.devjaewoo.openroadmaps.domain.blog.service.BlogService;
import com.devjaewoo.openroadmaps.domain.client.dto.SessionClient;
import com.devjaewoo.openroadmaps.global.dto.PageResponseDto;
import com.devjaewoo.openroadmaps.global.utils.SessionUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/blog")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @PostMapping("/posts")
    public ResponseEntity<?> post(@Valid @RequestBody PostDto.CreateRequest request) {
        SessionClient currentClient = SessionUtil.getCurrentClient();
        PostDto result = blogService.save(request, currentClient.getId());
        return ResponseEntity.ok(new PostDto.CreateResponse(result.id()));
    }

    @GetMapping("/{clientName}/posts/{postId}")
    public ResponseEntity<?> getPost(@PathVariable String clientName, @PathVariable Long postId) {
        String name = clientName.toLowerCase();
        Long currenClientId = SessionUtil.getOptionalCurrentClient().map(SessionClient::getId).orElse(null);

        PostDto result = blogService.getPost(name, postId, currenClientId);
        return ResponseEntity.ok(PostDto.Response.from(result));
    }

    @GetMapping("/{clientName}/categories")
    public ResponseEntity<?> getCategories(@PathVariable String clientName) {
        String name = clientName.toLowerCase();

        List<CategoryDto.ListItem.Response> result = blogService.getCategoryList(name).stream()
                .map(CategoryDto.ListItem.Response::from)
                .toList();

        return ResponseEntity.ok(new CategoryDto.ListItem.ResponseList(result));
    }

    @PostMapping("/categories")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDto.CreateRequest request) {
        SessionClient currentClient = SessionUtil.getCurrentClient();
        Long result = blogService.addCategory(request.name(), currentClient.getId());
        return ResponseEntity.ok(new CategoryDto.CreateResponse(result));
    }

    @DeleteMapping("/categories/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        SessionClient currentClient = SessionUtil.getCurrentClient();
        Long result = blogService.deleteCategory(categoryId, currentClient.getId());
        return ResponseEntity.ok(new CategoryDto.DeleteResponse(result));
    }

    @GetMapping("/posts")
    public ResponseEntity<?> search(@Valid PostSearch postSearch) {
        Optional<SessionClient> currentClient = SessionUtil.getOptionalCurrentClient();
        Long clientId = currentClient.map(SessionClient::getId).orElse(null);

        Page<PostDto.ListItem> result = blogService.search(postSearch, clientId);
        return ResponseEntity.ok(new PageResponseDto<>(result));
    }
}
