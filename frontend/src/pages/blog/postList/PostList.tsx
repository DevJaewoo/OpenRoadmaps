import { FC } from "react";
import { PostList, PostSearch, usePostList } from "src/apis/usePost";
import FlatPostListItemComponent from "./_FlatPostListItem";
import PostListItemComponent from "./_PostListItem";
import PostNotFound from "./_PostNotFound";

export const ListType = {
  NORMAL: "NORMAL",
  FLAT: "FLAT",
} as const;

export type TListType = typeof ListType[keyof typeof ListType];

interface Props {
  search: PostSearch;
  onSearch?: (data: PostList) => void;
  className?: string;
  type: TListType;
}

const PostListComponent: FC<Props> = ({
  search,
  onSearch,
  className,
  type,
}) => {
  const { data } = usePostList(search, onSearch);

  if (!data || data.content.length === 0) return <PostNotFound />;
  return (
    <div className={`flex flex-col items-center w-full ${className}`}>
      <div className="flex flex-row flex-wrap items-center w-full">
        {data?.content.map(
          (listItem) =>
            ({
              NORMAL: (
                <PostListItemComponent key={listItem.id} data={listItem} />
              ),
              FLAT: (
                <FlatPostListItemComponent key={listItem.id} data={listItem} />
              ),
            }[type])
        )}
      </div>
    </div>
  );
};

export default PostListComponent;
