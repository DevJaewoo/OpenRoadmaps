import { FC } from "react";
import { Recommend, TRecommend } from "src/apis/useRoadmap";
import { RoadmapColor } from "src/utils/constants";

interface Props {
  size?: number;
  recommend?: TRecommend;
  className?: string;
}

const RoadmapRecommendIcon: FC<Props> = ({
  size = 2,
  recommend,
  className,
}) => {
  const color = RoadmapColor[recommend ?? Recommend.NONE];
  return (
    <div
      className={`w-${size} h-${size} rounded-full bg-${color} ${className}`}
      style={{ backgroundColor: color }}
    />
  );
};

export default RoadmapRecommendIcon;
