import { FC } from "react";
import { FaArrowRight } from "react-icons/fa";
import { Route, Routes } from "react-router-dom";
import { OutlinedButton } from "src/components/button/VariantButtons";
import Header from "src/components/Header";
import NotFound from "src/pages/error/NotFound";
import BlogPost from "./post/BlogPost";

const BlogMain: FC<{}> = () => {
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
      </div>
    </div>
  );
};

const Blog: FC<{}> = () => {
  return (
    <Routes>
      <Route path="/" element={<BlogMain />} />
      <Route path="/posts/new" element={<BlogPost />} />
      <Route path="*" element={<NotFound />} />
    </Routes>
  );
};

export default Blog;
