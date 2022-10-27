package com.devjaewoo.openroadmaps.domain.roadmap.repository;

import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapSearch;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.Roadmap;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoadmapRepositoryCustom {
    List<Roadmap> search(RoadmapSearch roadmapSearch, Pageable pageable);
}
