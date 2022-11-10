import { FC, MouseEventHandler, RefObject, useState } from "react";
import Draggable, { DraggableEventHandler } from "react-draggable";
import { RoadmapItem } from "src/apis/useRoadmap";
import { pixelToRem } from "src/utils/PixelToRem";

interface Props {
  refs: RefObject<HTMLButtonElement>;
  roadmapItem: RoadmapItem;
  onClick: (id: number) => void;
  onDrag: (id: number, x: number, y: number) => void;
}

const RoadmapEditItem: FC<Props> = ({ refs, roadmapItem, onClick, onDrag }) => {
  const [defaultCoord] = useState({ x: roadmapItem.x, y: roadmapItem.y });
  const [position, setPosition] = useState<
    { x: number; y: number } | undefined
  >(undefined);

  const getCurrentPosition = () => {
    let x = 0;
    let y = 0;

    if (refs.current) {
      const style = window.getComputedStyle(refs.current);
      const matrix = new DOMMatrixReadOnly(style.transform);
      x = matrix.m41;
      y = matrix.m42;
    }

    x = pixelToRem(x + (refs.current?.offsetLeft || 0));
    y = pixelToRem(y + (refs.current?.offsetTop || 0));

    return { x, y };
  };

  const handleClick: MouseEventHandler<HTMLButtonElement> = () => {
    const { x, y } = getCurrentPosition();

    if (position === undefined || (position.x === x && position.y === y)) {
      onClick(roadmapItem.id);
    }

    setPosition(undefined);
  };

  const handleDoubleClick = () => {
    // console.log("handleDoubleClick");
  };

  const handleDrag: DraggableEventHandler = () => {
    const { x, y } = getCurrentPosition();

    roadmapItem.x = x;
    roadmapItem.y = y;

    setPosition(getCurrentPosition());
    onDrag(roadmapItem.id, x, y);
  };

  return (
    <Draggable onDrag={handleDrag}>
      <button
        ref={refs}
        type="button"
        className="flex justify-center items-center absolute p-2 bg-blue-400"
        style={{ top: `${defaultCoord.y}rem`, left: `${defaultCoord.x}rem` }}
        onClick={handleClick}
        onDoubleClick={handleDoubleClick}
      >
        {roadmapItem.name}
      </button>
    </Draggable>
  );
};

export default RoadmapEditItem;
