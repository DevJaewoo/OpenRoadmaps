import { Pagination } from "@mantine/core";
import { FC, useState } from "react";
import { useParams } from "react-router-dom";
import { useClient } from "src/apis/useClient";
import { RoadmapList, RoadmapSearch } from "src/apis/useRoadmap";
import ProfileImage from "src/components/ProfileImage";
import RoadmapListComponent from "../roadmaps/roadmapList/RoadmapList";

const Profile: FC<{}> = () => {
  const { clientId } = useParams();
  const { data: clientInfo } = useClient(Number(clientId));

  const [search, setSearch] = useState<RoadmapSearch>({
    client: Number(clientId),
    page: 0,
  });

  const [totalPage, setTotalPage] = useState(1);

  const onSearch = (data: RoadmapList) => {
    setTotalPage(data.totalPages);
  };

  const onPageChange = (page: number) => {
    if (page > 0 && page <= totalPage) {
      setSearch({ ...search, page: page - 1 });
    }
  };

  return (
    <div className="flex flex-col items-center w-full">
      <div className="flex flex-row justify-center w-full max-w-7xl pt-12">
        <div className="flex flex-col items-center w-96">
          <ProfileImage
            className="w-64 h-64"
            clientId={clientInfo?.id}
            url={clientInfo?.picture}
          />
          <p className="mt-5 text-2xl font-semibold">{clientInfo?.name}</p>
        </div>
        <div className="flex flex-col flex-1">
          <div className="flex flex-col items-center w-full">
            <h2 className="w-full p-3 rounded-md border border-gray-400 bg-gray-100">
              로드맵 목록
            </h2>
            <RoadmapListComponent search={search} onSearch={onSearch} />
            <Pagination
              className="mt-3"
              page={search.page + 1}
              total={totalPage}
              onChange={onPageChange}
            />
          </div>
        </div>
      </div>
    </div>
  );
};

export default Profile;
