import { Drawer, ScrollArea, Textarea } from "@mantine/core";
import { FC, useEffect, useState } from "react";
import {
  Recommend,
  RecommendText,
  RoadmapItem,
  useRoadmapItemClear,
} from "src/apis/useRoadmap";
import RoadmapRecommendIcon from "../roadmapEdit/_RoadmapRecommendIcon";

interface Props {
  roadmapId: number;
  roadmapItem: RoadmapItem | undefined;
  onClose: () => void;
}

const RoadmapViewDrawer: FC<Props> = ({ roadmapId, roadmapItem, onClose }) => {
  const [itemCleared, setItemCleared] = useState<boolean>(false);
  const roadmapItemClear = useRoadmapItemClear();

  useEffect(() => {
    setItemCleared(roadmapItem?.isCleared ?? false);
  }, [roadmapItem, setItemCleared]);

  const onRoadmapItemClearClick = (clear: boolean) => {
    roadmapItemClear.mutate(
      {
        roadmapId,
        roadmapItemId: roadmapItem?.id ?? 0,
        isCleared: clear,
      },
      {
        onSuccess: (data) => {
          if (roadmapItem?.id === data.roadmapItemId) {
            roadmapItem.isCleared = data.isCleared;
            setItemCleared(data.isCleared);
          }
        },
      }
    );
  };

  return (
    <Drawer
      opened={roadmapItem !== undefined}
      onClose={onClose}
      position="right"
      padding="xl"
      size="xl"
    >
      <div className="flex flex-row justify-between items-center">
        <h2 className="text-2xl font-semibold">{roadmapItem?.name}</h2>
        <div className="flex flex-row items-center">
          <RoadmapRecommendIcon
            recommend={roadmapItem?.recommend}
            className="mr-2"
          />
          <p className="mr-1">
            {RecommendText[roadmapItem?.recommend ?? Recommend.NONE]}
          </p>
        </div>
      </div>
      <Textarea
        className="mt-4"
        value={roadmapItem?.content}
        onChange={() => {}}
        minRows={7}
        maxRows={7}
      />
      <div className="flex flex-row justify-end mt-4">
        <button
          type="button"
          className={`flex justify-center items-center w-20 h-8 text-sm rounded-lg border-2 ${
            itemCleared
              ? "border-blue-600 bg-blue-200"
              : "border-yellow-400 bg-yellow-100"
          }`}
          onClick={() => onRoadmapItemClearClick(!itemCleared)}
        >
          {itemCleared ? "공부 완료!" : "공부중..."}
        </button>
      </div>
      <h2 className="mt-6 text-2xl font-semibold">References</h2>
      <ScrollArea className="flex flex-col w-full h-80 mt-4 p-2 border border-gray-100 rounded-lg">
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
