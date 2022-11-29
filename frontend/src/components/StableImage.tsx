import { FC, useState, ImgHTMLAttributes, ReactEventHandler } from "react";

interface Props extends ImgHTMLAttributes<HTMLImageElement> {}

const StableImage: FC<Props> = ({ src, ...rest }) => {
  const [url, setUrl] = useState<string>(src || "");
  const onError: ReactEventHandler<HTMLImageElement> = (event) => {
    setUrl(
      `https://dummyimage.com/${event.currentTarget.width}x${event.currentTarget.height}/e0e7ff/333`
    );
  };

  return (
    <img
      className="object-cover"
      alt={rest.alt}
      onError={onError}
      src={url}
      {...rest}
    />
  );
};

export default StableImage;
