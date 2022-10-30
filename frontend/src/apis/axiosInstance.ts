import { useRecoilState } from "recoil";
import axios, { AxiosError } from "axios";
import { atomClientInfo } from "../atoms/client";

const axiosInstance = axios.create({
  withCredentials: true,
});

axiosInstance.interceptors.response.use(
  (response) => response,
  (error: AxiosError | undefined) => {
    const [, setClientInfo] = useRecoilState(atomClientInfo);
    if (error?.response?.status === 401) {
      setClientInfo(undefined);
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;
