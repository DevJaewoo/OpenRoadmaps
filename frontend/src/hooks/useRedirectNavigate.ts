import { useLocation, useNavigate } from "react-router-dom";

const useRedirectNavigate = () => {
  const params = useLocation();
  const defaultNavigate = useNavigate();

  const navigate = (url: string) => {
    defaultNavigate(`${url}?redirect=${params.pathname}`);
  };

  return navigate;
};

export { useRedirectNavigate };
