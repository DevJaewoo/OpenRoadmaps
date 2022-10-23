package com.devjaewoo.openroadmaps.domain.roadmap;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RoadmapItem {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roadmap_item_id")
    private Long id;

    private String name;
    private String content;

    @Enumerated(EnumType.STRING)
    private Recommend recommend;

    @Enumerated(EnumType.STRING)
    private ConnectionType connectionType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private RoadmapItem parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<RoadmapItem> roadmapItemList = new ArrayList<>();

    @OneToMany(mappedBy = "roadmapItem", cascade = CascadeType.ALL)
    private List<RoadmapItemReference> referenceList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_id")
    private Roadmap roadmap;
}
