package com.devjaewoo.openroadmaps.domain.roadmap.controller;

import com.devjaewoo.openroadmaps.domain.client.dto.SessionClient;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapDto;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapSearch;
import com.devjaewoo.openroadmaps.domain.roadmap.service.RoadmapService;
import com.devjaewoo.openroadmaps.global.dto.PageResponseDto;
import com.devjaewoo.openroadmaps.global.utils.SessionUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/api/v1/roadmaps")
@RequiredArgsConstructor
public class RoadmapController {

    private final RoadmapService roadmapService;

    // roadmap 조회 (paging)
    @GetMapping("")
    public ResponseEntity<?> search(@Valid RoadmapSearch roadmapSearch) {
        Page<RoadmapDto.ListItem.Response> responseList = roadmapService.search(roadmapSearch)
                .map(RoadmapDto.ListItem.Response::of);

        return ResponseEntity.ok(new PageResponseDto<>(responseList));
    }

    // roadmap 등록 (
    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody RoadmapDto.CreateRequest request) {
        SessionClient sessionClient = SessionUtil.getCurrentClient();
        roadmapService.create(request, sessionClient.getId());
        return ResponseEntity.ok("");
    }
}
