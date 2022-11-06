import { useRecoilState } from "recoil";
import axios, { AxiosError } from "axios";
import { atomClientInfo } from "src/atoms/client";

const axiosInstance = axios.create({
  withCredentials: true,
});

axiosInstance.interceptors.response.use(
  (response) => response,
  (error: AxiosError | undefined) => {
    if (error?.response?.status === 401) {
      const [, setClientInfo] = useRecoilState(atomClientInfo);
      setClientInfo(undefined);
    }

    return Promise.reject(error);
  }
);

export default axiosInstance;
