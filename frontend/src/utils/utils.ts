const objectToParams = (o: Object): URLSearchParams => {
  const searchParams = new URLSearchParams();

  Object.entries(o).forEach(([k, v]) => {
    if (v !== undefined) searchParams.append(k, v.toString());
  });

  return searchParams;
};

export { objectToParams };
