import { FC } from "react";
import { Navigate, useParams } from "react-router-dom";
import { useClient } from "src/apis/useClient";
import ProfileImage from "src/components/ProfileImage";

const Profile: FC<{}> = () => {
  const { clientId } = useParams();
  const { data: clientInfo, isLoading, isError } = useClient(Number(clientId));

  if (clientId === undefined) {
    return <Navigate replace to="/" />;
  }

  return (
    <div>
      {isLoading && (
        <div className="flex flex-row justify-center">
          <div className="flex flex-col items-center">
            <ProfileImage url={clientInfo} />
          </div>
          <div className="flex flex-col">
            <div>
              <h2>로드맵 목록</h2>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default Profile;
