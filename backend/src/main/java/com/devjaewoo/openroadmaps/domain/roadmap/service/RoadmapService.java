package com.devjaewoo.openroadmaps.domain.roadmap.service;

import com.devjaewoo.openroadmaps.domain.client.dto.ClientErrorCode;
import com.devjaewoo.openroadmaps.domain.client.entity.Client;
import com.devjaewoo.openroadmaps.domain.client.repository.ClientRepository;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapDto;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapErrorCode;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapSearch;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.Roadmap;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItem;
import com.devjaewoo.openroadmaps.domain.roadmap.repository.RoadmapRepository;
import com.devjaewoo.openroadmaps.global.exception.RestApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RoadmapService {

    public static final int DEFAULT_PAGE_SIZE = 12;

    private final ClientRepository clientRepository;
    private final RoadmapRepository roadmapRepository;

    public Page<RoadmapDto.ListItem> search(RoadmapSearch roadmapSearch) {
        Pageable pageable = PageRequest.of(roadmapSearch.page(), DEFAULT_PAGE_SIZE);
        return roadmapRepository.search(roadmapSearch, pageable)
                .map(RoadmapDto.ListItem::of);
    }

    @Transactional
    public Long create(RoadmapDto.CreateRequest request, Long clientId) {

        Client client = clientRepository.findById(clientId).orElseThrow(() -> new RestApiException(ClientErrorCode.CLIENT_NOT_FOUND));
        Roadmap roadmap = Roadmap.create(request.title(), request.image(), request.accessibility(), client);
        Map<Long, RoadmapItem> map = new HashMap<>();

        request.roadmapItemList().forEach(roadmapItemDto -> {
            RoadmapItem roadmapItem = roadmapItemDto.toEntity();
            roadmapItem.updateRoadmap(roadmap);
            map.put(roadmapItemDto.id(), roadmapItem);
        });

        request.roadmapItemList().forEach(roadmapItemDto -> {
            Long parentId = roadmapItemDto.parentId();
            if(parentId != null) {
                RoadmapItem parent = map.get(parentId);
                if(parent == null) throw new RestApiException(RoadmapErrorCode.INVALID_PARENT);
                map.get(roadmapItemDto.id()).updateParent(parent);
            }
        });

        Roadmap result = roadmapRepository.save(roadmap);

        return result.getId();
    }
}
