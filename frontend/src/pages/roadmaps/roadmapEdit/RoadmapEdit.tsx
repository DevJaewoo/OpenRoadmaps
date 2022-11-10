import { FC, useState, useRef, memo, createRef, RefObject } from "react";
import { Button, ScrollArea } from "@mantine/core";
import { BsCursor } from "react-icons/bs";
import { AiOutlinePlusSquare, AiFillDelete } from "react-icons/ai";
import { MdOutlineMoving } from "react-icons/md";
import { RoadmapItem } from "src/apis/useRoadmap";
import RoadmapEditButton from "./_RoadmapEditButton";
import RoadmapNameItem from "./_RoadmapNameItem";
import RoadmapEditItem from "./_RoadmapEditItem";

const EditMode = {
  Cursor: 0,
  Add: 1,
  Connect: 2,
  Delete: 3,
} as const;

type TEditMode = typeof EditMode[keyof typeof EditMode];

interface Props {
  defaultValue?: RoadmapItem[];
  height?: string;
}

const testItem1: RoadmapItem = {
  id: 1,
  name: "Test1",
  isCleared: false,
  x: 20,
  y: 30,
  recommend: "RECOMMEND",
  connectionType: null,
  parentId: null,
};

const testItem2: RoadmapItem = {
  id: 2,
  name: "Test2",
  isCleared: false,
  x: 30,
  y: 20,
  recommend: "RECOMMEND",
  connectionType: null,
  parentId: null,
};

const RoadmapEdit: FC<Props> = ({ defaultValue = [], height = "36rem" }) => {
  const [, setEditMode] = useState<TEditMode>(EditMode.Cursor);
  const roadmapItemRefs = useRef<{
    [key: number]: RefObject<HTMLButtonElement>;
  }>({});
  const [roadmapItemList, setRoadmapItemList] = useState<RoadmapItem[]>([
    testItem1,
    testItem2,
  ]);

  const addRef = (key: number) => {
    console.log(`AddRef with key ${key}`);
    if (roadmapItemRefs.current[key] !== undefined) {
      return roadmapItemRefs.current[key];
    }
    const newRef = createRef<HTMLButtonElement>();
    roadmapItemRefs.current[key] = newRef;
    return newRef;
  };

  const removeRef = (key: number) => {
    delete roadmapItemRefs.current[key];
  };

  const onRoadmapItemClick = (id: number) => {
    // Drawer 열기
    console.log(`Clicked ${id}`);
  };

  const onRoadmapItemDrag = (id: number, x: number, y: number) => {
    // ID의 x, y 좌표 업데이트
    console.log(roadmapItemList.find((r) => r.id === id));
  };

  return (
    <div className="flex-1 flex flex-col w-full">
      <div className="flex flex-row justify-start items-center w-full h-20 mt-3 py-2 border-y">
        <input
          className="flex-1 px-2 text-2xl focus-visible:outline-none"
          placeholder="로드맵 제목을 입력하세요"
        />
        <Button className="w-24 h-14 bg-blue-600">완료</Button>
      </div>
      <div className={`flex flex-row h-[${height}] w-full border-b`}>
        <ScrollArea className="w-72 h-full border-r" scrollHideDelay={0}>
          <div className={`p-2 min-h-[${height}]`}>
            {roadmapItemList.map((roadmapItem) => (
              <RoadmapNameItem key={roadmapItem.id} roadmapItem={roadmapItem} />
            ))}
          </div>
        </ScrollArea>
        <div className="flex flex-col items-center flex-1 h-full">
          <div className="flex flex-row justify-center items-center rounded-b-lg px-1 py-2 text-2xl absolute z-10 text-white bg-blue-600">
            <RoadmapEditButton
              icon={<BsCursor />}
              onClick={() => setEditMode(EditMode.Cursor)}
            />
            <RoadmapEditButton
              icon={<AiOutlinePlusSquare />}
              onClick={() => setEditMode(EditMode.Add)}
            />
            <RoadmapEditButton
              icon={<MdOutlineMoving />}
              onClick={() => setEditMode(EditMode.Connect)}
            />
            <RoadmapEditButton
              icon={<AiFillDelete />}
              onClick={() => setEditMode(EditMode.Delete)}
              lastElement
            />
          </div>
          <ScrollArea className="h-full w-full bg-gray-50" scrollHideDelay={0}>
            <div className={`relative min-h-[${height}] w-full`}>
              {roadmapItemList.map((roadmapItem) => (
                <RoadmapEditItem
                  refs={addRef(roadmapItem.id)}
                  key={roadmapItem.id}
                  roadmapItem={roadmapItem}
                  onClick={onRoadmapItemClick}
                  onDrag={onRoadmapItemDrag}
                />
              ))}
            </div>
          </ScrollArea>
        </div>
      </div>
    </div>
  );
};

export default memo(RoadmapEdit);
