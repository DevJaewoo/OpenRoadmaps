import { FC } from "react";
import { RoadmapSearch, useRoadmapList } from "src/apis/useRoadmap";
import RoadmapListItemComponent from "./_RoadmapListItem";

interface Props {
  search: RoadmapSearch;
}

const RoadmapList: FC<Props> = ({ search }) => {
  const { data, isError } = useRoadmapList(search);

  return (
    <div className="flex flex-col items-center w-full mt-5">
      <div className="flex flex-row flex-wrap items-center w-full p-3 bg-gray-300 rounded-lg">
        {!isError &&
          data?.content.map((listItem) => (
            <RoadmapListItemComponent key={listItem.id} data={listItem} />
          ))}
      </div>
    </div>
  );
};

export default RoadmapList;
