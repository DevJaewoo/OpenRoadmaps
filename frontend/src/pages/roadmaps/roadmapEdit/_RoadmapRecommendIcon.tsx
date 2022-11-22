import { FC } from "react";
import { Recommend, TRecommend } from "src/apis/useRoadmap";
import { RoadmapColor } from "src/utils/constants";

interface Props {
  recommend?: TRecommend;
  className?: string;
}

const RoadmapRecommendIcon: FC<Props> = ({ recommend, className }) => {
  const color = RoadmapColor[recommend ?? Recommend.NONE];
  return (
    <div
      className={`w-2 h-2 rounded-full bg-${color} ${className}`}
      style={{ backgroundColor: color }}
    />
  );
};

export default RoadmapRecommendIcon;
