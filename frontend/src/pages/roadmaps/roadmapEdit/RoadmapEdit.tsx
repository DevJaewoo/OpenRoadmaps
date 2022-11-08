import { FC } from "react";
import { Button, ScrollArea } from "@mantine/core";
import { BsCursor } from "react-icons/bs";
import { AiOutlinePlusSquare, AiFillDelete } from "react-icons/ai";
import { MdOutlineMoving } from "react-icons/md";
import RoadmapEditButton from "./_RoadmapEditButton";

const RoadmapEdit: FC = () => {
  return (
    <div className="flex-1 flex flex-col w-full">
      <div className="flex flex-row justify-start items-center w-full h-20 mt-3 py-2 border-y">
        <input
          className="flex-1 px-2 text-2xl focus-visible:outline-none"
          placeholder="로드맵 제목을 입력하세요"
        />
        <Button className="w-24 h-14 bg-blue-600">완료</Button>
      </div>
      <div className="flex flex-row flex-1 w-full border-b">
        <ScrollArea className="w-72 p-2 border-r">
          <div className="h-[26rem]">a</div>
        </ScrollArea>
        <div className="flex flex-col items-center flex-1">
          <div className="flex flex-row justify-center items-center rounded-b-lg px-1 py-2 text-2xl absolute z-10 text-white bg-blue-600">
            <RoadmapEditButton icon={<BsCursor />} />
            <RoadmapEditButton icon={<AiOutlinePlusSquare />} />
            <RoadmapEditButton icon={<MdOutlineMoving />} />
            <RoadmapEditButton icon={<AiFillDelete />} lastElement />
          </div>
          <ScrollArea className="flex-1 flex w-full bg-gray-50">
            <div className="min-h-full relative bg-blue-200">a</div>
          </ScrollArea>
        </div>
      </div>
    </div>
  );
};

export default RoadmapEdit;
