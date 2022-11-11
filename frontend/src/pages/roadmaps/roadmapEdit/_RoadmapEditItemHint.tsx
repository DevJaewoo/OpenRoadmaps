import { FC, useState, MouseEventHandler, useRef } from "react";
import { pixelToRem } from "src/utils/PixelToRem";

interface Props {
  onSelect: (x: number, y: number) => void;
}

const RoadmapEditItemHint: FC<Props> = ({ onSelect }) => {
  const [hintPosition, setHintPosition] = useState<
    { x: number; y: number } | undefined
  >(undefined);
  const hintRef = useRef<HTMLDivElement>(null);

  const onClick: MouseEventHandler<HTMLButtonElement> = () => {
    if (hintPosition) {
      onSelect(
        pixelToRem(hintPosition.x - (hintRef.current?.offsetWidth || 0) / 2),
        pixelToRem(hintPosition.y - (hintRef.current?.offsetHeight || 0) / 2)
      );
      setHintPosition(undefined);
    }
  };

  const onMove: MouseEventHandler<HTMLButtonElement> = (event) => {
    setHintPosition({
      x: event.nativeEvent.offsetX,
      y: event.nativeEvent.offsetY,
    });
  };

  return (
    <>
      <button
        type="button"
        className="w-full h-full absolute z-20"
        onClick={onClick}
        onMouseMove={onMove}
      >
        {}
      </button>
      {hintPosition && (
        <div
          ref={hintRef}
          className="flex justify-center items-center absolute max-w-xs px-5 py-2 bg-white border-4 rounded-xl border-blue-600 z-10 -translate-x-1/2 -translate-y-1/2"
          style={{ top: `${hintPosition.y}px`, left: `${hintPosition.x}px` }}
        >
          Example
        </div>
      )}
    </>
  );
};

export default RoadmapEditItemHint;
