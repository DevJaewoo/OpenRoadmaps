import { useMutation, useQuery } from "react-query";
import axiosInstance from "src/apis/axiosInstance";

export interface RoadmapSearch {
  client?: number;
  title?: string;
  official?: boolean;
  order?: TRoadmapOrder;
  page: number;
}

export interface RoadmapListItem {
  id: number;
  title: string;
  image: string;
  accessibility: TAccessibility;
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

export const RoadmapOrder = {
  LATEST: "LATEST",
  LIKES: "LIKES",
} as const;
export type TRoadmapOrder = typeof RoadmapOrder[keyof typeof RoadmapOrder];

export const Accessibility = {
  PRIVATE: "PRIVATE",
  PROTECTED: "PROTECTED",
  PUBLIC: "PUBLIC",
} as const;
export type TAccessibility = typeof Accessibility[keyof typeof Accessibility];

export const Recommend = {
  RECOMMEND: "RECOMMEND",
  ALTERNATIVE: "ALTERNATIVE",
  NOT_RECOMMEND: "NOT_RECOMMEND",
  NONE: "NONE",
} as const;
export type TRecommend = typeof Recommend[keyof typeof Recommend];

export interface RoadmapItem {
  id: number;
  name: string;
  x: number;
  y: number;
  content: string;
  recommend: TRecommend;
  isCleared: boolean;
  connectionType: string | null;
  parentId: number | null;
  referenceList: string[];
}

export interface Roadmap {
  id: number;
  title: string;
  image: string;
  accessibility: TAccessibility;
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

const fetchRoadmapCreate = async (roadmap: Roadmap) => {
  const response = await axiosInstance.post("/api/v1/roadmaps", roadmap);
  return response.data;
};

const useRoadmapCreate = () => {
  return useMutation(fetchRoadmapCreate, {});
};

export { useRoadmapList, useRoadmapCreate };
