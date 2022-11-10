import { useState, useRef, KeyboardEvent } from "react";
import { Route, Routes } from "react-router-dom";
import { Input, Select, Pagination } from "@mantine/core";
import { FaSearch, FaArrowRight } from "react-icons/fa";
import {
  RoadmapList,
  RoadmapOrder,
  RoadmapSearch,
  TRoadmapOrder,
} from "src/apis/useRoadmap";
import { OutlinedButton } from "src/components/button/VariantButtons";
import Header from "src/components/Header";
import NotFound from "src/pages/error/NotFound";
import withAuth from "src/hoc/withAuth";
import RoadmapListComponent from "./roadmapList/RoadmapList";
import RoadmapCreate from "./RoadmapCreate";

const RoadmapMain: React.FC<{}> = () => {
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

  const onOrderChange = (order: TRoadmapOrder) => {
    let currentOrder: TRoadmapOrder | undefined;
    if (!Object.values(RoadmapOrder).includes(order)) {
      currentOrder = undefined;
    } else {
      currentOrder = order;
    }

    setSearch({ ...search, order: currentOrder });
  };

  const onPageChange = (page: number) => {
    if (page > 0 && page <= totalPage) {
      setSearch({ ...search, page: page - 1 });
    }
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
        <div className="flex flex-col items-center w-full">
          <Header
            title="Roadmaps"
            text={
              "개발 공부는 하고 싶은데 어떻게 시작해야 할지 막막하신가요?\n로드맵을 따라 기초부터 차근차근 공부해보세요!"
            }
          />
          <OutlinedButton
            className="flex flex-row justify-center items-center mt-5"
            type="link"
            to="/roadmaps/create"
            text={
              <>
                로드맵 만들기
                <FaArrowRight className="ml-1" />
              </>
            }
          />
        </div>
        <div className="flex flex-col items-center w-full mt-5 p-3 bg-gray-300 rounded-lg">
          <div className="flex flex-row w-full justify-between px-2">
            <Input ref={titleRef} icon={<FaSearch />} onKeyDown={onKeyDown} />
            <div className="flex flex-row items-center">
              <p className="mr-2 text-lg font-semibold">정렬</p>
              <Select
                value={search.order}
                data={[
                  { value: RoadmapOrder.LATEST, label: "최신 순" },
                  { value: RoadmapOrder.LIKES, label: "좋아요 순" },
                ]}
                onChange={onOrderChange}
              />
            </div>
          </div>
          <RoadmapListComponent
            search={search}
            onSearch={onSearch}
            className="mt-2"
          />
        </div>
        <Pagination
          className="mt-5"
          page={search.page + 1}
          onChange={onPageChange}
          total={totalPage}
        />
      </div>
    </div>
  );
};

const Roadmaps = () => {
  const AuthRoadmapCreate = withAuth(RoadmapCreate, true);
  return (
    <Routes>
      <Route path="/" element={<RoadmapMain />} />
      <Route path="/create" element={<AuthRoadmapCreate />} />
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
};

export default Roadmaps;
