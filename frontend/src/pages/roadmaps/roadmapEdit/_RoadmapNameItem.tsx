import { FC } from "react";
import { RoadmapItem } from "src/apis/useRoadmap";

interface Props {
  roadmapItem: RoadmapItem;
}

const RoadmapNameItem: FC<Props> = ({ roadmapItem }) => {
  return <div className="flex w-full h-12">{roadmapItem.name}</div>;
};

export default RoadmapNameItem;
