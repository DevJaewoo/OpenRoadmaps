import React from "react";
import { Link, useLocation } from "react-router-dom";
import logo from "../logo.svg";
import { OutlinedButton, PrimaryButton } from "./button/VariantButtons";

interface NavItemProps {
  text: string;
  path: string;
  location?: any;
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
    <header className="top-0 w-full h-16 min-w-[640px] bg-white shadow-md fixed">
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
          <div className="flex flex-row items-center h-full">
            <PrimaryButton to="/register" class="w-25 h-12" text="회원가입" />
            <OutlinedButton to="/login" class="w-25 h-12" text="로그인" />
          </div>
        </div>
      </nav>
    </header>
  );
};

export default Navigation;
