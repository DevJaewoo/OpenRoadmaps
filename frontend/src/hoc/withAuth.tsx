import { AxiosError } from "axios";
import { FC, Suspense, useEffect } from "react";
import { ErrorBoundary } from "react-error-boundary";
import { useRecoilState } from "recoil";
import { useLocation, useNavigate } from "react-router-dom";
import { useCurrentClient } from "src/apis/useClient";
import { atomClientInfo } from "src/atoms/client";
import { useQueryClient } from "react-query";

const withAuth = (
  SpecificComponent: FC,
  option: boolean | null = null,
  adminRoute: boolean = false
) => {
  const AuthenticationCheck: FC = () => {
    const [clientInfo] = useRecoilState(atomClientInfo);
    const url = useLocation();
    const navigate = useNavigate();
    const queryClient = useQueryClient();

    const onSuccess = () => {
      if (option === false) {
        navigate("/");
      }
      if (adminRoute) {
        navigate("/");
      }
    };

    const onError = (error: AxiosError) => {
      if (clientInfo === undefined || error.response?.status === 401) {
        if (option === true) {
          navigate(`/login?redirect=${url.pathname}`);
        }
      }
    };

    useCurrentClient(onSuccess, onError);

    useEffect(() => {
      queryClient.invalidateQueries("currentClient");
    }, [url, queryClient]);

    return (
      <Suspense fallback={<div>Loading...</div>}>
        <ErrorBoundary fallback={<div>Error!</div>}>
          <SpecificComponent />
        </ErrorBoundary>
      </Suspense>
    );
  };

  return AuthenticationCheck;
};

export default withAuth;
