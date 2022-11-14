import { FC } from "react";

interface Props {
  icon: JSX.Element;
  className?: string;
  onClick?: React.MouseEventHandler<HTMLButtonElement>;
  highlight?: boolean | undefined;
  lastElement?: boolean | undefined;
}

const RoadmapEditButton: FC<Props> = ({
  icon,
  className,
  onClick,
  highlight = false,
  lastElement = false,
}) => {
  return (
    <button
      type="button"
      className={`box-content px-1 cursor-pointer ${
        !lastElement && "border-r"
      } ${highlight ? "text-yellow-400" : "text-white"} ${className}`}
      onClick={onClick}
    >
      {icon}
    </button>
  );
};

export default RoadmapEditButton;
