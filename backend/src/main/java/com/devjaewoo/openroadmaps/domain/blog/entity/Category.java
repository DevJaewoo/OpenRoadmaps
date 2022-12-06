package com.devjaewoo.openroadmaps.domain.blog.entity;

import com.devjaewoo.openroadmaps.domain.client.entity.Client;
import com.devjaewoo.openroadmaps.global.domain.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "unique_category_name", columnNames = {"name"}),
})
public class Category extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "category")
    private List<Post> postList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    public void addPost(Post post) {
        if(post != null) {
            post.setCategory(this);
            this.postList.add(post);
        }
    }

    public static Category create(String name, Client client) {
        Category category = new Category();
        category.name = name;
        category.client = client;
        return category;
    }
}
