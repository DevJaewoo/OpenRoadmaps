const pixelToRem = (px: number): number => {
  return px / parseFloat(getComputedStyle(document.documentElement).fontSize);
};

const getCurrentPositionPixel = (refs: HTMLElement | null | undefined) => {
  let x = 0;
  let y = 0;

  if (refs) {
    const style = window.getComputedStyle(refs);
    const matrix = new DOMMatrixReadOnly(style.transform);
    x = matrix.m41; // translateX
    y = matrix.m42; // translateY
  }

  x += refs?.offsetLeft || 0;
  y += refs?.offsetTop || 0;

  return { x, y };
};

const getCurrentPositionRem = (refs: HTMLElement | null | undefined) => {
  const { x, y } = getCurrentPositionPixel(refs);
  return { x: pixelToRem(x), y: pixelToRem(y) };
};

export { pixelToRem, getCurrentPositionPixel, getCurrentPositionRem };
