package com.devjaewoo.openroadmaps.domain.roadmap;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoadmapRepositoryCustom {
    List<Roadmap> search(RoadmapSearch roadmapSearch, Pageable pageable);
}
