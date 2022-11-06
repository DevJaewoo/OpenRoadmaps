import axios from "axios";
import { useRecoilState } from "recoil";
import { useMutation } from "react-query";
import { ClientInfo, atomClientInfo } from "src/atoms/client";

axios.defaults.withCredentials = true;

interface LoginRequest {
  email: string;
  password: string;
}

const fetchLogin = async (request: LoginRequest): Promise<ClientInfo> => {
  const response = await axios.post("/api/v1/client/login", request);
  return response.data;
};

const useLogin = () => {
  const [, setClientInfo] = useRecoilState(atomClientInfo);
  return useMutation(fetchLogin, {
    onSuccess: (data) => {
      setClientInfo(data);
    },
  });
};

export default useLogin;
