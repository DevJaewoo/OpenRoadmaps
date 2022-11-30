import axiosInstance from "src/apis/axiosInstance";
import { useRecoilState } from "recoil";
import { useMutation } from "react-query";
import { ClientInfo, atomClientInfo } from "src/atoms/client";
import { useNavigate, useSearchParams } from "react-router-dom";

interface LoginRequest {
  email: string;
  password: string;
}

const fetchLogin = async (request: LoginRequest): Promise<ClientInfo> => {
  const response = await axiosInstance.post("/api/v1/client/login", request);
  return response.data;
};

const useLogin = () => {
  const [, setClientInfo] = useRecoilState(atomClientInfo);
  const [params] = useSearchParams();
  const navigate = useNavigate();

  return useMutation(fetchLogin, {
    onSuccess: (data) => {
      setClientInfo(data);

      const redirectURL = params.get("redirect");
      navigate(`${redirectURL || "/"}`);
    },
  });
};

export default useLogin;
