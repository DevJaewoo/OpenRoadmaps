package com.devjaewoo.openroadmaps.domain.roadmap.repository;

import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItem;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItemClear;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RoadmapItemClearRepository extends JpaRepository<RoadmapItemClear, Long> {
    Optional<RoadmapItemClear> findByRoadmapItemIdAndClientId(Long roadmapItemId, Long clientId);
    List<RoadmapItemClear> findAllByRoadmapItemInAndClientId(Collection<RoadmapItem> roadmapItem, Long client_id);
}
