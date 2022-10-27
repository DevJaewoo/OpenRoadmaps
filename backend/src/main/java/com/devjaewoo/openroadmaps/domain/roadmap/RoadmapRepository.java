package com.devjaewoo.openroadmaps.domain.roadmap;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadmapRepository extends JpaRepository<Roadmap, Long>, RoadmapRepositoryCustom {

}
