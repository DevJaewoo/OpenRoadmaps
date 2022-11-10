import { useQuery } from "react-query";
import axiosInstance from "src/apis/axiosInstance";

export interface RoadmapSearch {
  client?: number;
  title?: string;
  official?: boolean;
  order?: "LIKES" | "LATEST";
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

type Accessibility = "PRIVATE" | "PROTECTED" | "PUBLIC";
type Recommend = "RECOMMEND" | "ALTERNATIVE" | "NOT_RECOMMEND";

export interface RoadmapItem {
  id: number;
  name: string;
  x: number;
  y: number;
  recommend: Recommend;
  isCleared: boolean;
  connectionType: string | null;
  parentId: number | null;
}

export interface Roadmap {
  id: number;
  title: string;
  image: string;
  accessibility: Accessibility;
  likes: number;
  createdDate: string;
  roadmapItemList: RoadmapItem[];
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
