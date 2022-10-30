package com.devjaewoo.openroadmaps.domain.roadmap.entity;

import com.devjaewoo.openroadmaps.domain.client.entity.Client;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoadmapReportTest {

    @Test
    @DisplayName("create")
    public void create() {
        //given
        Client client = Client.create("name", "email", "password");
        Roadmap roadmap = Roadmap.create("title", "image", null, client);
        String cause = "cause";

        //when
        RoadmapReport roadmapReport = RoadmapReport.create(cause, roadmap, client);

        //then
        assertThat(roadmapReport.getId()).isNull();
        assertThat(roadmapReport.getCause()).isEqualTo(cause);
        assertThat(roadmapReport.getRoadmap()).isEqualTo(roadmap);
        assertThat(roadmapReport.getClient()).isEqualTo(client);
    }
}