import { useMutation, useQuery } from "react-query";
import axiosInstance from "src/apis/axiosInstance";

interface CategoryList {
  categoryList: CategoryListItem;
}

interface CategoryListItem {
  id: number;
  name: string;
}

const fetchCategoryList = async (clientName: string): Promise<CategoryList> => {
  const response = await axiosInstance.get(
    `/api/v1/blog/${clientName}/categories`
  );
  return response.data;
};

const useCategoryList = (clientName: string) => {
  return useQuery(
    ["categoryList", clientName],
    () => fetchCategoryList(clientName),
    {}
  );
};

interface CategoryUpload {
  name: string;
}

const fetchCategoryUpload = async (
  category: CategoryUpload
): Promise<{ categoryId: number }> => {
  const response = await axiosInstance.post(
    "/api/v1/blog/categories",
    category
  );
  return response.data;
};

const useCategoryUpload = () => {
  return useMutation(fetchCategoryUpload, {});
};

const fetchCategoryDelete = async (
  categoryId: number
): Promise<{ categoryId: number }> => {
  const response = await axiosInstance.delete(
    `/api/v1/blog/categories/${categoryId}`
  );
  return response.data;
};

const useCategoryDelete = () => {
  return useMutation(fetchCategoryDelete, {});
};

export { useCategoryList, useCategoryUpload, useCategoryDelete };
