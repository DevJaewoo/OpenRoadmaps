export const EditMode = {
  Cursor: 0,
  Add: 1,
  Connect: 2,
  Delete: 3,
} as const;

export type TEditMode = typeof EditMode[keyof typeof EditMode];

export const Position = {
  top: "t",
  bottom: "b",
  left: "l",
  right: "r",
} as const;

export type TPosition = typeof Position[keyof typeof Position];
