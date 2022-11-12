import { FC, RefObject } from "react";

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
  x: number | string;
  y: number | string;
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

  return (
    <>
      <RoadmapConnectorHintItem
        x="50%"
        y="0%"
        onClick={() => onSelect(id, Position.top)}
      />
      <RoadmapConnectorHintItem
        x="50%"
        y="100%"
        onClick={() => onSelect(id, Position.bottom)}
      />
      <RoadmapConnectorHintItem
        x="0%"
        y="50%"
        onClick={() => onSelect(id, Position.left)}
      />
      <RoadmapConnectorHintItem
        x="100%"
        y="50%"
        onClick={() => onSelect(id, Position.right)}
      />
    </>
  );
};

export default RoadmapConnectorHint;
