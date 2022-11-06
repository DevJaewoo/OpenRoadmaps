import { FC } from "react";
import {
  RoadmapList,
  RoadmapSearch,
  useRoadmapList,
} from "src/apis/useRoadmap";
import RoadmapListItemComponent from "./_RoadmapListItem";
import RoadmapNotFound from "./_RoadmapNotFound";

interface Props {
  search: RoadmapSearch;
  onSearch?: (data: RoadmapList) => void;
  className?: String;
}

const RoadmapListComponent: FC<Props> = ({ search, onSearch, className }) => {
  const { data, isError } = useRoadmapList(search, onSearch);

  return (
    <div className={`flex flex-col items-center w-full ${className}`}>
      <div className="flex flex-row flex-wrap items-center w-full">
        {!isError && (data?.content.length || 0) > 0 ? (
          data?.content.map((listItem) => (
            <RoadmapListItemComponent key={listItem.id} data={listItem} />
          ))
        ) : (
          <RoadmapNotFound />
        )}
      </div>
    </div>
  );
};

export default RoadmapListComponent;
