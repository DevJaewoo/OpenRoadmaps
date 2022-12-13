import axios from "axios";
import { useMutation } from "react-query";
import { useNavigate, useSearchParams } from "react-router-dom";
import { useRecoilState } from "recoil";
import { ClientInfo, atomClientInfo } from "src/atoms/client";

axios.defaults.withCredentials = true;

interface RegisterRequest {
  email: string;
  name: string;
  password: string;
}

const fetchRegister = async (request: RegisterRequest): Promise<ClientInfo> => {
  const response = await axios.post("/api/v1/client/register", request);
  return response.data;
};

const useRegister = () => {
  const [, setClientInfo] = useRecoilState(atomClientInfo);
  const [params] = useSearchParams();
  const navigate = useNavigate();

  return useMutation(fetchRegister, {
    onSuccess: (data) => {
      setClientInfo(data);
      const redirectURL = params.get("redirect");
      navigate(`${redirectURL || "/"}`);
    },
  });
};

export default useRegister;
