import { FC, useRef, RefObject } from "react";
import { getCurrentPositionPixel } from "src/utils/PixelToRem";

export const Position = {
  top: "t",
  bottom: "b",
  left: "l",
  right: "r",
} as const;

export type TPosition = typeof Position[keyof typeof Position];

interface Props {
  id: number;
  refs: RefObject<HTMLDivElement> | undefined;
  onSelect: (id: number, x: number, y: number, position: TPosition) => void;
}

const RoadmapConnectorHintItem: FC<{
  x: number | string;
  y: number | string;
  onClick: (x: number, y: number) => void;
}> = ({ x, y, onClick }) => {
  const hintRef = useRef(null);

  const handleClick = () => {
    const { x: currentX, y: currentY } = getCurrentPositionPixel(hintRef);
    onClick(currentX, currentY);
  };

  return (
    <div
      ref={hintRef}
      className="w-3 h-3 rounded-full z-10 absolute -translate-x-1/2 -translate-y-1/2 bg-black"
      onClick={handleClick}
      style={{ top: y, left: x }}
      role="button"
      aria-hidden
    >
      {}
    </div>
  );
};

const RoadmapConnectorHint: FC<Props> = ({ id, refs, onSelect }) => {
  if (!refs || !refs.current) return null;

  return (
    <>
      <RoadmapConnectorHintItem
        x="50%"
        y="0%"
        onClick={(x, y) => onSelect(id, x, y, Position.top)}
      />
      <RoadmapConnectorHintItem
        x="50%"
        y="100%"
        onClick={(x, y) => onSelect(id, x, y, Position.bottom)}
      />
      <RoadmapConnectorHintItem
        x="0%"
        y="50%"
        onClick={(x, y) => onSelect(id, x, y, Position.left)}
      />
      <RoadmapConnectorHintItem
        x="100%"
        y="50%"
        onClick={(x, y) => onSelect(id, x, y, Position.right)}
      />
    </>
  );
};

export default RoadmapConnectorHint;
