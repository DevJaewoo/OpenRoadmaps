import React from "react";
import { Link, useLocation } from "react-router-dom";
import logo from "../logo.svg";

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
    <header className="w-full h-16 min-w-[640px] bg-white shadow-md fixed">
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
            <Link
              to="/register"
              className="flex items-center w-25 h-12 mx-2 px-4 py-2 bg-indigo-500 outline-none rounded text-white shadow-indigo-200 shadow-lg font-medium active:shadow-none active:scale-95 hover:bg-indigo-600 focus:bg-indigo-600 focus:ring-2 focus:ring-indigo-600 focus:ring-offset-2 disabled:bg-gray-400/80 disabled:shadow-none disabled:cursor-not-allowed transition-colors duration-200"
            >
              회원가입
            </Link>
            <Link
              to="/login"
              className="flex items-center w-25 h-12 px-4 py-2 bg-transparent outline-none border-2 border-indigo-400 rounded text-indigo-500 font-medium active:scale-95 hover:bg-indigo-600 hover:text-white hover:border-transparent focus:bg-indigo-600 focus:text-white focus:border-transparent focus:ring-2 focus:ring-indigo-600 focus:ring-offset-2 disabled:bg-gray-400/80 disabled:shadow-none disabled:cursor-not-allowed transition-colors duration-200"
            >
              로그인
            </Link>
          </div>
        </div>
      </nav>
    </header>
  );
};

export default Navigation;
