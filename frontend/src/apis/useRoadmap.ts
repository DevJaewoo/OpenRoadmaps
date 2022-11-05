import { useQuery } from "react-query";
import axiosInstance from "src/apis/axiosInstance";

export interface RoadmapSearch {
  clientId?: number;
  title?: string;
  official?: boolean;
  order?: "LIKE" | "LATEST";
  page: number;
}

export interface RoadmapListItem {
  id: number;
  title: string;
  image: string;
  accessibility: "PUBLIC" | "PROTECTED" | "PRIVATE";
  isOfficial: boolean;
  likes: number;
  createdDate: string;
  clientId: number;
}

export interface RoadmapList {
  content: RoadmapListItem[];
  totalElements: number;
  totalPages: number;
}

const fetchRoadmapList = async (query: RoadmapSearch): Promise<RoadmapList> => {
  const searchParams = new URLSearchParams();

  Object.entries(query).forEach(([k, v]) => {
    if (v !== undefined) searchParams.append(k, v.toString());
  });

  const response = await axiosInstance.get(`/api/v1/roadmaps?${searchParams}`);
  return response.data;
};

const useRoadmapList = (
  query: RoadmapSearch,
  onSuccess?: (data: RoadmapList) => void
) => {
  return useQuery(["roadmapList", query], () => fetchRoadmapList(query), {
    onSuccess,
  });
};

export { useRoadmapList };
