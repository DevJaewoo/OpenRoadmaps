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
import { Button, ScrollArea } from "@mantine/core";
import { BsCursor } from "react-icons/bs";
import { AiOutlinePlusSquare, AiFillDelete } from "react-icons/ai";
import { MdOutlineMoving } from "react-icons/md";
import Connector from "@devjaewoo/react-svg-connector";
import { Recommend, RoadmapItem } from "src/apis/useRoadmap";
import { getCurrentPositionPixel } from "src/utils/PixelToRem";
import RoadmapEditButton from "./_RoadmapEditButton";
import RoadmapNameItem from "./_RoadmapNameItem";
import RoadmapEditItem from "./_RoadmapEditItem";
import RoadmapEditItemHint from "./_RoadmapEditItemHint";
import RoadmapConnectorHint, {
  Position,
  TPosition,
} from "./_RoadmapConnectorHint";

const EditMode = {
  Cursor: 0,
  Add: 1,
  Connect: 2,
  Delete: 3,
} as const;

type TEditMode = typeof EditMode[keyof typeof EditMode];

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

  const onRoadmapItemClick = (id: number) => {
    switch (editMode) {
      case EditMode.Cursor:
        // Drawer 열기
        break;

      case EditMode.Delete:
        removeRef(id);
        setRoadmapItemList(roadmapItemList.filter((r) => r.id !== id));
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

  const onRoadmapItemDoubleClick = (_id: number) => {
    // 텍스트 수정
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
          recommend: Recommend.RECOMMEND,
          isCleared: false,
          connectionType: null,
          parentId: null,
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
    const itemPosition = getCurrentPositionPixel(roadmapItemRefs.current[id]);
    setConnectorHintId(undefined);
    setConnectorStatus({
      id,
      x: itemPosition.x + x,
      y: itemPosition.y + y,
      position,
    });
  };

  const getHintConnectorDirection = () => {
    switch (connectorStatus?.position) {
      case Position.top:
        return "t2b";
      case Position.bottom:
        return "b2t";
      case Position.left:
        return "l2r";
      case Position.right:
        return "r2l";
      default:
        return "t2b";
    }
  };

  useEffect(() => {
    updateScrollHeight();
  }, [updateScrollHeight]);

  return (
    <div className="flex-1 flex flex-col w-full">
      <div className="flex flex-row justify-start items-center w-full h-20 mt-3 py-2 border-y">
        <input
          className="flex-1 px-2 text-2xl focus-visible:outline-none"
          placeholder="로드맵 제목을 입력하세요"
        />
        <Button className="w-24 h-14 bg-blue-600">완료</Button>
      </div>
      <div
        className="flex flex-row w-full border-b"
        style={{ height: `${height}rem` }}
      >
        <ScrollArea className="w-72 h-full border-r" scrollHideDelay={0}>
          <div className="p-2" style={{ height: `${height}rem` }}>
            {roadmapItemList.map((roadmapItem) => (
              <RoadmapNameItem key={roadmapItem.id} roadmapItem={roadmapItem} />
            ))}
          </div>
        </ScrollArea>
        <div className="flex flex-col items-center flex-1 h-full">
          <div className="flex flex-row justify-center items-center rounded-b-lg px-1 py-2 text-2xl absolute z-30 text-white bg-blue-600">
            <RoadmapEditButton
              icon={<BsCursor />}
              onClick={() => setEditMode(EditMode.Cursor)}
              highlight={editMode === EditMode.Cursor}
            />
            <RoadmapEditButton
              icon={<AiOutlinePlusSquare />}
              onClick={() => setEditMode(EditMode.Add)}
              highlight={editMode === EditMode.Add}
            />
            <RoadmapEditButton
              icon={<MdOutlineMoving />}
              onClick={() => setEditMode(EditMode.Connect)}
              highlight={editMode === EditMode.Connect}
            />
            <RoadmapEditButton
              icon={<AiFillDelete />}
              onClick={() => setEditMode(EditMode.Delete)}
              highlight={editMode === EditMode.Delete}
              lastElement
            />
          </div>
          <ScrollArea
            className="h-full w-full bg-gray-50"
            scrollHideDelay={0}
            onMouseMove={(event) => {
              setConnectorHintPosition({
                x: event.nativeEvent.offsetX,
                y: event.nativeEvent.offsetY,
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
                  onDoubleClick={onRoadmapItemDoubleClick}
                  onEnter={onRoadmapItemEnter}
                  onLeave={onRoadmapItemLeave}
                  onDrag={onRoadmapItemDrag}
                  disabled={editMode !== EditMode.Cursor}
                >
                  {editMode === EditMode.Connect &&
                    roadmapItem.id === connectorHintId && (
                      <RoadmapConnectorHint
                        id={connectorHintId}
                        refs={roadmapItemRefs.current[connectorHintId]}
                        onSelect={onConnectorHintSelect}
                      />
                    )}
                </RoadmapEditItem>
              ))}
              {editMode === EditMode.Add && (
                <RoadmapEditItemHint onSelect={onRoadmapAddHintSelect} />
              )}
              {editMode === EditMode.Connect && (
                <>
                  {connectorStatus && (
                    <div
                      ref={connectorFromRef}
                      className="w-3 h-3 rounded-full absolute bg-yellow-400"
                      style={{
                        top: connectorStatus.y,
                        left: connectorStatus.x,
                      }}
                    />
                  )}
                  {connectorHintPosition && (
                    <div
                      ref={connectorToRef}
                      className="w-3 h-3 rounded-full absolute bg-yellow-400"
                      style={{
                        top: connectorHintPosition.y,
                        left: connectorHintPosition.x,
                      }}
                    />
                  )}
                  {connectorFromRef.current && connectorToRef.current && (
                    <Connector
                      el1={connectorFromRef.current}
                      el2={connectorToRef.current}
                      shape="narrow-s"
                      direction={getHintConnectorDirection()}
                      roundCorner
                      endArrow
                      className="bg-opacity-100 z-0"
                    />
                  )}
                </>
              )}
            </div>
          </ScrollArea>
        </div>
      </div>
    </div>
  );
};

export default memo(RoadmapEdit);
