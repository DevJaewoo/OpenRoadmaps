import { FC, MouseEventHandler, RefObject, useState, ReactNode } from "react";
import Draggable, { DraggableEventHandler } from "react-draggable";
import { RoadmapItem } from "src/apis/useRoadmap";
import { getCurrentPositionRem } from "src/utils/PixelToRem";
import { RoadmapColor } from "src/utils/constants";

interface Props {
  refs: RefObject<HTMLDivElement>;
  roadmapItem: RoadmapItem;
  onClick: (id: number) => void;
  onDoubleClick: (id: number) => void;
  onEnter: (id: number) => void;
  onLeave: (id: number) => void;
  onDrag: (id: number, x: number, y: number) => void;
  disabled?: boolean;
  children?: ReactNode;
}

const RoadmapEditItem: FC<Props> = ({
  refs,
  roadmapItem,
  onClick,
  onDoubleClick,
  onEnter,
  onLeave,
  onDrag,
  disabled = true,
  children,
}) => {
  const [defaultCoord] = useState({ x: roadmapItem.x, y: roadmapItem.y });
  const [position, setPosition] = useState<
    { x: number; y: number } | undefined
  >(undefined);

  const handleClick: MouseEventHandler<HTMLDivElement> = () => {
    const { x, y } = getCurrentPositionRem(refs);

    if (position === undefined || (position.x === x && position.y === y)) {
      onClick(roadmapItem.id);
    }

    setPosition(undefined);
  };

  const handleDoubleClick = () => {
    onDoubleClick(roadmapItem.id);
  };

  const handleDrag: DraggableEventHandler = () => {
    const { x, y } = getCurrentPositionRem(refs);

    roadmapItem.x = x;
    roadmapItem.y = y;

    setPosition(getCurrentPositionRem(refs));
    onDrag(roadmapItem.id, x, y);
  };

  return (
    <Draggable onDrag={handleDrag} disabled={disabled}>
      <div
        ref={refs}
        className={`flex justify-center items-center absolute max-w-xs px-5 py-2 z-10 bg-white border-4 rounded-xl border-${
          RoadmapColor[roadmapItem.recommend]
        } ${disabled ? "cursor-default" : "cursor-pointer"}`}
        style={{ top: `${defaultCoord.y}rem`, left: `${defaultCoord.x}rem` }}
        onClick={handleClick}
        onDoubleClick={handleDoubleClick}
        onMouseEnter={() => onEnter(roadmapItem.id)}
        onMouseLeave={() => onLeave(roadmapItem.id)}
        role="button"
        aria-hidden
      >
        {roadmapItem.name}
        {children}
      </div>
    </Draggable>
  );
};

export default RoadmapEditItem;
