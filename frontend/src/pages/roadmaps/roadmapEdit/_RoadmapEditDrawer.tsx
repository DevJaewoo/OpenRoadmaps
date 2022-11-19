import { Drawer, ScrollArea, Select, Textarea } from "@mantine/core";
import { FC, forwardRef, useState, useEffect, useRef } from "react";
import { AiOutlinePlus, AiOutlineMinusCircle } from "react-icons/ai";
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

  const [nextId, setNextId] = useState(roadmapItem?.referenceList.length ?? 0);
  const [referenceList, setReferenceList] = useState<
    { id: number; reference: string }[]
  >(
    roadmapItem?.referenceList.map((reference, index) => {
      return { id: index, reference };
    }) ?? []
  );
  const [appending, setAppending] = useState<boolean>(false);
  const inputRef = useRef<HTMLInputElement>(null);

  useEffect(() => {
    setContent(roadmapItem?.content ?? "");
    setRecommend(roadmapItem?.recommend ?? Recommend.NONE);
    setReferenceList(
      roadmapItem?.referenceList.map((reference, index) => {
        return { id: index, reference };
      }) ?? []
    );
    setNextId(roadmapItem?.referenceList.length ?? 0);
  }, [roadmapItem]);

  useEffect(() => {
    if (inputRef && appending) {
      inputRef.current?.select();
    }
  }, [inputRef, appending]);

  useEffect(() => {
    if (roadmapItem) {
      roadmapItem.referenceList = referenceList.map((r) => r.reference);
    }
  }, [roadmapItem, referenceList]);

  const handleChangeRecommend = (item: TRecommend) => {
    if (roadmapItem) {
      roadmapItem.recommend = Recommend[item];
      setRecommend(item);
    }
  };

  const handleChangeContent: React.ChangeEventHandler<HTMLTextAreaElement> = (
    event
  ) => {
    if (roadmapItem) {
      roadmapItem.content = event.target.value;
      setContent(event.target.value);
    }
  };

  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter") {
      inputRef.current?.blur();
    }
  };

  const handleReferenceAdd = () => {
    if (inputRef.current && inputRef.current?.value !== "") {
      setReferenceList([
        ...referenceList,
        { id: nextId, reference: inputRef.current?.value },
      ]);
      setNextId(nextId + 1);
    }

    setAppending(false);
  };

  const handleReferenceRemove = (id: number) => {
    setReferenceList(referenceList.filter((r) => r.id !== id));
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
            onChange={handleChangeRecommend}
            value={recommend}
          />
        </div>
      </div>
      <Textarea
        className="mb-10"
        value={content}
        onChange={handleChangeContent}
        minRows={7}
        maxRows={7}
      />
      <h2 className="mb-4 text-2xl font-semibold">References</h2>
      <ScrollArea className="flex flex-col w-full h-80 p-2 border border-gray-100 rounded-lg">
        {referenceList.map((r) => (
          <div
            key={r.id}
            className="flex flex-row justify-between items-center w-full mb-2 pr-2"
          >
            <p className="w-full mr-2 p-2 border border-gray-100 rounded-lg">
              {r.reference}
            </p>
            <AiOutlineMinusCircle
              className="text-xl rounded-full cursor-pointer text-red-400 bg-white hover:text-white hover:bg-red-400 transition-colors"
              onClick={() => handleReferenceRemove(r.id)}
            />
          </div>
        ))}
        {!appending ? (
          <button
            type="button"
            className="flex justify-center items-center w-full p-2 border border-dashed border-blue-600 rounded-lg hover:bg-blue-600 hover:text-white transition-all"
            onClick={() => setAppending(true)}
          >
            <AiOutlinePlus />
          </button>
        ) : (
          <input
            ref={inputRef}
            className="w-full p-2 border border-gray-100 rounded-lg"
            placeholder="URL"
            onBlur={handleReferenceAdd}
            onKeyDown={handleKeyDown}
          />
        )}
      </ScrollArea>
    </Drawer>
  );
};

export default RoadmapEditDrawer;
