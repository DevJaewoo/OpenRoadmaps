import axios from "axios";
import { useMutation } from "react-query";
import { useRecoilState } from "recoil";
import { ClientInfo, atomClientInfo } from "../atoms/client";

axios.defaults.withCredentials = true;

interface RegisterRequest {
  email: string;
  password: string;
}

const fetchRegister = async (request: RegisterRequest): Promise<ClientInfo> => {
  const response = await axios.post("/api/v1/client/register", request);
  return response.data;
};

const useRegister = () => {
  const [, setClientInfo] = useRecoilState(atomClientInfo);
  return useMutation(fetchRegister, {
    onSuccess: (data) => {
      setClientInfo(data);
    },
  });
};

export default useRegister;
