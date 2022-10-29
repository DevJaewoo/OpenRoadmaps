package com.devjaewoo.openroadmaps.domain.roadmap.repository;

import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapSearch;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.Roadmap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoadmapRepositoryCustom {
    Page<Roadmap> search(RoadmapSearch roadmapSearch, Pageable pageable);
}
