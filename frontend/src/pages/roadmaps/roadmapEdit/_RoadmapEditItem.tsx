import {
  FC,
  MouseEventHandler,
  RefObject,
  useState,
  ReactNode,
  useRef,
  ChangeEvent,
  useEffect,
} from "react";
import Draggable, { DraggableEventHandler } from "react-draggable";
import { RoadmapItem } from "src/apis/useRoadmap";
import { getCurrentPositionRem } from "src/utils/PixelToRem";
import { RoadmapColor } from "src/utils/constants";
import { EditMode, TEditMode } from "./types";

interface Props {
  refs: RefObject<HTMLDivElement>;
  roadmapItem: RoadmapItem;
  onClick: (id: number) => void;
  onEnter: (id: number) => void;
  onLeave: (id: number) => void;
  onDrag: (id: number, x: number, y: number) => void;
  editMode: TEditMode;
  children?: ReactNode;
}

const RoadmapEditItem: FC<Props> = ({
  refs,
  roadmapItem,
  onClick,
  onEnter,
  onLeave,
  onDrag,
  editMode,
  children,
}) => {
  const [defaultCoord] = useState({ x: roadmapItem.x, y: roadmapItem.y });
  const [position, setPosition] = useState<
    { x: number; y: number } | undefined
  >(undefined);
  const [editing, setEditing] = useState<boolean>(false);

  const inputRef = useRef<HTMLInputElement>(null);
  const [inputText, setInputText] = useState<string>(roadmapItem.name);

  useEffect(() => {
    if (editing && inputRef) {
      inputRef.current?.select();
    }
  }, [inputRef, editing]);

  let singleClicked = false;

  const handleDrag: DraggableEventHandler = () => {
    const { x, y } = getCurrentPositionRem(refs.current);

    roadmapItem.x = x;
    roadmapItem.y = y;

    setPosition(getCurrentPositionRem(refs.current));
    onDrag(roadmapItem.id, x, y);
  };

  const handleClick: MouseEventHandler<HTMLDivElement> = () => {
    const { x, y } = getCurrentPositionRem(refs.current);

    singleClicked = true;
    setTimeout(() => {
      if (!singleClicked) return;
      singleClicked = false;

      if (position === undefined || (position.x === x && position.y === y)) {
        onClick(roadmapItem.id);
      }

      setPosition(undefined);
    }, 300);
  };

  const handleDoubleClick = () => {
    if (!singleClicked) return;
    singleClicked = false;

    setEditing(true);
  };

  const handleTextChange = (event: ChangeEvent<HTMLInputElement>) => {
    roadmapItem.name = event.target.value;
    setInputText(event.target.value);
  };

  const handleBlur = () => {
    if (inputText === "") {
      setInputText("Example");
    }

    setEditing(false);
  };

  return (
    <Draggable
      onDrag={handleDrag}
      disabled={editMode !== EditMode.Cursor || editing}
    >
      <div
        ref={refs}
        className={`flex justify-center items-center absolute max-w-xs px-5 py-2 z-10 bg-white border-4 rounded-xl border-${
          RoadmapColor[roadmapItem.recommend]
        } ${
          editMode === EditMode.Cursor || editMode === EditMode.Delete
            ? "cursor-pointer"
            : "cursor-default"
        }`}
        style={{ top: `${defaultCoord.y}rem`, left: `${defaultCoord.x}rem` }}
        onClick={handleClick}
        onDoubleClick={handleDoubleClick}
        onMouseEnter={() => onEnter(roadmapItem.id)}
        onMouseLeave={() => onLeave(roadmapItem.id)}
        role="button"
        aria-hidden
      >
        {editing ? (
          <input
            ref={inputRef}
            type="text"
            value={inputText}
            onChange={handleTextChange}
            onBlur={handleBlur}
          />
        ) : (
          <p>{inputText}</p>
        )}
        {children}
      </div>
    </Draggable>
  );
};

export default RoadmapEditItem;
