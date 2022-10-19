import { atomClientInfo } from "./../atoms/client";
import { useRecoilState } from "recoil";
import axios, { AxiosError } from "axios";

const axiosInstance = axios.create({
  withCredentials: true,
});

axiosInstance.interceptors.response.use(
  (response) => response,
  (error: AxiosError | undefined) => {
    const [, setClientInfo] = useRecoilState(atomClientInfo);
    if (error?.response?.status === 401) {
      setClientInfo(undefined);
    } else {
      return Promise.reject(error);
    }
  }
);

export default axiosInstance;
