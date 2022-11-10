const pixelToRem = (px: number): number => {
  return px / parseFloat(getComputedStyle(document.documentElement).fontSize);
};

export { pixelToRem };
