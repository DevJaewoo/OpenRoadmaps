import { Input, Pagination, Select } from "@mantine/core";
import { FC, useRef, useState, KeyboardEvent } from "react";
import { FaArrowRight, FaSearch } from "react-icons/fa";
import { Route, Routes } from "react-router-dom";
import { PostList, PostOrder, PostSearch, TPostOrder } from "src/apis/usePost";
import { OutlinedButton } from "src/components/button/VariantButtons";
import Header from "src/components/Header";
import NotFound from "src/pages/error/NotFound";
import BlogHome from "./home/BlogHome";
import BlogPost from "./post/BlogPost";
import PostListComponent from "./postList/PostList";
import BlogPostView from "./postView/BlogPostView";

const BlogMain: FC<{}> = () => {
  const titleRef = useRef<HTMLInputElement>(null);
  const [search, setSearch] = useState<PostSearch>({
    title: undefined,
    page: 0,
    order: PostOrder.LATEST,
  });

  const [totalPage, setTotalPage] = useState(1);

  const onKeyDown = (event: KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter") {
      let title = titleRef.current?.value.trim();
      if (title === "") title = undefined;

      setSearch({ ...search, title });
    }
  };

  const onSearch = (data: PostList) => {
    setTotalPage(data.totalPages);
  };

  const onOrderChange = (order: TPostOrder) => {
    let currentOrder: TPostOrder | undefined;
    if (!Object.values(PostOrder).includes(order)) {
      currentOrder = undefined;
    } else {
      currentOrder = order;
    }

    setSearch({ ...search, order: currentOrder });
  };

  const onPageChange = (page: number) => {
    if (page > 0 && page <= totalPage) {
      setSearch({ ...search, page: page - 1 });
    }
  };

  return (
    <div className="flex flex-col items-center">
      <div className="flex flex-col items-center w-full max-w-7xl">
        <div className="flex flex-col items-center w-full">
          <Header title="Blog" text="나만의 지식과 노하우를 공유해보세요!" />
          <OutlinedButton
            className="flex flex-row justify-center items-center mt-5"
            type="link"
            to="/blog/posts/new"
            text={
              <>
                글 작성하기
                <FaArrowRight className="ml-1" />
              </>
            }
          />
        </div>
        <div className="flex flex-col items-center w-full mt-2">
          <div className="flex flex-col items-center w-full mt-5 p-3 bg-gray-300 rounded-lg">
            <div className="flex flex-row w-full justify-between px-2">
              <Input ref={titleRef} icon={<FaSearch />} onKeyDown={onKeyDown} />
              <div className="flex flex-row items-center">
                <p className="mr-2 text-lg font-semibold">정렬</p>
                <Select
                  value={search.order}
                  data={[
                    { value: PostOrder.LATEST, label: "최신 순" },
                    { value: PostOrder.LIKES, label: "좋아요 순" },
                  ]}
                  onChange={onOrderChange}
                />
              </div>
            </div>
            <PostListComponent
              search={search}
              onSearch={onSearch}
              className="mt-2"
              type="NORMAL"
            />
          </div>
          <Pagination
            className="mt-5"
            page={search.page + 1}
            onChange={onPageChange}
            total={totalPage}
          />
        </div>
      </div>
    </div>
  );
};

const Blog: FC<{}> = () => {
  return (
    <Routes>
      <Route path="/" element={<BlogMain />} />
      <Route path="/posts/new" element={<BlogPost />} />
      <Route path="/:clientName" element={<BlogHome />} />
      <Route
        path="/:clientName/categories/:categoryId"
        element={<BlogHome />}
      />
      <Route path="/:clientName/posts/:postId" element={<BlogPostView />} />
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
};

export default Blog;
