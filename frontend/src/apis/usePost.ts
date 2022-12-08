import { objectToParams } from "src/utils/utils";
import axiosInstance from "src/apis/axiosInstance";
import { useMutation, useQuery } from "react-query";
import { TAccessibility } from "src/utils/constants";

interface PostUploadRequest {
  title: string;
  content: string;
  image: string;
  accessibility: TAccessibility;
  categoryId: number;
  roadmapItemId: number;
}

const fetchPostUpload = async (
  post: PostUploadRequest
): Promise<{ postId: number }> => {
  const response = await axiosInstance.post("/api/v1/blog/posts", post);
  return response.data;
};

const usePostUpload = () => {
  return useMutation(fetchPostUpload, {});
};

interface PostSearch {
  title: string;
  clientName: string;
  categoryId: number;
  roadmapItemId: number;
  page: number;
}

interface PostList {
  content: PostListItem[];
  totalElements: number;
  totalPages: number;
}

interface PostListItem {
  id: number;
  title: string;
  accessibility: TAccessibility;
  likes: number;
  categoryId: number;
  roadmapItemId: number;
  clientId: number;
}

const fetchPostList = async (query: PostSearch): Promise<PostList> => {
  const response = await axiosInstance.get(
    `/api/v1/blog/posts?${objectToParams(query)}`
  );
  return response.data;
};

const usePostList = (query: PostSearch) => {
  return useQuery(["postSearch", query], () => fetchPostList(query), {});
};

interface Post {
  id: number;
  title: string;
  content: string;
  likes: number;
  views: number;
  categoryId: number;
  roadmapItemId: number;
  clientId: number;
}

const fetchPost = async (clientName: string, postId: number): Promise<Post> => {
  const response = await axiosInstance.get(
    `/api/v1/${clientName}/posts/${postId}`
  );
  return response.data;
};

const usePost = (clientName: string, postId: number) => {
  return useQuery(
    ["post", { clientName, postId }],
    () => fetchPost(clientName, postId),
    {}
  );
};

export { usePostUpload, usePostList, usePost };
