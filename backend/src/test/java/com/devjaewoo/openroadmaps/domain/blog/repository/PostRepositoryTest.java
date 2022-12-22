package com.devjaewoo.openroadmaps.domain.blog.repository;

import com.devjaewoo.openroadmaps.RepositoryTest;
import com.devjaewoo.openroadmaps.domain.blog.dto.PostOrder;
import com.devjaewoo.openroadmaps.domain.blog.dto.PostSearch;
import com.devjaewoo.openroadmaps.domain.blog.entity.Category;
import com.devjaewoo.openroadmaps.domain.blog.entity.Post;
import com.devjaewoo.openroadmaps.domain.client.entity.Client;
import com.devjaewoo.openroadmaps.domain.client.repository.ClientRepository;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItem;
import com.devjaewoo.openroadmaps.domain.roadmap.repository.RoadmapItemRepository;
import com.devjaewoo.openroadmaps.global.domain.Accessibility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RepositoryTest
class PostRepositoryTest {

    @Autowired ClientRepository clientRepository;
    @Autowired CategoryRepository categoryRepository;
    @Autowired RoadmapItemRepository roadmapItemRepository;
    @Autowired PostRepository postRepository;

    @Nested
    @DisplayName("검색")
    class search {

        @Test
        @DisplayName("search")
        public void findBySearch() {
            //given
            Client client1 = Client.create("client1", "email1", "password");
            Client client2 = Client.create("client2", "email2", "password");
            clientRepository.save(client1);
            clientRepository.save(client2);

            Category category1 = Category.create("category1", null);
            Category category2 = Category.create("category1", null);
            categoryRepository.save(category1);
            categoryRepository.save(category2);

            RoadmapItem roadmapItem1 = RoadmapItem.create("item1", "content", 1, 2, null, null, null, null);
            RoadmapItem roadmapItem2 = RoadmapItem.create("item1", "content", 1, 2, null, null, null, null);
            roadmapItemRepository.save(roadmapItem1);
            roadmapItemRepository.save(roadmapItem2);

            Post post1 = Post.create("title", "content", null, Accessibility.PRIVATE, category1, roadmapItem1, client1);
            Post post2 = Post.create("title", "content", null, Accessibility.PRIVATE, category1, roadmapItem1, client1);
            Post post3 = Post.create("title3", "content", null, Accessibility.PRIVATE, category1, roadmapItem1, client1);
            Post post4 = Post.create("title4", "content", null, Accessibility.PRIVATE, category1, roadmapItem1, client1);
            Post post5 = Post.create("title5", "content", null, Accessibility.PUBLIC, category2, roadmapItem2, client2);
            Post post6 = Post.create("title6", "content", null, Accessibility.PUBLIC, category2, roadmapItem2, client2);
            Post post7 = Post.create("title", "content", null, Accessibility.PUBLIC, category2, roadmapItem2, client2);
            Post post8 = Post.create("title", "content", null, Accessibility.PUBLIC, category2, roadmapItem2, client2);

            post1.setLikes(8);
            post2.setLikes(7);
            post3.setLikes(6);
            post4.setLikes(5);
            post5.setLikes(4);
            post6.setLikes(3);
            post7.setLikes(2);
            post8.setLikes(1);

            postRepository.saveAll(Arrays.asList(post1, post2, post3, post4, post5, post6, post7, post8));

            PostSearch defaultSearch = new PostSearch(null, null, null, null, null, 0);
            PostSearch searchByTitle = new PostSearch(post1.getTitle(), null, null, null, null, 0);
            PostSearch searchByClientName = new PostSearch(null, client2.getName(), null, null, null, 0);
            PostSearch searchByCategoryId = new PostSearch(null, null, category1.getId(), null, null, 0);
            PostSearch searchByRoadmapItemId = new PostSearch(null, null, null, roadmapItem2.getId(), null, 0);
            PostSearch searchByOrderLatest = new PostSearch(null, null, null, null, PostOrder.LATEST, 0);
            PostSearch searchByOrderLikes = new PostSearch(null, null, null, null, PostOrder.LIKES, 0);

            PageRequest defaultPageable = PageRequest.of(0, 10);
            PageRequest searchByPage = PageRequest.of(2, 3);

            //when

            Page<Post> result1 = postRepository.search(searchByTitle, defaultPageable, client1.getId());
            Page<Post> result2 = postRepository.search(searchByClientName, defaultPageable, client1.getId());
            Page<Post> result3 = postRepository.search(searchByCategoryId, defaultPageable, client1.getId());
            Page<Post> result4 = postRepository.search(searchByRoadmapItemId, defaultPageable, client1.getId());
            Page<Post> result5 = postRepository.search(searchByOrderLatest, defaultPageable, client1.getId());
            Page<Post> result6 = postRepository.search(searchByOrderLikes, defaultPageable, client1.getId());
            Page<Post> result7 = postRepository.search(defaultSearch, searchByPage, client1.getId());
            Page<Post> result8 = postRepository.search(defaultSearch, defaultPageable, client2.getId());

            //then
            assertThat(result1.getContent()).contains(post1, post2, post7, post8);
            assertThat(result2.getContent()).contains(post5, post6, post7, post8);
            assertThat(result3.getContent()).contains(post1, post2, post3, post4);
            assertThat(result4.getContent()).contains(post5, post6, post7, post8);

            List<Post> orderLatest = result5.getContent();
            for(int i = 1; i < orderLatest.size(); i++) {
                assertThat(orderLatest.get(i - 1).getCreatedDate())
                        .isAfterOrEqualTo(orderLatest.get(i).getCreatedDate());
            }

            List<Post> orderLikes = result6.getContent();
            for(int i = 1; i < orderLikes.size(); i++) {
                assertThat(orderLikes.get(i - 1).getLikes())
                        .isGreaterThanOrEqualTo(orderLikes.get(i).getLikes());
            }

            assertThat(result7.getContent()).hasSize(2);
            assertThat(result8.getContent()).contains(post5, post6, post7, post8);
        }
    }
}