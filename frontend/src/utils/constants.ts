export const RoadmapColor = {
  RECOMMEND: "rgb(37 99 235)",
  ALTERNATIVE: "rgb(250 204 21)",
  NOT_RECOMMEND: "rgb(248 113 113)",
  NONE: "rgb(229 231 235)",
} as const;

export type TRoadmapColor = typeof RoadmapColor[keyof typeof RoadmapColor];

export const Accessibility = {
  PRIVATE: "PRIVATE",
  PROTECTED: "PROTECTED",
  PUBLIC: "PUBLIC",
} as const;

export type TAccessibility = typeof Accessibility[keyof typeof Accessibility];
