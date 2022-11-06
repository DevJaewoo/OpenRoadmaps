import { atom } from "recoil";

export interface ClientInfo {
  id: number;
  name: string;
  email: string;
  picture: string | null;
  reputation: number;
}

export const atomClientInfo = atom<ClientInfo | undefined>({
  key: "clientInfo",
  default: undefined,
});
