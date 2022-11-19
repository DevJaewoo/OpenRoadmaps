import { Drawer, Select, Textarea } from "@mantine/core";
import { FC, forwardRef, useState } from "react";
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
        <RoadmapRecommendIcon size={2} recommend={value} className="mr-2" />
        <p>{label}</p>
      </div>
    </div>
  )
);

const RoadmapEditDrawer: FC<Props> = ({ roadmapItem, onClose }) => {
  const [content, setContent] = useState<string>(roadmapItem?.content ?? "");
  const [selectedValue, setSelectedValue] = useState<TRecommend>(
    roadmapItem?.recommend ?? Recommend.NONE
  );

  const onChange = (item: TRecommend) => {
    if (roadmapItem) {
      roadmapItem.recommend = Recommend[item];
      setSelectedValue(item);
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
          <RoadmapRecommendIcon
            size={2}
            recommend={selectedValue}
            className="mr-2"
          />
          <Select
            itemComponent={SelectItem}
            data={data}
            onChange={onChange}
            value={selectedValue}
          />
        </div>
      </div>
      {roadmapItem && (
        <div>
          <Textarea
            value={content}
            onChange={(e) => setContent(e.target.value)}
            minRows={7}
            maxRows={7}
          />
        </div>
      )}
    </Drawer>
  );
};

export default RoadmapEditDrawer;
