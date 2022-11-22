import {
  FC,
  useState,
  useRef,
  memo,
  createRef,
  RefObject,
  useEffect,
  useCallback,
} from "react";
import { useNavigate } from "react-router-dom";
import { Button, ScrollArea } from "@mantine/core";
import { BsCursor } from "react-icons/bs";
import { AiOutlinePlusSquare, AiFillDelete } from "react-icons/ai";
import { MdOutlineMoving } from "react-icons/md";
import Connector from "@devjaewoo/react-svg-connector";
import {
  Accessibility,
  Recommend,
  RoadmapItem,
  UploadRoadmap,
  useRoadmapCreate,
} from "src/apis/useRoadmap";
import { getCurrentPositionPixel } from "src/utils/PixelToRem";
import { ShapeDirection } from "@devjaewoo/react-svg-connector/lib/SvgConnector";
import RoadmapEditButton from "./_RoadmapEditButton";
import RoadmapNameItem from "./_RoadmapNameItem";
import RoadmapEditItem from "./_RoadmapEditItem";
import RoadmapEditItemHint from "./_RoadmapEditItemHint";
import RoadmapConnectorHint from "./_RoadmapConnectorHint";
import { EditMode, TEditMode, Position, TPosition } from "./types";
import RoadmapEditDrawer from "./_RoadmapEditDrawer";
import RoadmapEditCompleteDrawer from "./_RoadmapEditCompleteDrawer";

interface Props {
  defaultValue?: RoadmapItem[];
  height?: number;
}

interface ConnectorInfo {
  id: number;
  x: number;
  y: number;
  position: TPosition;
}

