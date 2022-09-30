import { Link, useLocation } from "react-router-dom";
import { FaGithub } from "react-icons/fa";
import { useEffect, useState } from "react";

const EnabledPaths: string[] = [
  "/roadmaps",
  "/projects",
  "/blog",
  "/community",
];

const Footer: React.FC<{}> = () => {
  const location = useLocation();
  const [disabled, setDisabled] = useState<boolean>(false);

  useEffect(() => {
    const result = EnabledPaths.find((path) =>
      location.pathname.startsWith(path)
    );
    console.log(result, location.pathname);
    setDisabled(location.pathname !== "/" && result === undefined);
  }, [location.pathname]);

  return (
    <>
      {!disabled ? (
        <footer className="flex justify-center w-full mt-10 mb-20 text-gray-500">
          <div className="flex flex-col items-center w-full max-w-screen-xl">
            <div className="flex w-full">
              <Link to="/credits">Credits</Link>
            </div>
            <div className="flex justify-between w-full mt-4 pt-4 border-t-2 border-gray-200">
              <h3>&copy;DevJaewoo. All rights reserved.</h3>
              <div className="flex justify-end items-center">
                <a
                  href="https://github.com/DevJaewoo"
                  target="_blank"
                  rel="noreferrer"
                  className="text-2xl"
                >
                  <FaGithub />
                </a>
              </div>
            </div>
          </div>
        </footer>
      ) : null}
    </>
  );
};

export default Footer;
