import { Suspense, useState, useRef, KeyboardEvent } from "react";
import { Input, Select, Pagination } from "@mantine/core";
import { FaSearch } from "react-icons/fa";
import { RoadmapList, RoadmapSearch } from "src/apis/useRoadmap";
import Header from "src/components/Header";
import RoadmapListComponent from "./roadmapList/RoadmapList";

const Roadmaps: React.FC<{}> = () => {
  const titleRef = useRef<HTMLInputElement>(null);

  const [search, setSearch] = useState<RoadmapSearch>({
    title: undefined,
    official: undefined,
    page: 0,
    order: "LATEST",
  });

  const [totalPage, setTotalPage] = useState(1);

  const onSearch = (data: RoadmapList) => {
    setTotalPage(data.totalPages);
  };

  const onOrderChange = (order: string) => {
    let currentOrder: "LATEST" | "LIKES" | undefined;
    if (order !== "LATEST" && order !== "LIKES") {
      currentOrder = undefined;
    } else {
      currentOrder = order;
    }

    setSearch({ ...search, order: currentOrder });
  };

  const onKeyDown = (event: KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter") {
      let title = titleRef.current?.value.trim();
      if (title === "") title = undefined;

      setSearch({ ...search, title });
    }
  };

  return (
    <div className="flex flex-col items-center">
      <div className="flex flex-col items-center w-full max-w-7xl">
        <Header
          title="Roadmaps"
          text={
            "개발 공부는 하고 싶은데 어떻게 시작해야 할지 막막하신가요?\n로드맵을 따라 기초부터 차근차근 공부해보세요!"
          }
        />
        <div className="flex flex-col items-center w-full mt-5 p-3 bg-gray-300 rounded-lg">
          <div className="flex flex-row w-full justify-between px-2">
            <Input ref={titleRef} icon={<FaSearch />} onKeyDown={onKeyDown} />
            <div className="flex flex-row items-center">
              <p className="mr-2 text-lg font-semibold">정렬</p>
              <Select
                value={search.order}
                data={[
                  { value: "LATEST", label: "최신 순" },
                  { value: "LIKES", label: "좋아요 순" },
                ]}
                onChange={onOrderChange}
              />
            </div>
          </div>
          <Suspense>
            <RoadmapListComponent
              search={search}
              onSearch={onSearch}
              className="mt-2"
            />
          </Suspense>
        </div>
        <Pagination
          className="mt-5"
          page={search.page + 1}
          onChange={(page) => setSearch({ ...search, page: page - 1 })}
          total={totalPage}
        />
      </div>
    </div>
  );
};

export default Roadmaps;
