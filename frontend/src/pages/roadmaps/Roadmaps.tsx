import { Suspense } from "react";
import { RoadmapSearch, useRoadmapList } from "src/apis/useRoadmap";

const RoadmapList = () => {
  const a: RoadmapSearch = {
    page: 0,
    order: "LATEST",
  };

  const { data, isError } = useRoadmapList(a);

  return <div>{!isError && JSON.stringify(data)}</div>;
};

const Roadmaps: React.FC<{}> = () => {
  return (
    <div>
      <Suspense>
        <RoadmapList />
      </Suspense>
    </div>
  );
};

export default Roadmaps;
