import { FC, MouseEventHandler, RefObject, useState } from "react";
import Draggable, { DraggableEventHandler } from "react-draggable";
import { RoadmapItem } from "src/apis/useRoadmap";
import { getCurrentPositionRem } from "src/utils/PixelToRem";
import { RoadmapColor } from "src/utils/constants";

interface Props {
  refs: RefObject<HTMLButtonElement>;
  roadmapItem: RoadmapItem;
  onClick: (id: number) => void;
  onDoubleClick: (id: number) => void;
  onEnter: (id: number) => void;
  onLeave: (id: number) => void;
  onDrag: (id: number, x: number, y: number) => void;
  disabled?: boolean;
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
}) => {
  const [defaultCoord] = useState({ x: roadmapItem.x, y: roadmapItem.y });
  const [position, setPosition] = useState<
    { x: number; y: number } | undefined
  >(undefined);

  const handleClick: MouseEventHandler<HTMLButtonElement> = () => {
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
      <button
        ref={refs}
        type="button"
        className={`flex justify-center items-center absolute max-w-xs px-5 py-2 bg-white border-4 rounded-xl border-${
          RoadmapColor[roadmapItem.recommend]
        }`}
        style={{ top: `${defaultCoord.y}rem`, left: `${defaultCoord.x}rem` }}
        onClick={handleClick}
        onDoubleClick={handleDoubleClick}
        onMouseEnter={() => onEnter(roadmapItem.id)}
        onMouseLeave={() => onLeave(roadmapItem.id)}
      >
        {roadmapItem.name}
      </button>
    </Draggable>
  );
};

export default RoadmapEditItem;
