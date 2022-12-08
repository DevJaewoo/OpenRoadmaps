package com.devjaewoo.openroadmaps.domain.blog.service;

import com.devjaewoo.openroadmaps.domain.blog.dto.BlogErrorCode;
import com.devjaewoo.openroadmaps.domain.blog.dto.CategoryDto;
import com.devjaewoo.openroadmaps.domain.blog.dto.PostDto;
import com.devjaewoo.openroadmaps.domain.blog.dto.PostSearch;
import com.devjaewoo.openroadmaps.domain.blog.entity.Category;
import com.devjaewoo.openroadmaps.domain.blog.entity.Post;
import com.devjaewoo.openroadmaps.domain.blog.repository.CategoryRepository;
import com.devjaewoo.openroadmaps.domain.blog.repository.PostRepository;
import com.devjaewoo.openroadmaps.domain.client.dto.ClientErrorCode;
import com.devjaewoo.openroadmaps.domain.client.entity.Client;
import com.devjaewoo.openroadmaps.domain.client.repository.ClientRepository;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItem;
import com.devjaewoo.openroadmaps.domain.roadmap.repository.RoadmapItemRepository;
import com.devjaewoo.openroadmaps.global.domain.Accessibility;
import com.devjaewoo.openroadmaps.global.exception.CommonErrorCode;
import com.devjaewoo.openroadmaps.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogService {

    public static final int DEFAULT_PAGE_SIZE = 12;

    private final ClientRepository clientRepository;
    private final CategoryRepository categoryRepository;
    private final RoadmapItemRepository roadmapItemRepository;
    private final PostRepository postRepository;

    private Optional<Category> getCategoryFromId(Long categoryId) {
        if(categoryId == null) return Optional.empty();

        return Optional.of(
                categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND))
        );
    }

    private Optional<RoadmapItem> getRoadmapItemFromId(Long roadmapItemId) {
        if(roadmapItemId == null) return Optional.empty();
        return Optional.of(
                roadmapItemRepository.findById(roadmapItemId)
                        .orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND))
        );
    }

    @Transactional
    public PostDto save(PostDto.CreateRequest request, Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RestApiException(ClientErrorCode.CLIENT_NOT_FOUND));

        Category category = getCategoryFromId(request.categoryId()).orElse(null);
        if(category != null && !category.getClient().getId().equals(clientId)) {
            throw new RestApiException(CommonErrorCode.FORBIDDEN);
        }

        RoadmapItem roadmapItem = getRoadmapItemFromId(request.roadmapItemId()).orElse(null);

        Post post = Post.create(request.title(), request.content(), request.accessibility(), category, roadmapItem, client);
        postRepository.save(post);

        return PostDto.from(post);
    }

    @Transactional
    public PostDto getPost(String clientName, Long postId, Long clientId) {
        Post post = postRepository.findByIdAndClientName(postId, clientName)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));

        if(post.isDeleted()) {
            throw new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND);
        }

        if(post.getAccessibility() != Accessibility.PUBLIC && !post.getClient().getId().equals(clientId)) {
            throw new RestApiException(CommonErrorCode.FORBIDDEN);
        }

        post.setViews(post.getViews() + 1);

        return PostDto.from(post);
    }

    public Page<PostDto.ListItem> search(PostSearch postSearch, Long clientId) {
        PageRequest pageable = PageRequest.of(postSearch.page(), DEFAULT_PAGE_SIZE);
        return postRepository.search(postSearch, pageable, clientId)
                .map(PostDto.ListItem::from);
    }

    @Transactional
    public Long addCategory(String name, Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new RestApiException(ClientErrorCode.CLIENT_NOT_FOUND));

        if(categoryRepository.existsByNameAndClientId(name, clientId)) {
            throw new RestApiException(BlogErrorCode.DUPLICATE_CATEGORY);
        }

        Category category = Category.create(name, client);
        categoryRepository.save(category);

        return category.getId();
    }

    public List<CategoryDto.ListItem> getCategoryList(String clientName) {
        Client client = clientRepository.findByName(clientName)
                .orElseThrow(() -> new RestApiException(ClientErrorCode.CLIENT_NOT_FOUND));

        List<Category> categoryList = categoryRepository.findAllByClientId(client.getId());

        return categoryList.stream()
                .map(CategoryDto.ListItem::from)
                .toList();
    }

    @Transactional
    public Long deleteCategory(Long categoryId, Long clientId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND));

        if(!category.getClient().getId().equals(clientId)) {
            throw new RestApiException(CommonErrorCode.FORBIDDEN);
        }

        // category.getPostList().forEach(post -> post.setCategory(null));
        postRepository.updateCategoryToNull(categoryId);
        categoryRepository.delete(category);

        return category.getId();
    }
}
