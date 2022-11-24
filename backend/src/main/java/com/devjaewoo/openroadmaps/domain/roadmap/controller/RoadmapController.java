package com.devjaewoo.openroadmaps.domain.roadmap.controller;

import com.devjaewoo.openroadmaps.domain.client.dto.SessionClient;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapDto;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapItemClearDto;
import com.devjaewoo.openroadmaps.domain.roadmap.dto.RoadmapLikeDto;
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

    @GetMapping("/{roadmapId}")
    public ResponseEntity<?> find(@PathVariable Long roadmapId) {
        Long clientId = SessionUtil.getOptionalCurrentClient().map(SessionClient::getId).orElse(null);
        RoadmapDto result = roadmapService.findById(roadmapId, clientId);
        return ResponseEntity.ok(RoadmapDto.Response.from(result));
    }

    @GetMapping("")
    public ResponseEntity<?> search(@Valid RoadmapSearch roadmapSearch) {
        Page<RoadmapDto.ListItem.Response> responseList = roadmapService.search(roadmapSearch)
                .map(RoadmapDto.ListItem.Response::from);

        return ResponseEntity.ok(new PageResponseDto<>(responseList));
    }

    @PostMapping("")
    public ResponseEntity<?> create(@Valid @RequestBody RoadmapDto.CreateRequest request) {
        SessionClient sessionClient = SessionUtil.getCurrentClient();
        Long roadmapId = roadmapService.create(request, sessionClient.getId());
        return ResponseEntity.ok(new RoadmapDto.CreateResponse(roadmapId));
    }

    @PutMapping("/{roadmapId}/items/{roadmapItemId}/clear")
    public ResponseEntity<?> clearRoadmapItem(@PathVariable Long roadmapId, @PathVariable Long roadmapItemId, @Valid @RequestBody RoadmapItemClearDto.ClearRequest request) {
        SessionClient sessionClient = SessionUtil.getCurrentClient();
        RoadmapItemClearDto result = roadmapService.clearRoadmapItem(roadmapId, roadmapItemId, request.isCleared(), sessionClient.getId());
        return ResponseEntity.ok(RoadmapItemClearDto.ClearResponse.from(result));
    }

    @PutMapping("/{roadmapId}/like")
    public ResponseEntity<?> likeRoadmap(@PathVariable Long roadmapId, @Valid @RequestBody RoadmapLikeDto.LikeRequest request) {
        SessionClient sessionClient = SessionUtil.getCurrentClient();
        RoadmapLikeDto result = roadmapService.likeRoadmap(roadmapId, request.like(), sessionClient.getId());
        return ResponseEntity.ok(RoadmapLikeDto.LikeResponse.from(result));
    }
}
