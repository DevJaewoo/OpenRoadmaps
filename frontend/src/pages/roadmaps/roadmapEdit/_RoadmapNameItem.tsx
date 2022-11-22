import { FC } from "react";
import { RoadmapItem } from "src/apis/useRoadmap";
import RoadmapRecommendIcon from "./_RoadmapRecommendIcon";

interface Props {
  roadmapItem: RoadmapItem;
  onClick: (id: number) => void;
}

const RoadmapNameItem: FC<Props> = ({ roadmapItem, onClick }) => {
  return (
    <div
      role="button"
      className="flex flex-row justify-between items-center w-full h-12 p-2 border-b border-gray-100 cursor-pointer"
      onClick={() => onClick(roadmapItem.id)}
      aria-hidden
    >
      <p>{roadmapItem.name}</p>
      <RoadmapRecommendIcon recommend={roadmapItem.recommend} />
    </div>
  );
};

export default RoadmapNameItem;
