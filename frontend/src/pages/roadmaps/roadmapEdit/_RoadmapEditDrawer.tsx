import { Drawer, Select, Textarea } from "@mantine/core";
import { FC, forwardRef, useState, useEffect } from "react";
import { RoadmapItem, Recommend, TRecommend } from "src/apis/useRoadmap";
import RoadmapRecommendIcon from "./_RoadmapRecommendIcon";

interface Props {
  roadmapItem: RoadmapItem | undefined;
  onClose: () => void;
}

interface ItemProps {
  label: string;
  value: TRecommend;
}

const data: ItemProps[] = [
  {
    label: "Recommend",
    value: Recommend.RECOMMEND,
  },
  {
    label: "Alternative",
    value: Recommend.ALTERNATIVE,
  },
  {
    label: "Not Recommend",
    value: Recommend.NOT_RECOMMEND,
  },
  {
    label: "None",
    value: Recommend.NONE,
  },
];

const SelectItem = forwardRef<HTMLDivElement, ItemProps>(
  ({ value, label, ...others }, ref) => (
    <div ref={ref} {...others}>
      <div className="flex flex-row items-center">
        <RoadmapRecommendIcon recommend={value} className="mr-2" />
        <p>{label}</p>
      </div>
    </div>
  )
);

const RoadmapEditDrawer: FC<Props> = ({ roadmapItem, onClose }) => {
  const [content, setContent] = useState<string | undefined>(
    roadmapItem?.content
  );
  const [recommend, setRecommend] = useState<TRecommend | undefined>(
    roadmapItem?.recommend
  );

  useEffect(() => {
    setContent(roadmapItem?.content ?? "");
    setRecommend(roadmapItem?.recommend ?? Recommend.NONE);
  }, [roadmapItem]);

  const onChangeRecommend = (item: TRecommend) => {
    if (roadmapItem) {
      roadmapItem.recommend = Recommend[item];
      setRecommend(item);
    }
  };

  const onChangeContent: React.ChangeEventHandler<HTMLTextAreaElement> = (
    event
  ) => {
    if (roadmapItem) {
      roadmapItem.content = event.target.value;
      setContent(event.target.value);
    }
  };

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
          <RoadmapRecommendIcon recommend={recommend} className="mr-2" />
          <Select
            itemComponent={SelectItem}
            data={data}
            onChange={onChangeRecommend}
            value={recommend}
          />
        </div>
      </div>
      {roadmapItem && (
        <div>
          <Textarea
            value={content}
            onChange={onChangeContent}
            minRows={7}
            maxRows={7}
          />
        </div>
      )}
    </Drawer>
  );
};

export default RoadmapEditDrawer;
