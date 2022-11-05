import { useRecoilState } from "recoil";
import { useQuery } from "react-query";
import { atomClientInfo, ClientInfo } from "src/atoms/client";
import axiosInstance from "src/apis/axiosInstance";

const fetchClient = async (id: number): Promise<ClientInfo> => {
  const response = await axiosInstance.get(`/api/v1/client/${id}`);
  return response.data as ClientInfo;
};

const fetchCurrentClient = async (): Promise<ClientInfo> => {
  const response = await axiosInstance.get("/api/v1/client");
  return response.data as ClientInfo;
};

const useClient = (id: number) => {
  return useQuery(["client", id], () => fetchClient(id), {});
};

const useCurrentClient = () => {
  const [, setClientInfo] = useRecoilState(atomClientInfo);
  return useQuery(["currentClient"], fetchCurrentClient, {
    onSuccess: (data: ClientInfo) => {
      setClientInfo(data);
    },
  });
};

export { useClient, useCurrentClient };
