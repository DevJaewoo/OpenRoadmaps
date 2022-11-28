import { Drawer, ScrollArea, Textarea } from "@mantine/core";
import { FC } from "react";
import { Recommend, RecommendText, RoadmapItem } from "src/apis/useRoadmap";
import RoadmapRecommendIcon from "../roadmapEdit/_RoadmapRecommendIcon";

interface Props {
  roadmapItem: RoadmapItem | undefined;
  onClose: () => void;
}

const RoadmapViewDrawer: FC<Props> = ({ roadmapItem, onClose }) => {
  return (
    <Drawer
      opened={roadmapItem !== undefined}
      onClose={onClose}
      position="right"
      padding="xl"
      size="xl"
    >
      <div className="flex flex-row mb-4 justify-between items-center">
        <h2 className="text-2xl font-semibold">{roadmapItem?.name}</h2>
        <div className="flex flex-row items-center">
          <RoadmapRecommendIcon
            recommend={roadmapItem?.recommend}
            className="mr-2"
          />
          <p>{RecommendText[roadmapItem?.recommend ?? Recommend.NONE]}</p>
        </div>
      </div>
      <Textarea
        className="mb-10"
        value={roadmapItem?.content}
        minRows={7}
        maxRows={7}
      />
      <h2 className="mb-4 text-2xl font-semibold">References</h2>
      <ScrollArea className="flex flex-col w-full h-80 p-2 border border-gray-100 rounded-lg">
        {roadmapItem?.referenceList.map((r, index) => (
          <div
            // eslint-disable-next-line react/no-array-index-key
            key={`${r}${index}`}
            className="flex flex-row justify-between items-center w-full mb-2"
          >
            <a
              href={r}
              rel="noreferrer"
              target="_blank"
              className="w-full p-2 border border-gray-100 rounded-lg text-blue-600"
            >
              {r}
            </a>
          </div>
        ))}
      </ScrollArea>
    </Drawer>
  );
};

export default RoadmapViewDrawer;
