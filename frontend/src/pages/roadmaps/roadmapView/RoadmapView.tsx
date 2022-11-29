import Connector from "@devjaewoo/react-svg-connector";
import { ShapeDirection } from "@devjaewoo/react-svg-connector/lib/SvgConnector";
import { ScrollArea } from "@mantine/core";
import axios from "axios";
import {
  FC,
  createRef,
  RefObject,
  useRef,
  useState,
  useMemo,
  useEffect,
} from "react";
import { AiFillHeart } from "react-icons/ai";
import { Link, useParams } from "react-router-dom";
import { RoadmapItem, useRoadmap, useRoadmapLike } from "src/apis/useRoadmap";
import StableImage from "src/components/StableImage";
import NotFound from "src/pages/error/NotFound";
import RoadmapViewDrawer from "./_RoadmapViewDrawer";
import RoadmapViewItem from "./_RoadmapViewItem";

interface Props {}

const RoadmapView: FC<Props> = () => {
  const { roadmapId } = useParams();
  const { data: roadmap, error, isError } = useRoadmap(Number(roadmapId));

  const [drawerItem, setDrawerItem] = useState<RoadmapItem | undefined>(
    undefined
  );

  const roadmapLike = useRoadmapLike();

  const roadmapItemRefs = useRef<{
    [key: number]: RefObject<HTMLDivElement>;
  }>({});

  const globalTop = useMemo(
    () => Math.min(...(roadmap?.roadmapItemList.map((r) => r.y) ?? [0])) - 6,
    [roadmap]
  );

  const globalLeft = useMemo(
    () => Math.min(...(roadmap?.roadmapItemList.map((r) => r.x) ?? [0])) - 6,
    [roadmap]
  );

  const scrollHeight = useMemo(
    () =>
      Math.max(...(roadmap?.roadmapItemList.map((r) => r.y) ?? [0])) -
      globalTop +
      24,
    [roadmap, globalTop]
  );

  const [, redraw] = useState<number>(0);

  const addRef = (key: number) => {
    if (roadmapItemRefs.current[key] !== undefined) {
      return roadmapItemRefs.current[key];
    }
    const newRef = createRef<HTMLDivElement>();
    roadmapItemRefs.current[key] = newRef;
    return newRef;
  };

  useEffect(() => {
    redraw(Math.random());
  }, [roadmap, redraw]);

  const onLikeClick = () => {
    if (!roadmap) return;
    roadmapLike.mutate(
      { roadmapId: Number(roadmapId), liked: !roadmap.liked },
      {
        onSuccess: (data) => {
          roadmap.liked = data.liked;
          roadmap.likes = data.likes;
        },
      }
    );
  };

  const onRoadmapItemClick = (id: number) => {
    setDrawerItem(roadmap?.roadmapItemList.find((r) => r.id === id));
  };

  if (isError && axios.isAxiosError(error)) {
    if (error.response?.status === 404 || error.response?.status === 400) {
      return (
        <NotFound
          error="존재하지 않는 로드맵입니다."
          action="이전 화면으로 돌아가기"
          navigate={-1}
        />
      );
    }
    if (error.response?.status === 403) {
      return (
        <NotFound
          error="로드맵에 접근할 권한이 없습니다."
          action="이전 화면으로 돌아가기"
          navigate={-1}
        />
      );
    }
  }

  if (!roadmap) return null;
  return (
    <div className="flex flex-col w-full items-center">
      <div className="flex flex-col items-center w-full bg-gray-200">
        <div className="flex flex-col w-full max-w-7xl">
          <div className="flex flex-row w-full p-10">
            <StableImage
              className="w-80 h-60 rounded-2xl border-2 border-gray-800 object-fill"
              src={`/api/v1/images/${roadmap?.image}`}
            />
            <div className="flex flex-col justify-center ml-8 p-2">
              <h2 className="text-4xl whitespace-pre-line">{roadmap?.title}</h2>
              <div className="flex flex-col mt-2">
                <p className="flex flex-row items-center mt-2 text-xl">
                  <AiFillHeart
                    className={`text-2xl mr-0.5 cursor-pointer ${
                      roadmap?.liked ? "text-red-600" : "text-gray-600"
                    }`}
                    onClick={onLikeClick}
                  />
                  {roadmap?.likes}
                </p>
                <Link
                  to={`/clients/${roadmap?.clientId}`}
                  className="mt-0.5"
                >{`by. ${roadmap?.clientId}`}</Link>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div className="flex flex-col items-center w-full max-w-7xl m-2">
        <div
          className="flex flex-row w-full my-4"
          style={{
            height: `${scrollHeight + 1}rem`,
            minHeight: "24rem",
            maxHeight: "80rem",
          }}
        >
          <div className="flex flex-col items-center flex-1 h-full">
            <ScrollArea
              className="h-full w-full bg-gray-50 rounded-2xl"
              scrollHideDelay={0}
            >
              <div
                className="relative w-full"
                style={{
                  minHeight: "24rem",
                  height: `${scrollHeight}rem`,
                }}
              >
                {roadmap?.roadmapItemList.map((roadmapItem) => (
                  <RoadmapViewItem
                    refs={addRef(roadmapItem.id)}
                    key={roadmapItem.id}
                    roadmapItem={roadmapItem}
                    offsetTop={-globalTop}
                    offsetLeft={-globalLeft}
                    onClick={onRoadmapItemClick}
                  />
                ))}
                {roadmap?.roadmapItemList
                  .filter((r) => r.parentId)
                  .map((r) => {
                    if (r.parentId === null) return null;

                    const to = roadmapItemRefs.current[r.id].current;
                    const from = roadmapItemRefs.current[r.parentId]?.current;

                    if (!from || !to) return null;

                    return (
                      <Connector
                        key={r.id}
                        el1={from}
                        el2={to}
                        shape="narrow-s"
                        direction={r.connectionType as ShapeDirection}
                        roundCorner
                        endArrow
                        stem={5}
                        className="bg-opacity-100 z-0"
                      />
                    );
                  })}
              </div>
            </ScrollArea>
          </div>
        </div>
      </div>
      <RoadmapViewDrawer
        roadmapId={roadmap?.id ?? 0}
        roadmapItem={drawerItem}
        onClose={() => setDrawerItem(undefined)}
      />
    </div>
  );
};

export default RoadmapView;
