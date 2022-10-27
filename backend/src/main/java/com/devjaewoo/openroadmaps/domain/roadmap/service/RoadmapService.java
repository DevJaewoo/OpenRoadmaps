package com.devjaewoo.openroadmaps.domain.roadmap.service;

import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapSearch;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.Roadmap;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapDto;
import com.devjaewoo.openroadmaps.domain.roadmap.repository.RoadmapRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoadmapService {

    public static final int DEFAULT_PAGE_SIZE = 12;

    private final RoadmapRepository roadmapRepository;

    public List<RoadmapDto.ListItem> search(RoadmapSearch roadmapSearch) {
        Pageable pageable = PageRequest.of(roadmapSearch.page(), DEFAULT_PAGE_SIZE);
        List<Roadmap> roadmapList = roadmapRepository.search(roadmapSearch, pageable);
        return roadmapList.stream()
                .map(RoadmapDto.ListItem::of)
                .toList();
    }
}
