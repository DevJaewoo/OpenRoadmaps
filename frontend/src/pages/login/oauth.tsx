import { Suspense } from "react";
import { useCurrentClient } from "../../apis/useClient";

const Client: React.FC<{}> = () => {
  const { data } = useCurrentClient();
  return <>{`Hello, ${data?.name}!`}</>;
};

const OAuth: React.FC<{}> = () => {
  return (
    <div className="flex flex-1 justify-center items-center text-3xl font-semibold">
      <Suspense fallback={<>Loading...</>}>
        <Client />
      </Suspense>
    </div>
  );
};

export default OAuth;
