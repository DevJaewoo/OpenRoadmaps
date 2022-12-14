package com.devjaewoo.openroadmaps.domain.roadmap.entity;

import com.devjaewoo.openroadmaps.domain.client.entity.Client;
import com.devjaewoo.openroadmaps.global.domain.BaseCreateTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoadmapLike extends BaseCreateTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roadmap_like_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id")
    private Roadmap roadmap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    @Setter
    @Column(name = "is_like")
    private boolean like;

    public static RoadmapLike create(Roadmap roadmap, Client client) {
        return new RoadmapLike(null, roadmap, client, false);
    }
}
