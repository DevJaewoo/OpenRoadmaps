import { FC } from "react";
import { Button, ScrollArea } from "@mantine/core";
import { AiOutlinePlusSquare } from "react-icons/ai";
import { MdOutlineMoving } from "react-icons/md";
import Header from "src/components/Header";

const RoadmapCreate: FC = () => {
  return (
    <div className="flex flex-col flex-1 items-center w-full">
      <div className="flex flex-col flex-1 items-center w-full max-w-7xl">
        <Header title="로드맵 만들기" text="" />
        <div className="flex flex-row justify-start items-center w-full h-20 mt-3 py-2 border-y">
          <input
            className="flex-1 px-2 text-2xl focus-visible:outline-none"
            placeholder="로드맵 제목을 입력하세요"
          />
          <Button className="w-24 h-14 bg-blue-600">완료</Button>
        </div>
        <div className="flex flex-row flex-1 w-full border-b">
          <ScrollArea className="w-72 p-2 border-r">
            <div className="min-h-[2rem]">a</div>
          </ScrollArea>
          <div className="flex flex-col items-center flex-1">
            <div className="flex flex-row justify-center items-center rounded-b-lg px-1 py-2 text-2xl absolute z-10 text-white bg-blue-600">
              <AiOutlinePlusSquare className="box-content px-1 border-r cursor-pointer" />
              <MdOutlineMoving className="box-content px-1 cursor-pointer" />
            </div>
            <ScrollArea className="flex-1 w-full bg-gray-50" />
          </div>
        </div>
      </div>
    </div>
  );
};

export default RoadmapCreate;
