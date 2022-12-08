package com.devjaewoo.openroadmaps.domain.blog.entity;

import com.devjaewoo.openroadmaps.domain.client.entity.Client;
import com.devjaewoo.openroadmaps.domain.roadmap.entity.RoadmapItem;
import com.devjaewoo.openroadmaps.global.domain.Accessibility;
import com.devjaewoo.openroadmaps.global.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    private String title;

    @Lob
    private String content;

    private String image;

    private int likes;
    private int views;
    private boolean isDeleted;

    @Enumerated(value = EnumType.STRING)
    private Accessibility accessibility;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roadmap_item_id")
    private RoadmapItem roadmapItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    public void updateCategory(Category category) {
        if(category != null) {
            category.getPostList().add(this);
            this.category = category;
        }
    }

    public static Post create(String title, String content, String image, Accessibility accessibility, Category category, RoadmapItem roadmapItem, Client client) {
        Post post = new Post();
        post.title = title;
        post.content = content;
        post.image = image;
        post.accessibility = accessibility;
        post.likes = 0;
        post.isDeleted = false;

        post.updateCategory(category);
        post.roadmapItem = roadmapItem;
        post.client = client;

        return post;
    }
}
