import { FC } from "react";
import axios from "axios";
import DOMPurify from "dompurify";
import moment from "moment";
import { Link, useParams } from "react-router-dom";
import { usePost } from "src/apis/usePost";
import NotFound from "src/pages/error/NotFound";

interface Props {}

const BlogPostView: FC<Props> = () => {
  const { clientName, postId } = useParams();
  const { data, isError, error } = usePost(
    clientName?.substring(1) ?? "",
    Number(postId ?? 0)
  );

  if (!clientName?.startsWith("@")) {
    return <NotFound />;
  }

  if (isError && axios.isAxiosError(error)) {
    if (error.response?.status === 404 || error.response?.status === 400) {
      return (
        <NotFound
          error="존재하지 않는 글입니다."
          action="이전 화면으로 돌아가기"
          navigate={-1}
        />
      );
    }
    if (error.response?.status === 403) {
      return (
        <NotFound
          error="글에 접근할 권한이 없습니다."
          action="이전 화면으로 돌아가기"
          navigate={-1}
        />
      );
    }
  }

  if (!data) return null;
  return (
    <div className="flex flex-col items-center w-full">
      <div className="flex flex-col items-center w-full py-24 bg-gray-100">
        <div className="flex flex-col items-center w-full max-w-7xl px-8">
          <h1 className="w-full text-4xl">{data.title}</h1>
          <div className="flex flex-row justify-start items-center w-full mt-2">
            <Link
              to={`/blog/${clientName}`}
              className="text-gray-600"
            >{`by. ${clientName}`}</Link>
            <p className="ml-2 text-xs text-gray-600">
              {moment(data.createdDate).local().format("YYYY년 MM월 DD일")}
            </p>
          </div>
        </div>
      </div>
      <div
        className="flex flex-col w-full max-w-7xl min-h-[30rem] p-8"
        // eslint-disable-next-line react/no-danger
        dangerouslySetInnerHTML={{ __html: DOMPurify.sanitize(data.content) }}
      >
        {}
      </div>
    </div>
  );
};

export default BlogPostView;
