import { useRecoilState } from "recoil";
import { AxiosError } from "axios";
import { useQuery } from "react-query";
import { atomClientInfo, ClientInfo } from "src/atoms/client";
import axiosInstance from "src/apis/axiosInstance";
import { objectToParams } from "src/utils/utils";

const fetchClient = async (id: number): Promise<ClientInfo> => {
  const response = await axiosInstance.get(`/api/v1/client/${id}`);
  return response.data as ClientInfo;
};

const useClient = (id: number) => {
  return useQuery(["client", id], () => fetchClient(id), {});
};

const fetchCurrentClient = async (): Promise<ClientInfo> => {
  const response = await axiosInstance.get("/api/v1/client");
  return response.data as ClientInfo;
};

const useCurrentClient = (
  onSuccess?: (data: ClientInfo) => void,
  onError?: (error: AxiosError) => void
) => {
  const [clientInfo, setClientInfo] = useRecoilState(atomClientInfo);
  return useQuery(["currentClient"], fetchCurrentClient, {
    onSuccess: (data) => {
      setClientInfo(data);
      if (onSuccess) onSuccess(data);
    },
    onError: (error: AxiosError) => {
      if (clientInfo === undefined || error.response?.status === 401) {
        setClientInfo(undefined);
      }
      if (onError) onError(error as AxiosError);
    },
  });
};

export interface ClientSearch {
  name?: string;
}

const fetchClientSearch = async (search: ClientSearch): Promise<ClientInfo> => {
  const response = await axiosInstance.get(
    `/api/v1/client/search?${objectToParams(search)}`
  );
  return response.data;
};

const useClientSearch = (search: ClientSearch) => {
  return useQuery(
    ["clientSearch", search],
    () => fetchClientSearch(search),
    {}
  );
};

export { useClient, useCurrentClient, useClientSearch };
