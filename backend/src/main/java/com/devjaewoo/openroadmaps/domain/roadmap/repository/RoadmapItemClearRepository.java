package com.devjaewoo.openroadmaps.domain.roadmap.repository;

import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItemClear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoadmapItemClearRepository extends JpaRepository<RoadmapItemClear, Long> {
    @Query("select r from RoadmapItemClear r where r.roadmapItem.roadmap.id = :roadmapId and r.client.id = :clientId")
    List<RoadmapItemClear> findAllByRoadmapIdAndClientId(@Param("roadmapId") Long roadmapId, @Param("clientId") Long clientId);

    Optional<RoadmapItemClear> findByRoadmapItemIdAndClientId(Long roadmapItemId, Long clientId);
}
