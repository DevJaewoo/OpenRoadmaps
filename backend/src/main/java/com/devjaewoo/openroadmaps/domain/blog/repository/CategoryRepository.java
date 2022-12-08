package com.devjaewoo.openroadmaps.domain.blog.repository;

import com.devjaewoo.openroadmaps.domain.blog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findAllByClientId(Long clientId);
    boolean existsByNameAndClientId(String name, Long clientId);
}
