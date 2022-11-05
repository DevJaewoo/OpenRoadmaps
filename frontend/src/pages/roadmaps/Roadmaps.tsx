import { Suspense, useState } from "react";
import { RoadmapSearch } from "src/apis/useRoadmap";
import Header from "src/components/Header";
import RoadmapList from "./roadmapList/RoadmapList";

const Roadmaps: React.FC<{}> = () => {
  const [search] = useState<RoadmapSearch>({
    page: 0,
    order: "LATEST",
  });

  return (
    <div className="flex flex-col items-center">
      <div className="flex flex-col w-full max-w-7xl">
        <Header
          title="Roadmaps"
          text={
            "개발 공부는 하고 싶은데 어떻게 시작해야 할지 막막하신가요?\n로드맵을 따라 기초부터 차근차근 공부해보세요!"
          }
        />
        <Suspense>
          <RoadmapList search={search} />
        </Suspense>
      </div>
    </div>
  );
};

export default Roadmaps;
