package com.devjaewoo.openroadmaps.domain.blog.repository;

import com.devjaewoo.openroadmaps.domain.blog.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, PostRepositoryCustom {
    Optional<Post> findByIdAndClientName(Long id, String clientName);

    @Modifying
    @Query("update Post p set p.category = null where p.category.id = :category_id")
    void updateCategoryToNull(@Param("category_id") Long categoryId);
}
