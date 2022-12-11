import { objectToParams } from "src/utils/utils";
import axiosInstance from "src/apis/axiosInstance";
import { useMutation, useQuery } from "react-query";
import { TAccessibility } from "src/utils/constants";

export interface PostUploadRequest {
  id?: number;
  title: string;
  content: string;
  image?: string;
  accessibility: TAccessibility;
  categoryId?: number;
  roadmapItemId?: number;
}

const fetchPostUpload = async (
  post: PostUploadRequest
): Promise<{ postId: number }> => {
  const response = await axiosInstance.post("/api/v1/blog/posts", post);
  return response.data;
};

export const PostOrder = {
  LATEST: "LATEST",
  LIKES: "LIKES",
} as const;
export type TPostOrder = typeof PostOrder[keyof typeof PostOrder];

const usePostUpload = () => {
  return useMutation(fetchPostUpload, {});
};

export interface PostSearch {
  title?: string;
  clientName?: string;
  categoryId?: number;
  roadmapItemId?: number;
  order?: TPostOrder;
  page: number;
}

export interface PostList {
  content: PostListItem[];
  totalElements: number;
  totalPages: number;
}

export interface PostListItem {
  id: number;
  title: string;
  image: string;
  accessibility: TAccessibility;
  likes: number;
  categoryId: number;
  roadmapItemId: number;
  clientId: number;
  clientName: string;
  createdDate: string;
  modifiedDate: string;
}

const fetchPostList = async (query: PostSearch): Promise<PostList> => {
  const response = await axiosInstance.get(
    `/api/v1/blog/posts?${objectToParams(query)}`
  );
  return response.data;
};

const usePostList = (
  query: PostSearch,
  onSuccess?: (data: PostList) => void
) => {
  return useQuery(["postSearch", query], () => fetchPostList(query), {
    onSuccess,
  });
};

export interface Post {
  id: number;
  title: string;
  content: string;
  likes: number;
  views: number;
  categoryId: number;
  roadmapItemId: number;
  clientId: number;
  clientName: string;
  createdDate: string;
  modifiedDate: string;
}

const fetchPost = async (clientName: string, postId: number): Promise<Post> => {
  const response = await axiosInstance.get(
    `/api/v1/blog/${clientName}/posts/${postId}`
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
