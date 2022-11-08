import { FC } from "react";

interface Props {
  title: string;
  text: string;
}

const Header: FC<Props> = ({ title, text }) => {
  return (
    <div className="flex flex-col items-center">
      <h1 className="mt-10 text-4xl text-center">{title}</h1>
      <p className="mt-5 max-w-xl text-md text-center whitespace-pre-line">
        {text}
      </p>
    </div>
  );
};

export default Header;
