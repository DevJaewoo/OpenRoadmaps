import Connector from "@devjaewoo/react-svg-connector";
import { ShapeDirection } from "@devjaewoo/react-svg-connector/lib/SvgConnector";
import { Image, ScrollArea } from "@mantine/core";
import { FC, createRef, RefObject, useRef, useState, useMemo } from "react";
import { AiFillHeart } from "react-icons/ai";
import { Link, useParams } from "react-router-dom";
import { RoadmapItem, useRoadmap, useRoadmapLike } from "src/apis/useRoadmap";
import { remToPixel } from "src/utils/positionUtil";
import RoadmapViewDrawer from "./_RoadmapViewDrawer";
import RoadmapViewItem from "./_RoadmapViewItem";

interface Props {}

const RoadmapView: FC<Props> = () => {
  const { roadmapId } = useParams();
  const { data: roadmap } = useRoadmap(Number(roadmapId));

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

  const addRef = (key: number) => {
    if (roadmapItemRefs.current[key] !== undefined) {
      return roadmapItemRefs.current[key];
    }
    const newRef = createRef<HTMLDivElement>();
    roadmapItemRefs.current[key] = newRef;
    return newRef;
  };

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

  return (
    <div className="flex flex-col w-full items-center">
      <div className="flex flex-col items-center w-full bg-gray-200">
        <div className="flex flex-col w-full max-w-7xl">
          <div className="flex flex-row w-full p-10">
            <div className="w-80">
              <Image
                radius="md"
                height={remToPixel(60 / 4)}
                src={`/api/v1/images/${roadmap?.image}`}
                alt=""
              />
            </div>
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
