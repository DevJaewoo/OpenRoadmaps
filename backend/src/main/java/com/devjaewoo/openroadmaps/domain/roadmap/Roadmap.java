package com.devjaewoo.openroadmaps.domain.roadmap;

import com.devjaewoo.openroadmaps.domain.client.Client;
import com.devjaewoo.openroadmaps.global.domain.Accessibility;
import com.devjaewoo.openroadmaps.global.domain.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Roadmap extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roadmap_id")
    private Long id;

    private String title;

    private String image;

    @Enumerated(EnumType.STRING)
    private Accessibility accessibility;

    private boolean isOfficial;

    private int likes;

    @OneToMany(mappedBy = "roadmap", cascade = CascadeType.ALL)
    private List<RoadmapItem> roadmapItemList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    public void addRoadmapItem(RoadmapItem roadmapItem) {
        if(roadmapItem != null) {
            this.roadmapItemList.add(roadmapItem);
            roadmapItem.setRoadmap(this);
        }
    }

    public static Roadmap create(String title, String image, Accessibility accessibility, Client client) {

        return Roadmap.builder()
                .title(title)
                .image(image)
                .accessibility(accessibility)
                .isOfficial(false)
                .likes(0)
                .roadmapItemList(new ArrayList<>())
                .client(client)
                .build();
    }
}
