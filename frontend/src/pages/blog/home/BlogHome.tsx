import { Input, Pagination } from "@mantine/core";
import { FC, useRef, useState, KeyboardEvent } from "react";
import { FaSearch } from "react-icons/fa";
import { useNavigate, useParams, useSearchParams } from "react-router-dom";
import { useCategoryList } from "src/apis/useCategory";
import { useClientSearch } from "src/apis/useClient";
import { PostList, PostSearch } from "src/apis/usePost";
import ProfileImage from "src/components/ProfileImage";
import PostListComponent from "../postList/PostList";

interface Props {}

const BlogHome: FC<Props> = () => {
  const { clientName, categoryId } = useParams();
  const [params] = useSearchParams();
  const navigate = useNavigate();

  const { data: clientInfo } = useClientSearch({
    name: clientName?.substring(1),
  });

  const { data: categoryList } = useCategoryList(
    clientName?.substring(1) ?? ""
  );

  const titleRef = useRef<HTMLInputElement>(null);
  const [search, setSearch] = useState<PostSearch>({
    clientName: clientName?.substring(1),
    categoryId: Number(categoryId) || undefined,
    page: Number(params.get("page")) ?? 1,
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

  const onPageChange = (page: number) => {
    if (page > 0 && page <= totalPage) {
      setSearch({ ...search, page: page - 1 });
    }
  };

  const onCategorySelect = (selectedCategoryId: number | undefined) => {
    setSearch({ ...search, categoryId: selectedCategoryId });
    navigate(
      `/blog/${clientName}${
        selectedCategoryId ? `/categories/${selectedCategoryId}` : ""
      }`
    );
  };

  if (!clientInfo || !categoryList) return null;
  return (
    <div className="flex flex-col items-center w-full">
      <div className="flex flex-col items-center w-full max-w-7xl">
        <div className="flex flex-row items-center w-full mt-10 px-10">
          <ProfileImage url={clientInfo.picture} className="w-52 h-52" />
          <h2 className="ml-16 text-3xl font-bold">{clientInfo.name}</h2>
        </div>
        <div className="flex flex-row w-full mt-16 px-10">
          <div className="flex flex-col w-52">
            <h2
              className="text-lg font-bold cursor-pointer"
              onClick={() => onCategorySelect(undefined)}
              aria-hidden
            >
              카테고리 목록
            </h2>
            <div className="flex flex-col w-full mt-2">
              {categoryList.categoryList.map((category) => (
                <p
                  key={category.id}
                  className={`mt-1 cursor-pointer ${
                    search.categoryId === category.id
                      ? "font-semibold"
                      : "font-normal"
                  }`}
                  onClick={() => onCategorySelect(category.id)}
                  aria-hidden
                >
                  {category.name}
                </p>
              ))}
            </div>
          </div>
          <div className="flex flex-col flex-1 ml-16">
            <div className="flex flex-col items-center w-full">
              <div className="flex flex-col items-center w-full p-3 bg-gray-300 rounded-lg">
                <div className="flex flex-row w-full justify-start px-2">
                  <Input
                    ref={titleRef}
                    icon={<FaSearch />}
                    onKeyDown={onKeyDown}
                  />
                </div>
                <PostListComponent
                  search={search}
                  onSearch={onSearch}
                  className="mt-2"
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
      </div>
    </div>
  );
};

export default BlogHome;
