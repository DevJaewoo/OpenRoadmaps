package com.devjaewoo.openroadmaps.domain.blog.repository;

import com.devjaewoo.openroadmaps.domain.blog.dto.PostSearch;
import com.devjaewoo.openroadmaps.domain.blog.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<Post> search(PostSearch search, Pageable pageable, Long currentClientId);
}
