package com.devjaewoo.openroadmaps.domain.roadmap;

import com.devjaewoo.openroadmaps.global.domain.BaseCreateTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoadmapItemReference extends BaseCreateTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roadmap_item_reference_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_item_id")
    private RoadmapItem roadmapItem;

    private String url;

    public static RoadmapItemReference create(RoadmapItem roadmapItem, String url) {
        return new RoadmapItemReference(null, roadmapItem, url);
    }
}
