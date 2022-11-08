import { FC } from "react";

interface Props {
  icon: JSX.Element;
  className?: string;
  onClick?: React.MouseEventHandler<HTMLButtonElement>;
  lastElement?: boolean | undefined;
}

const RoadmapEditButton: FC<Props> = ({
  icon,
  className,
  onClick,
  lastElement = false,
}) => {
  return (
    <button
      type="button"
      className={`box-content px-1 cursor-pointer ${
        !lastElement && "border-r"
      } ${className}`}
      onClick={onClick}
    >
      {icon}
    </button>
  );
};

export default RoadmapEditButton;
