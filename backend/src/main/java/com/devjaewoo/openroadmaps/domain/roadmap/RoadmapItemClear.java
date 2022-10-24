package com.devjaewoo.openroadmaps.domain.roadmap;

import com.devjaewoo.openroadmaps.domain.client.Client;
import com.devjaewoo.openroadmaps.global.domain.BaseCreateTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoadmapItemClear extends BaseCreateTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roadmap_item_clear_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_item_id")
    private RoadmapItem roadmapItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    public static RoadmapItemClear create(RoadmapItem roadmapItem, Client client) {
        return new RoadmapItemClear(null, roadmapItem, client);
    }
}
