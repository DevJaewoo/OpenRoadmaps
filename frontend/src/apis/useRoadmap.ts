import { atomClientInfo } from "src/atoms/client";
import { useRecoilState } from "recoil";
import { AxiosError } from "axios";
import { useMutation, useQuery } from "react-query";
import axiosInstance from "src/apis/axiosInstance";
import { TAccessibility } from "src/utils/constants";
import { objectToParams } from "src/utils/utils";
import { useRedirectNavigate } from "src/hooks/useRedirectNavigate";

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
  clientName: string;
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

export const Recommend = {
  RECOMMEND: "RECOMMEND",
  ALTERNATIVE: "ALTERNATIVE",
  NOT_RECOMMEND: "NOT_RECOMMEND",
  NONE: "NONE",
} as const;
export type TRecommend = typeof Recommend[keyof typeof Recommend];

export const RecommendText = {
  RECOMMEND: "Recommended",
  ALTERNATIVE: "Alternative",
  NOT_RECOMMEND: "Not Recommend",
  NONE: "None",
};

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
  content: string;
  image: string;
  accessibility: TAccessibility;
  likes: number;
  liked: boolean;
  createdDate: string;
  roadmapItemList: RoadmapItem[];
  clientId: number;
  clientName: string;
}

export interface RoadmapUploadRequest {
  title: string;
  image?: string;
  accessibility: TAccessibility;
  roadmapItemList: RoadmapItem[];
}

const fetchRoadmapList = async (query: RoadmapSearch): Promise<RoadmapList> => {
  const response = await axiosInstance.get(
    `/api/v1/roadmaps?${objectToParams(query)}`
  );
  return response.data;
};

const useRoadmapList = (
  query: RoadmapSearch,
  onSuccess?: (data: RoadmapList) => void
) => {
  const [clientInfo] = useRecoilState(atomClientInfo);
  return useQuery(
    ["roadmapList", clientInfo?.id, query],
    () => fetchRoadmapList(query),
    {
      onSuccess,
    }
  );
};

const fetchRoadmapCreate = async (
  roadmap: RoadmapUploadRequest
): Promise<{ roadmapId: number }> => {
  const response = await axiosInstance.post("/api/v1/roadmaps", roadmap);
  return response.data;
};

const useRoadmapCreate = () => {
  return useMutation(fetchRoadmapCreate, {});
};

const fetchRoadmap = async (roadmapId: number): Promise<Roadmap> => {
  const response = await axiosInstance.get(`/api/v1/roadmaps/${roadmapId}`, {});
  return response.data;
};

const useRoadmap = (roadmapId: number) => {
  const [clientInfo] = useRecoilState(atomClientInfo);
  return useQuery(
    ["roadmap", clientInfo?.id, roadmapId],
    () => fetchRoadmap(roadmapId),
    {}
  );
};

interface RoadmapLikeRequest {
  roadmapId: number;
  liked: boolean;
}

interface RoadmapLikeResponse {
  roadmapId: number;
  liked: boolean;
  likes: number;
}

const fetchRoadmapLike = async ({
  roadmapId,
  liked,
}: RoadmapLikeRequest): Promise<RoadmapLikeResponse> => {
  const response = await axiosInstance.put(
    `/api/v1/roadmaps/${roadmapId}/like`,
    { liked }
  );
  return response.data;
};

const useRoadmapLike = () => {
  const navigate = useRedirectNavigate();
  return useMutation(fetchRoadmapLike, {
    onError: (error: AxiosError) => {
      if (error.response?.status === 401) {
        navigate("/login");
      }
    },
  });
};

interface RoadmapItemClearRequest {
  roadmapId: number;
  roadmapItemId: number;
  isCleared: boolean;
}

interface RoadmapItemClearResponse {
  roadmapItemId: number;
  isCleared: boolean;
}

const fetchRoadmapClear = async ({
  roadmapId,
  roadmapItemId,
  isCleared,
}: RoadmapItemClearRequest): Promise<RoadmapItemClearResponse> => {
  const response = await axiosInstance.put(
    `/api/v1/roadmaps/${roadmapId}/items/${roadmapItemId}/clear`,
    { isCleared }
  );
  return response.data;
};

const useRoadmapItemClear = () => {
  const navigate = useRedirectNavigate();
  return useMutation(fetchRoadmapClear, {
    onError: (error: AxiosError) => {
      if (error.response?.status === 401) {
        navigate("/login");
      }
    },
  });
};

export {
  useRoadmapList,
  useRoadmapCreate,
  useRoadmap,
  useRoadmapLike,
  useRoadmapItemClear,
};
