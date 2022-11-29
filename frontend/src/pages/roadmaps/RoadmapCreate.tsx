import { FC } from "react";
import Header from "src/components/Header";
import withAuth from "src/hoc/withAuth";
import RoadmapEdit from "./roadmapEdit/RoadmapEdit";

const RoadmapCreate: FC = () => {
  return (
    <div className="flex flex-col flex-1 items-center w-full">
      <div className="flex flex-col flex-1 items-center w-full max-w-7xl">
        <Header title="로드맵 만들기" text="" />
        <RoadmapEdit />
      </div>
    </div>
  );
};

export default withAuth(RoadmapCreate, true);
