package com.devjaewoo.openroadmaps.domain.roadmap;

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
public class RoadmapItem extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roadmap_item_id")
    private Long id;

    private String name;
    private String content;

    private double x;
    private double y;

    @Enumerated(EnumType.STRING)
    private Recommend recommend;

    @Enumerated(EnumType.STRING)
    private ConnectionType connectionType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private RoadmapItem parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<RoadmapItem> roadmapItemList;

    @OneToMany(mappedBy = "roadmapItem", cascade = CascadeType.ALL)
    private List<RoadmapItemReference> referenceList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id")
    private Roadmap roadmap;

    public void updateRoadmap(Roadmap roadmap) {
        this.roadmap = roadmap;
        if(roadmap != null) {
            roadmap.getRoadmapItemList().add(this);
        }
    }

    public void updateParent(RoadmapItem parent) {
        this.parent = parent;
        if(parent != null) {
            parent.getRoadmapItemList().add(this);
        }
    }

    public void addRoadmapItem(RoadmapItem child) {
        if(child != null) {
            this.roadmapItemList.add(child);
            child.setParent(this);
        }
    }

    public void addReference(RoadmapItemReference roadmapItemReference) {
        if(roadmapItemReference != null) {
            this.referenceList.add(roadmapItemReference);
            roadmapItemReference.setRoadmapItem(this);
        }
    }

    public static RoadmapItem create(
            String name,
            String content,
            double x,
            double y,
            Recommend recommend,
            ConnectionType connectionType,
            RoadmapItem parent,
            Roadmap roadmap) {

        RoadmapItem roadmapItem = RoadmapItem.builder()
                .name(name)
                .content(content)
                .x(x).y(y)
                .recommend(recommend)
                .connectionType(connectionType)
                .roadmapItemList(new ArrayList<>())
                .referenceList(new ArrayList<>())
                .build();

        roadmapItem.updateParent(parent);
        roadmapItem.updateRoadmap(roadmap);

        return roadmapItem;
    }
}
