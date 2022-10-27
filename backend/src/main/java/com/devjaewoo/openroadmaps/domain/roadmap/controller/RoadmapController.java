package com.devjaewoo.openroadmaps.domain.roadmap.controller;

import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapDto;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapSearch;
import com.devjaewoo.openroadmaps.domain.roadmap.service.RoadmapService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/roadmaps")
@RequiredArgsConstructor
public class RoadmapController {

    private final RoadmapService roadmapService;

    // roadmap 조회 (paging)
    @GetMapping("")
    public ResponseEntity<?> search(@Valid RoadmapSearch roadmapSearch) {
        List<RoadmapDto.ListItem.Response> responseList = roadmapService.search(roadmapSearch).stream()
                .map(RoadmapDto.ListItem.Response::of)
                .toList();

        return ResponseEntity.ok(new RoadmapDto.ResponseList(responseList));
    }

    // roadmap 등록 (
}
