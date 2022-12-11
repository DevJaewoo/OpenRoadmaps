import { FC } from "react";

import { FaHeart } from "react-icons/fa";
import { Link, useNavigate } from "react-router-dom";
import { PostListItem } from "src/apis/usePost";
import StableImage from "src/components/StableImage";

interface Props {
  data: PostListItem;
}

const PostListItemComponent: FC<Props> = ({ data }) => {
  const navigate = useNavigate();

  return (
    <div
      className="flex flex-col w-[32%] mx-[calc((100%-(32%*3))/6)] my-1 p-3 bg-white rounded-md border transition-transform hover:cursor-pointer hover:scale-105"
      onClick={() => navigate(`/blog/@${data.clientName}/posts/${data.id}`)}
      role="button"
      aria-hidden
    >
      <StableImage
        src={`/api/v1/images/${data.image}`}
        alt={data.image}
        className="w-full h-48 rounded-md object-cover"
      />
      <h4 className="mt-2 text-lg font-semibold">{data.title}</h4>
      <div className="flex flex-row justify-between mt-1">
        <div className="flex flex-row items-center">
          <FaHeart className="mr-1 text-xs text-red-500" />
          <p className="">{data.likes}</p>
        </div>
        <Link
          to={`/blog/@${data.clientName}`}
          onClick={(event) => {
            event.stopPropagation();
          }}
          className="text-sm"
        >{`by. @${data.clientName}`}</Link>
      </div>
    </div>
  );
};

export default PostListItemComponent;
