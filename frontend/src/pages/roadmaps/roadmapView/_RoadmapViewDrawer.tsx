import {
  Drawer,
  Pagination,
  ScrollArea,
  Select,
  Textarea,
} from "@mantine/core";
import { FC, useEffect, useState } from "react";
import { PostList, PostOrder, PostSearch, TPostOrder } from "src/apis/usePost";
import {
  Recommend,
  RecommendText,
  RoadmapItem,
  useRoadmapItemClear,
} from "src/apis/useRoadmap";
import PostListComponent from "src/pages/blog/postList/PostList";
import RoadmapRecommendIcon from "../roadmapEdit/_RoadmapRecommendIcon";

interface Props {
  roadmapId: number;
  roadmapItem: RoadmapItem | undefined;
  clientName: string;
  onClose: () => void;
}

const RoadmapViewDrawer: FC<Props> = ({
  roadmapId,
  roadmapItem,
  clientName,
  onClose,
}) => {
  const [itemCleared, setItemCleared] = useState<boolean>(false);
  const roadmapItemClear = useRoadmapItemClear();

  const [totalPage, setTotalPage] = useState(1);
  const [search, setSearch] = useState<PostSearch>({
    clientName,
    roadmapItemId: roadmapItem?.id,
    order: PostOrder.LATEST,
    page: 0,
  });

  useEffect(() => {
    setItemCleared(roadmapItem?.isCleared ?? false);
  }, [roadmapItem, setItemCleared]);

  useEffect(() => {
    if (!roadmapItem) return;

    if (search.roadmapItemId !== roadmapItem.id) {
      search.roadmapItemId = roadmapItem.id;
    }
  }, [search, roadmapItem]);

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

  const onSearch = (data: PostList) => {
    setTotalPage(data.totalPages);
  };

  const onOrderChange = (order: TPostOrder) => {
    let currentOrder: TPostOrder | undefined;
    if (!Object.values(PostOrder).includes(order)) {
      currentOrder = undefined;
    } else {
      currentOrder = order;
    }

    setSearch({ ...search, order: currentOrder });
  };

  const onOfficialChange = () => {
    setSearch({
      ...search,
      clientName: search.clientName ? undefined : clientName,
    });
  };

  const onPageChange = (page: number) => {
    if (page > 0 && page <= totalPage) {
      setSearch({ ...search, page: page - 1 });
    }
  };

  return (
    <Drawer
      opened={roadmapItem !== undefined}
      onClose={onClose}
      position="right"
      padding="xl"
      size="xl"
      className="overflow-y-auto"
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
      <h2 className="mt-6 text-2xl font-semibold">Posts</h2>
      <div className="flex flex-col items-center mt-4">
        <div className="flex flex-row w-full justify-between items-center">
          <button
            type="button"
            className={`flex justify-center items-center w-24 h-9 p-2 border-2 text-sm rounded-lg ${
              search.clientName
                ? "bg-gray-200 border-gray-400 text-gray-500"
                : "bg-blue-400 border-blue-600"
            }`}
            onClick={onOfficialChange}
          >
            전체 글 보기
          </button>
          <Select
            value={search.order}
            data={[
              { value: PostOrder.LATEST, label: "최신 순" },
              { value: PostOrder.LIKES, label: "좋아요 순" },
            ]}
            onChange={onOrderChange}
          />
        </div>
        <div className="flex flex-col items-center w-full mt-2 p-2 border border-gray-100 rounded-lg">
          <PostListComponent search={search} onSearch={onSearch} type="FLAT" />
          <Pagination
            className="mt-5"
            page={search.page + 1}
            onChange={onPageChange}
            total={totalPage}
          />
        </div>
      </div>
    </Drawer>
  );
};

export default RoadmapViewDrawer;
