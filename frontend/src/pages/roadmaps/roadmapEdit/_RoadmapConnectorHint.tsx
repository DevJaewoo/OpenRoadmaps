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
  onHintEnter: (id: number, x: number, y: number, position: TPosition) => void;
  onHintLeave: (id: number) => void;
}

const RoadmapConnectorHintItem: FC<{
  x: number | string;
  y: number | string;
  onClick: (x: number, y: number) => void;
  onEnter: (x: number, y: number) => void;
  onLeave: () => void;
}> = ({ x, y, onClick, onEnter, onLeave }) => {
  const hintRef = useRef<HTMLDivElement>(null);

  const getCoord = () => {
    const { x: currentX, y: currentY } = getCurrentPositionPixel(
      hintRef.current
    );
    return {
      x: currentX + (hintRef.current?.offsetWidth || 0) / 2,
      y: currentY + (hintRef.current?.offsetHeight || 0) / 2,
    };
  };

  const handleClick = () => {
    const { x: currentX, y: currentY } = getCoord();
    onClick(currentX, currentY);
  };

  const handleEnter = () => {
    const { x: currentX, y: currentY } = getCoord();
    onEnter(currentX, currentY);
  };

  return (
    <div
      ref={hintRef}
      className="w-3 h-3 rounded-full z-10 absolute -translate-x-1/2 -translate-y-1/2 bg-black"
      onMouseEnter={handleEnter}
      onMouseLeave={onLeave}
      onClick={handleClick}
      style={{ top: y, left: x }}
      role="button"
      aria-hidden
    >
      {}
    </div>
  );
};

const RoadmapConnectorHint: FC<Props> = ({
  id,
  refs,
  onSelect,
  onHintEnter,
  onHintLeave,
}) => {
  if (!refs || !refs.current) return null;

  return (
    <>
      <RoadmapConnectorHintItem
        x="50%"
        y="0%"
        onClick={(x, y) => onSelect(id, x, y, Position.top)}
        onEnter={(x, y) => onHintEnter(id, x, y, Position.top)}
        onLeave={() => onHintLeave(id)}
      />
      <RoadmapConnectorHintItem
        x="50%"
        y="100%"
        onClick={(x, y) => onSelect(id, x, y, Position.bottom)}
        onEnter={(x, y) => onHintEnter(id, x, y, Position.bottom)}
        onLeave={() => onHintLeave(id)}
      />
      <RoadmapConnectorHintItem
        x="0%"
        y="50%"
        onClick={(x, y) => onSelect(id, x, y, Position.left)}
        onEnter={(x, y) => onHintEnter(id, x, y, Position.left)}
        onLeave={() => onHintLeave(id)}
      />
      <RoadmapConnectorHintItem
        x="100%"
        y="50%"
        onClick={(x, y) => onSelect(id, x, y, Position.right)}
        onEnter={(x, y) => onHintEnter(id, x, y, Position.right)}
        onLeave={() => onHintLeave(id)}
      />
    </>
  );
};

export default RoadmapConnectorHint;