const RoadmapEdit: FC<Props> = ({ defaultValue = [], height = 36 }) => {
  const navigate = useNavigate();
  const [nextId, setNextId] = useState(
    defaultValue.length === 0
      ? 1
      : Math.max(...defaultValue.map((r) => r.id)) + 1
  ); // 가장 ID가 큰 element의 ID + 1

  const [scrollHeight, setScrollHeight] = useState<number>(height);

  const [editMode, setEditMode] = useState<TEditMode>(EditMode.Cursor);

  const roadmapItemRefs = useRef<{
    [key: number]: RefObject<HTMLDivElement>;
  }>({});

  const [roadmapItemList, setRoadmapItemList] =
    useState<RoadmapItem[]>(defaultValue);

  const [connectorStatus, setConnectorStatus] = useState<
    ConnectorInfo | undefined
  >(undefined);

  const [connectorHintId, setConnectorHintId] = useState<number | undefined>(
    undefined
  );

  const connectorFromRef = useRef<HTMLDivElement>(null);
  const connectorToRef = useRef<HTMLDivElement>(null);
  const [connectorHintPosition, setConnectorHintPosition] = useState<
    { x: number; y: number } | undefined
  >(undefined);
  const [connectorFixedHintPosition, setConnectorFixedHintPosition] = useState<
    ConnectorInfo | undefined
  >(undefined);

  const [editDrawerItem, setEditDrawerItem] = useState<RoadmapItem | undefined>(
    undefined
  );

  const [completeDrawerOpen, setCompleteDrawerOpen] = useState<boolean>(false);
  const [roadmap] = useState<UploadRoadmap>({
    title: "",
    image: undefined,
    accessibility: Accessibility.PUBLIC,
    roadmapItemList,
  });

  const titleRef = useRef<HTMLInputElement>(null);
  const [titleWarning, setTitleWarning] = useState<boolean>(false);
  const roadmapCreate = useRoadmapCreate();

  const updateEditMode = (mode: TEditMode) => {
    switch (mode) {
      case EditMode.Cursor:
        setEditMode(EditMode.Cursor);
        break;
      case EditMode.Add:
        setEditMode(EditMode.Add);
        break;
      case EditMode.Connect:
        setConnectorHintId(undefined);
        setConnectorStatus(undefined);
        setEditMode(EditMode.Connect);
        break;
      case EditMode.Delete:
        setEditMode(EditMode.Delete);
        break;
    }
  };

  const addRef = (key: number) => {
    if (roadmapItemRefs.current[key] !== undefined) {
      return roadmapItemRefs.current[key];
    }
    const newRef = createRef<HTMLDivElement>();
    roadmapItemRefs.current[key] = newRef;
    return newRef;
  };

  const removeRef = (key: number) => {
    delete roadmapItemRefs.current[key];
  };

  const updateScrollHeight = useCallback(() => {
    if (roadmapItemList.length === 0) {
      setScrollHeight(height);
      return;
    }

    setScrollHeight(Math.max(...roadmapItemList.map((r) => r.y)) + height / 2);
  }, [height, roadmapItemList]);

  const onRoadmapNameItemClick = (id: number) => {
    setEditDrawerItem(roadmapItemList.find((r) => r.id === id));
  };

  const onRoadmapItemClick = (id: number) => {
    switch (editMode) {
      case EditMode.Cursor:
        setEditDrawerItem(roadmapItemList.find((r) => r.id === id));
        break;

      case EditMode.Delete:
        setRoadmapItemList(roadmapItemList.filter((r) => r.id !== id));
        roadmapItemList
          .filter((r) => r.parentId === id)
          .forEach((r) => {
            r.connectionType = null;
            r.parentId = null;
          });
        removeRef(id);
        updateScrollHeight();
        break;
    }
  };

  const onRoadmapItemEnter = (id: number) => {
    if (editMode !== EditMode.Connect) return;
    if ((connectorStatus?.id || -1) === id) return;
    setConnectorHintId(id);
  };

  const onRoadmapItemLeave = (id: number) => {
    if (editMode !== EditMode.Connect) return;
    if (connectorHintId === id) {
      setConnectorHintId(undefined);
    }
  };

  const onRoadmapItemDrag = (_id: number, _x: number, _y: number) => {
    // ID의 x, y 좌표 업데이트
    updateScrollHeight();
  };

  const onRoadmapAddHintSelect = (x: number, y: number) => {
    switch (editMode) {
      case EditMode.Add: {
        const newRoadmapItem: RoadmapItem = {
          id: nextId,
          name: "Example",
          x,
          y,
          content: "",
          recommend: Recommend.RECOMMEND,
          isCleared: false,
          connectionType: null,
          parentId: null,
          referenceList: [],
        };
        setNextId((id) => id + 1);
        setRoadmapItemList([...roadmapItemList, newRoadmapItem]);
        break;
      }
    }
  };

  const onConnectorHintSelect = (
    id: number,
    x: number,
    y: number,
    position: TPosition
  ) => {
    if (connectorStatus === undefined) {
      const itemPosition = getCurrentPositionPixel(
        roadmapItemRefs.current[id].current
      );
      setConnectorHintId(undefined);
      setConnectorFixedHintPosition(undefined);
      setConnectorStatus({
        id,
        x: itemPosition.x + x,
        y: itemPosition.y + y,
        position,
      });
    } else {
      const child = roadmapItemList.find((r) => r.id === id);
      if (child === undefined || child.parentId !== null) return;

      child.parentId = connectorStatus.id;
      child.connectionType = getHintConnectorDirection(
        connectorStatus.position,
        position
      );

      setConnectorHintId(undefined);
      setConnectorFixedHintPosition(undefined);
      setConnectorStatus(undefined);
    }
  };

  const onConnectorHintEnter = (
    id: number,
    x: number,
    y: number,
    position: TPosition
  ) => {
    const itemPosition = getCurrentPositionPixel(
      roadmapItemRefs.current[id].current
    );

    setConnectorFixedHintPosition({
      id,
      x: itemPosition.x + x,
      y: itemPosition.y + y,
      position,
    });
  };

  const onConnectorHintLeave = (id: number) => {
    if (connectorFixedHintPosition?.id === id) {
      setConnectorFixedHintPosition(undefined);
    }
  };

  const getHintConnectorDirection = (
    from: TPosition | undefined,
    to: TPosition | undefined
  ) => {
    if (to !== undefined) {
      if (
        ((from === Position.top || from === Position.bottom) &&
          (to === Position.top || to === Position.bottom)) ||
        ((from === Position.left || from === Position.right) &&
          (to === Position.left || to === Position.right))
      ) {
        return `${from}2${to}` as ShapeDirection;
      }
    }

    switch (from) {
      case Position.top:
        return "t2b";
      case Position.bottom:
        return "b2t";
      case Position.left:
        return "l2r";
      case Position.right:
        return "r2l";
    }

    return "t2b";
  };

  useEffect(() => {
    updateScrollHeight();
  }, [updateScrollHeight]);

  const onUpload = () => {
    const title = titleRef.current?.value ?? "";
    if (title === "") {
      setTitleWarning(true);
      return;
    }

    roadmap.title = title;
    setCompleteDrawerOpen(true);
  };

  const onFinish = () => {
    roadmapCreate.mutate(roadmap, {
      onSuccess: (data) => {
        navigate(`/roadmaps/${data.roadmapId}`);
      },
      onError: (_error) => {},
    });
  };

  return (
    <div className="flex-1 flex flex-col w-full">
      <div className="flex flex-row justify-start items-center w-full h-20 mt-3 py-2 border-y">
        <input
          ref={titleRef}
          className={`flex-1 px-2 text-2xl focus-visible:outline-none ${
            titleWarning && "placeholder:text-red-400"
          }`}
          style={{ color: titleWarning ? "red" : "black" }}
          placeholder="로드맵 제목을 입력하세요"
          onChange={() => setTitleWarning(false)}
        />
        <Button className="w-24 h-14 bg-blue-600" onClick={onUpload}>
          완료
        </Button>
      </div>
      <div
        className="flex flex-row w-full border-b"
        style={{ height: `${height}rem` }}
      >
        <ScrollArea className="w-72 h-full border-r" scrollHideDelay={0}>
          <div className="p-2" style={{ height: `${height}rem` }}>
            {roadmapItemList.map((roadmapItem) => (
              <RoadmapNameItem
                key={roadmapItem.id}
                roadmapItem={roadmapItem}
                onClick={onRoadmapNameItemClick}
              />
            ))}
          </div>
        </ScrollArea>
        <div className="flex flex-col items-center flex-1 h-full">
          <div className="flex flex-row justify-center items-center rounded-b-lg px-1 py-2 text-2xl absolute z-30 text-white bg-blue-600">
            <RoadmapEditButton
              icon={<BsCursor />}
              onClick={() => updateEditMode(EditMode.Cursor)}
              highlight={editMode === EditMode.Cursor}
            />
            <RoadmapEditButton
              icon={<AiOutlinePlusSquare />}
              onClick={() => updateEditMode(EditMode.Add)}
              highlight={editMode === EditMode.Add}
            />
            <RoadmapEditButton
              icon={<MdOutlineMoving />}
              onClick={() => updateEditMode(EditMode.Connect)}
              highlight={editMode === EditMode.Connect}
            />
            <RoadmapEditButton
              icon={<AiFillDelete />}
              onClick={() => updateEditMode(EditMode.Delete)}
              highlight={editMode === EditMode.Delete}
              lastElement
            />
          </div>
          <ScrollArea
            className="h-full w-full bg-gray-50"
            scrollHideDelay={0}
            onMouseMove={(event) => {
              const target = event.target as HTMLDivElement;
              const { x, y } = getCurrentPositionPixel(target);

              setConnectorHintPosition(() => {
                return {
                  x: event.nativeEvent.offsetX + x,
                  y: event.nativeEvent.offsetY + y,
                };
              });
            }}
          >
            <div
              className="relative w-full"
              style={{
                minHeight: `${height}rem`,
                height: `${scrollHeight}rem`,
              }}
            >
              {roadmapItemList.map((roadmapItem) => (
                <RoadmapEditItem
                  refs={addRef(roadmapItem.id)}
                  key={roadmapItem.id}
                  roadmapItem={roadmapItem}
                  onClick={onRoadmapItemClick}
                  onEnter={onRoadmapItemEnter}
                  onLeave={onRoadmapItemLeave}
                  onDrag={onRoadmapItemDrag}
                  editMode={editMode}
                >
                  {editMode === EditMode.Connect &&
                    roadmapItem.id === connectorHintId &&
                    !(
                      connectorStatus !== undefined &&
                      roadmapItem.parentId !== null
                    ) && (
                      <RoadmapConnectorHint
                        id={connectorHintId}
                        refs={roadmapItemRefs.current[connectorHintId]}
                        onSelect={onConnectorHintSelect}
                        onHintEnter={onConnectorHintEnter}
                        onHintLeave={onConnectorHintLeave}
                        position={connectorStatus?.position}
                      />
                    )}
                </RoadmapEditItem>
              ))}
              {roadmapItemList
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
                      onClick={() => {
                        if (editMode === EditMode.Delete) {
                          r.connectionType = null;
                          r.parentId = null;
                        }
                      }}
                    />
                  );
                })}
              {editMode === EditMode.Add && (
                <RoadmapEditItemHint onSelect={onRoadmapAddHintSelect} />
              )}
              {editMode === EditMode.Connect && (
                <>
                  {connectorStatus && (
                    <div
                      ref={connectorFromRef}
                      className="absolute"
                      style={{
                        top: connectorStatus.y,
                        left: connectorStatus.x,
                      }}
                    />
                  )}
                  {connectorHintPosition && (
                    <div
                      ref={connectorToRef}
                      className="absolute w-4 h-4 -translate-x-1/2 -translate-y-1/2"
                      style={{
                        top: connectorFixedHintPosition
                          ? connectorFixedHintPosition.y
                          : connectorHintPosition.y,
                        left: connectorFixedHintPosition
                          ? connectorFixedHintPosition.x
                          : connectorHintPosition.x,
                      }}
                    />
                  )}
                  {connectorFromRef.current && connectorToRef.current && (
                    <Connector
                      el1={connectorFromRef.current}
                      el2={connectorToRef.current}
                      shape="narrow-s"
                      direction={getHintConnectorDirection(
                        connectorStatus?.position,
                        connectorFixedHintPosition?.position
                      )}
                      roundCorner
                      endArrow
                      stem={5}
                      className="bg-opacity-100 z-0"
                    />
                  )}
                </>
              )}
            </div>
          </ScrollArea>
        </div>
      </div>

      <RoadmapEditDrawer
        roadmapItem={editDrawerItem}
        onClose={() => setEditDrawerItem(undefined)}
      />
      <RoadmapEditCompleteDrawer
        opened={completeDrawerOpen}
        roadmap={roadmap}
        onClose={() => setCompleteDrawerOpen(false)}
        onFinish={onFinish}
      />
    </div>
  );
};

export default memo(RoadmapEdit);
