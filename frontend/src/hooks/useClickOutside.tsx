import { useEffect, useRef, useState } from "react";

const useClickOutside = <T extends HTMLElement>(initialState: boolean) => {
  const ref = useRef<T>(null);
  const [isVisible, setVisible] = useState(initialState);

  const handleClickOutside = (event: MouseEvent) => {
    if (!(event.target instanceof Element)) return;
    if (ref.current && !ref.current.contains(event.target)) {
      setVisible(false);
    }
  };

  useEffect(() => {
    document.addEventListener("click", handleClickOutside, true);
    return () =>
      document.removeEventListener("click", handleClickOutside, true);
  }, [ref]);

  return { ref, isVisible, setVisible };
};

export default useClickOutside;
