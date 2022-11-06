import { FC } from "react";
import { RoadmapListItem } from "src/apis/useRoadmap";
import { FaHeart } from "react-icons/fa";

interface Props {
  data: RoadmapListItem;
}

const RoadmapListItemComponent: FC<Props> = ({ data }) => {
  return (
    <div className="flex flex-col w-[32%] mx-[calc((100%-(32%*3))/6)] my-1 p-3 bg-white rounded-md border transition-transform hover:cursor-pointer hover:scale-105">
      <img
        src={data.image}
        alt={data.image}
        className="w-full h-48 bg-indigo-100 rounded-md object-cover"
      />
      <h4 className="mt-2 text-lg font-semibold">{data.title}</h4>
      <div className="flex flex-row justify-between mt-1">
        <div className="flex flex-row items-center">
          <FaHeart className="mr-1 text-xs text-red-500" />
          <p className="">{data.likes}</p>
        </div>
        <p className="text-sm">{`by. ${data.clientId}`}</p>
      </div>
    </div>
  );
};

export default RoadmapListItemComponent;
