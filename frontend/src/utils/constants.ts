export const RoadmapColor = {
  RECOMMEND: "blue-600",
  ALTERNATIVE: "yellow-400",
  NOT_RECOMMEND: "red-400",
  NONE: "gray-200",
} as const;

export type TRoadmapColor = typeof RoadmapColor[keyof typeof RoadmapColor];
