import {
  FC,
  MouseEventHandler,
  RefObject,
  useState,
  ReactNode,
  useRef,
  useEffect,
} from "react";
import Draggable, { DraggableEventHandler } from "react-draggable";
import { RoadmapItem } from "src/apis/useRoadmap";
import { getCurrentPositionRem } from "src/utils/positionUtil";
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
  const [inputWidth, setInputWidth] = useState<number>(0);

  useEffect(() => {
    if (editing && inputRef) {
      inputRef.current?.select();
    }
  }, [inputRef, editing]);

  useEffect(() => {
    const tmp = document.createElement("span");
    tmp.className = "input-element tmp-element";
    tmp.style.whiteSpace = "pre";
    tmp.innerHTML = inputText
      .replace(/&/g, "&amp;")
      .replace(/</g, "&lt;")
      .replace(/>/g, "&gt;");
    document.body.appendChild(tmp);

    const width = tmp.getBoundingClientRect().width;
    document.body.removeChild(tmp);

    if (inputRef.current) {
      inputRef.current.style.width = `${width}px`;
    }

    setInputWidth(width);
  }, [inputRef, inputText]);

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
    const click = () => {
      if (position === undefined || (position.x === x && position.y === y)) {
        onClick(roadmapItem.id);
      }
      setPosition(undefined);
    };

    if (editMode === EditMode.Cursor) {
      if (singleClicked) return;
      singleClicked = true;

      setTimeout(() => {
        if (!singleClicked) return;
        singleClicked = false;

        click();
      }, 300);
    } else {
      click();
    }
  };

  const handleDoubleClick = () => {
    if (!singleClicked) return;
    singleClicked = false;

    if (editMode === EditMode.Cursor) {
      setEditing(true);
    }
  };

  const handleTextChange = (event: React.ChangeEvent<HTMLInputElement>) => {
    roadmapItem.name = event.target.value;
    setInputText(event.target.value);
  };

  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter") {
      inputRef.current?.blur();
    }
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
        className={`flex justify-center items-center absolute max-w-xs px-5 py-2 z-10 bg-white border-4 rounded-xl ${
          editMode === EditMode.Cursor || editMode === EditMode.Delete
            ? "cursor-pointer"
            : "cursor-default"
        }`}
        style={{
          top: `${defaultCoord.y}rem`,
          left: `${defaultCoord.x}rem`,
          borderColor: RoadmapColor[roadmapItem.recommend],
        }}
        onClick={handleClick}
        onDoubleClick={handleDoubleClick}
        onMouseEnter={() => onEnter(roadmapItem.id)}
        onMouseLeave={() => onLeave(roadmapItem.id)}
        role="button"
        aria-hidden
      >
        {editing ? (
          <input
            className="max-w-sm"
            ref={inputRef}
            type="text"
            value={inputText}
            onChange={handleTextChange}
            onKeyDown={handleKeyDown}
            onBlur={handleBlur}
            style={{ width: `${inputWidth}px` }}
          />
        ) : (
          <p className="max-w-sm select-none">{inputText}</p>
        )}
        {children}
      </div>
    </Draggable>
  );
};

export default RoadmapEditItem;
