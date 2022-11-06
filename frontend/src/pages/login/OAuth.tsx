import { useCurrentClient } from "src/apis/useClient";

const Client: React.FC<{}> = () => {
  const { data } = useCurrentClient();
  return (
    <div className="text-3xl font-semibold">{`Hello, ${data?.name}!`}</div>
  );
};

const OAuth: React.FC<{}> = () => {
  return (
    <div className="flex flex-1 justify-center items-center">
      <Client />
    </div>
  );
};

export default OAuth;
