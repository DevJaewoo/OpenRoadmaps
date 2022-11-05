import React from "react";
import { Link, useLocation } from "react-router-dom";
import logo from "src/logo.svg";
import {
  OutlinedButton,
  PrimaryButton,
} from "src/components/button/VariantButtons";
import { useRecoilState } from "recoil";
import { atomClientInfo } from "src/atoms/client";
import ProfileImage from "./ProfileImage";

interface NavItemProps {
  text: string;
  path: string;
}

const NavItem: React.FC<NavItemProps> = ({ text, path }) => {
  const location = useLocation();

  return (
    <div
      className={`flex flex-col justify-center h-full px-5 ${
        location.pathname === path ? "font-semibold" : null
      }`}
    >
      <Link to={path}>{text}</Link>
    </div>
  );
};

const Navigation = () => {
  const [clientInfo] = useRecoilState(atomClientInfo);

  const navItems: NavItemProps[] = [
    {
      text: "Roadmaps",
      path: "/roadmaps",
    },
    {
      text: "Projects",
      path: "/projects",
    },
    {
      text: "Blog",
      path: "/blog",
    },
    {
      text: "Community",
      path: "/community",
    },
  ];

  return (
    <header className="top-0 w-full h-16 min-w-[640px] bg-white shadow-md fixed z-50">
      <nav className="max-w-screen-xl h-full mx-auto">
        <div className="flex flex-row justify-between items-center w-full h-full px-4">
          <div className="flex flex-row items-center h-full">
            <div className="flex flex-col justify-center h-full">
              <Link to="/">
                <img src={logo} alt="logo" className="w-16 h-auto" />
              </Link>
            </div>
            {navItems.map((navItem) => (
              <NavItem {...navItem} key={navItem.text} />
            ))}
          </div>
          {clientInfo === undefined ? (
            <div className="flex flex-row items-center h-full">
              <PrimaryButton
                type="link"
                to="/register"
                className="mr-2 w-25 h-12"
                text="회원가입"
              />
              <OutlinedButton
                type="link"
                to="/login"
                className="w-25 h-12"
                text="로그인"
              />
            </div>
          ) : (
            <ProfileImage clientId={clientInfo.id} url={clientInfo.picture} />
          )}
        </div>
      </nav>
    </header>
  );
};

export default Navigation;
