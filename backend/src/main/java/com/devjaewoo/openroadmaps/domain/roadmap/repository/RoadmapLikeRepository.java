package com.devjaewoo.openroadmaps.domain.roadmap.repository;

import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoadmapLikeRepository extends JpaRepository<RoadmapLike, Long> {
    Optional<RoadmapLike> findByRoadmapIdAndClientId(Long roadmapId, Long clientId);
}
