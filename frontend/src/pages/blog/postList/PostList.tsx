import { FC } from "react";
import { PostList, PostSearch, usePostList } from "src/apis/usePost";
import PostListItemComponent from "./_PostListItem";
import PostNotFound from "./_PostNotFound";

interface Props {
  search: PostSearch;
  onSearch?: (data: PostList) => void;
  className?: string;
}

const PostListComponent: FC<Props> = ({ search, onSearch, className }) => {
  const { data } = usePostList(search, onSearch);

  if (!data || data.content.length === 0) return <PostNotFound />;
  return (
    <div className={`flex flex-col items-center w-full ${className}`}>
      <div className="flex flex-row flex-wrap items-center w-full">
        {data?.content.map((listItem) => (
          <PostListItemComponent key={listItem.id} data={listItem} />
        ))}
      </div>
    </div>
  );
};

export default PostListComponent;
