import { AxiosError } from "axios";
import { FC, Suspense } from "react";
import { ErrorBoundary } from "react-error-boundary";
import { useRecoilState } from "recoil";
import { useNavigate } from "react-router-dom";
import { useCurrentClient } from "src/apis/useClient";
import { atomClientInfo, ClientInfo } from "src/atoms/client";

const withAuth = (
  SpecificComponent: FC,
  option: boolean | null = null,
  adminRoute: boolean = false
) => {
  const AuthenticationCheck: FC = () => {
    const [clientInfo, setClientInfo] = useRecoilState(atomClientInfo);
    const navigate = useNavigate();

    const onSuccess = (data: ClientInfo) => {
      setClientInfo(data);
      if (option === false) {
        navigate("/");
      }
      if (adminRoute) {
        navigate("/");
      }
    };

    const onError = (error: AxiosError) => {
      if (clientInfo === undefined || error.response?.status === 401) {
        setClientInfo(undefined);
        if (option === true) {
          navigate("/login");
        }
      }
    };

    useCurrentClient(onSuccess, onError);

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
