import { RefObject } from "react";

const pixelToRem = (px: number): number => {
  return px / parseFloat(getComputedStyle(document.documentElement).fontSize);
};

const getCurrentPositionPixel = (refs: RefObject<HTMLElement>) => {
  let x = 0;
  let y = 0;

  if (refs.current) {
    const style = window.getComputedStyle(refs.current);
    const matrix = new DOMMatrixReadOnly(style.transform);
    x = matrix.m41; // translateX
    y = matrix.m42; // translateY
  }

  x += refs.current?.offsetLeft || 0;
  y += refs.current?.offsetTop || 0;

  return { x, y };
};

const getCurrentPositionRem = (refs: RefObject<HTMLElement>) => {
  const { x, y } = getCurrentPositionPixel(refs);
  return { x: pixelToRem(x), y: pixelToRem(y) };
};

export { pixelToRem, getCurrentPositionPixel, getCurrentPositionRem };
