package com.devjaewoo.openroadmaps.domain.blog.service;

import com.devjaewoo.openroadmaps.domain.blog.dto.CategoryDto;
import com.devjaewoo.openroadmaps.domain.blog.dto.PostDto;
import com.devjaewoo.openroadmaps.domain.blog.entity.Category;
import com.devjaewoo.openroadmaps.domain.blog.entity.Post;
import com.devjaewoo.openroadmaps.domain.blog.repository.CategoryRepository;
import com.devjaewoo.openroadmaps.domain.blog.repository.PostRepository;
import com.devjaewoo.openroadmaps.domain.client.dto.ClientErrorCode;
import com.devjaewoo.openroadmaps.domain.client.entity.Client;
import com.devjaewoo.openroadmaps.domain.client.repository.ClientRepository;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItem;
import com.devjaewoo.openroadmaps.domain.roadmap.repository.RoadmapItemRepository;
import com.devjaewoo.openroadmaps.global.exception.CommonErrorCode;
import com.devjaewoo.openroadmaps.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BlogService {

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
        RoadmapItem roadmapItem = getRoadmapItemFromId(request.roadmapItemId()).orElse(null);

        Post post = Post.create(request.title(), request.content(), request.accessibility(), category, roadmapItem, client);
        postRepository.save(post);

        return PostDto.from(post);
    }

    public List<CategoryDto.ListItem> getCategoryList(String clientName) {
        Client client = clientRepository.findByName(clientName)
                .orElseThrow(() -> new RestApiException(ClientErrorCode.CLIENT_NOT_FOUND));

        List<Category> categoryList = categoryRepository.findAllByClientId(client.getId());

        return categoryList.stream()
                .map(CategoryDto.ListItem::from)
                .toList();
    }
}
