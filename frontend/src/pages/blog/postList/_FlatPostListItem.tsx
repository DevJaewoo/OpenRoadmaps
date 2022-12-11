import { FC } from "react";

import { FaHeart } from "react-icons/fa";
import { Link, useNavigate } from "react-router-dom";
import { PostListItem } from "src/apis/usePost";

interface Props {
  data: PostListItem;
}

const FlatPostListItemComponent: FC<Props> = ({ data }) => {
  const navigate = useNavigate();

  return (
    <div
      className="flex flex-col w-full my-1 p-2 bg-white rounded-md border transition-transform hover:cursor-pointer hover:scale-[1.02]"
      onClick={() => navigate(`/blog/@${data.clientName}/posts/${data.id}`)}
      role="button"
      aria-hidden
    >
      <div className="flex flex-row justify-between items-center">
        <h4 className="text-lg font-semibold">{data.title}</h4>
        <div className="flex flex-col items-end">
          <Link
            to={`/blog/@${data.clientName}`}
            onClick={(event) => {
              event.stopPropagation();
            }}
            className="text-sm"
          >{`by. @${data.clientName}`}</Link>
          <div className="flex flex-row justify-end items-center">
            <FaHeart className="mr-1 text-xs text-red-500" />
            <p className="">{data.likes}</p>
          </div>
        </div>
      </div>
    </div>
  );
};

export default FlatPostListItemComponent;
