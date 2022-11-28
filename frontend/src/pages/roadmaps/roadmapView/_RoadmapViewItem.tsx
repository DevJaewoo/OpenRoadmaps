import { FC, RefObject } from "react";
import { RoadmapItem } from "src/apis/useRoadmap";
import { RoadmapColor } from "src/utils/constants";

interface Props {
  refs: RefObject<HTMLDivElement>;
  roadmapItem: RoadmapItem;
  onClick: (id: number) => void;
}

const RoadmapViewItem: FC<Props> = ({ refs, roadmapItem, onClick }) => {
  return (
    <div
      ref={refs}
      className={`flex justify-center items-center absolute max-w-xs px-5 py-2 z-10 bg-white border-4 rounded-xl "cursor-pointer"
        `}
      style={{
        top: `${roadmapItem.y}rem`,
        left: `${roadmapItem.x}rem`,
        borderColor: RoadmapColor[roadmapItem.recommend],
      }}
      onClick={() => onClick(roadmapItem.id)}
      role="button"
      aria-hidden
    >
      <p className="max-w-sm select-none">{roadmapItem.name}</p>
    </div>
  );
};

export default RoadmapViewItem;
