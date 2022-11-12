import { FC, RefObject } from "react";
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
  refs: RefObject<HTMLButtonElement> | undefined;
  onSelect: (id: number, position: TPosition) => void;
}

const RoadmapConnectorHintItem: FC<{
  x: number;
  y: number;
  onClick: () => void;
}> = ({ x, y, onClick }) => {
  return (
    <button
      type="button"
      className="w-3 h-3 rounded-full z-10 absolute -translate-x-1/2 -translate-y-1/2 bg-black"
      onClick={onClick}
      style={{ top: y, left: x }}
    >
      {}
    </button>
  );
};

const RoadmapConnectorHint: FC<Props> = ({ id, refs, onSelect }) => {
  if (!refs || !refs.current) return null;

  const { x, y } = getCurrentPositionPixel(refs);

  return (
    <>
      <RoadmapConnectorHintItem
        x={x + refs.current.offsetWidth / 2}
        y={y}
        onClick={() => onSelect(id, Position.top)}
      />
      <RoadmapConnectorHintItem
        x={x + refs.current.offsetWidth / 2}
        y={y + refs.current.offsetHeight}
        onClick={() => onSelect(id, Position.bottom)}
      />
      <RoadmapConnectorHintItem
        x={x}
        y={y + refs.current.offsetHeight / 2}
        onClick={() => onSelect(id, Position.left)}
      />
      <RoadmapConnectorHintItem
        x={x + refs.current.offsetWidth}
        y={y + refs.current.offsetHeight / 2}
        onClick={() => onSelect(id, Position.right)}
      />
    </>
  );
};

export default RoadmapConnectorHint;
