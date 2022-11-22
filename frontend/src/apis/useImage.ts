import { useMutation } from "react-query";
import axiosInstance from "./axiosInstance";

interface ImageUploadResponse {
  url: string;
}

const fetchImageUpload = async (image: File): Promise<ImageUploadResponse> => {
  const formData = new FormData();
  formData.append("image", image);

  const response = await axiosInstance.post("/api/v1/images/upload", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });

  return response.data;
};

const useImageUpload = () => {
  return useMutation(fetchImageUpload, {});
};

export { useImageUpload };
