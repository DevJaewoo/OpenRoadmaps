import { Link } from "react-router-dom";

interface ButtonProps {
  type?: string;
  to?: string;
  text?: string | JSX.Element;
  class?: string;
  onClick?: React.MouseEventHandler<this>;
  props?: object;
}

const BaseButton: React.FC<ButtonProps> = (props) => {
  if (props.type === "button") {
    return (
      <button onClick={props.onClick} className={props.class} {...props.props}>
        {props.text}
      </button>
    );
  } else if (props.type === "anchor") {
    return (
      <a
        href={props.to || "."}
        onClick={props.onClick}
        className={props.class}
        {...props.props}
      >
        {props.text}
      </a>
    );
  } else {
    return (
      <Link
        to={props.to || "."}
        onClick={props.onClick}
        className={props.class}
        {...props.props}
      >
        {props.text}
      </Link>
    );
  }
};

export const PrimaryButton: React.FC<ButtonProps> = (props) => {
  const className = `flex items-center px-4 py-2 bg-indigo-500 outline-none rounded text-white shadow-indigo-200 shadow-lg font-medium active:shadow-none hover:bg-indigo-600 focus:bg-indigo-600 focus:ring-2 focus:ring-indigo-600 focus:ring-offset-2 disabled:bg-gray-400/80 disabled:shadow-none disabled:cursor-not-allowed transition-colors duration-200 ${props.class}`;
  return <BaseButton {...props} class={className} />;
};

export const SecondaryButton: React.FC<ButtonProps> = (props) => {
  const className = `flex items-center px-4 py-2 bg-indigo-50 outline-none border border-indigo-100 rounded text-indigo-500 font-medium hover:bg-indigo-400 hover:text-white focus:ring-2 focus:ring-indigo-600 focus:ring-offset-2 disabled:bg-gray-400/80 disabled:shadow-none disabled:cursor-not-allowed transition-colors duration-200 ${props.class}`;
  return <BaseButton {...props} class={className} />;
};

export const OutlinedButton: React.FC<ButtonProps> = (props) => {
  const className = `flex items-center px-4 py-2 bg-transparent outline-none border-2 border-indigo-400 rounded text-indigo-500 font-medium hover:bg-indigo-600 hover:text-white hover:border-transparent focus:bg-indigo-600 focus:text-white focus:border-transparent focus:ring-2 focus:ring-indigo-600 focus:ring-offset-2 disabled:bg-gray-400/80 disabled:shadow-none disabled:cursor-not-allowed transition-colors duration-200 ${props.class}`;
  return <BaseButton {...props} class={className} />;
};
