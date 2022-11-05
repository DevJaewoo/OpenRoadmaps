import { FC } from "react";
import { Link } from "react-router-dom";

interface Props {
  clientId?: number;
  url: string | null | undefined;
  className?: string;
  to?: string;
}

const ProfileImage: FC<Props> = ({ clientId, url, className, to }) => {
  return (
    <div className={`w-10 h-10 ${className}`}>
      <Link to={to || `/clients/${clientId}`}>
        <img
          src={url || undefined}
          alt="Profile"
          className="w-full h-full rounded-full bg-gray-200"
        />
      </Link>
    </div>
  );
};

export default ProfileImage;
