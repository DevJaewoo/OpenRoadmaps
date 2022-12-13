import { BiErrorCircle } from "react-icons/bi";
import { FaArrowRight } from "react-icons/fa";
import { useNavigate } from "react-router-dom";
import { OutlinedButton } from "src/components/button/VariantButtons";

interface Props {
  error?: string;
  action?: string;
  navigate?: string | number;
}

const NotFound: React.FC<Props> = ({
  error = "페이지를 찾을 수 없습니다.",
  action = "홈페이지로 이동하기",
  navigate = "/",
}) => {
  const navigator = useNavigate();
  return (
    <div className="flex flex-col justify-center items-center h-screen -mt-16">
      <h1 className="flex items-center text-7xl font-bold">
        <BiErrorCircle className="inline" />
        &nbsp;{error}
      </h1>
      <OutlinedButton
        type="button"
        onClick={() => {
          if (typeof navigate === "number") navigator(navigate);
          else if (typeof navigate === "string") navigator(navigate);
        }}
        text={
          <>
            {action}
            <FaArrowRight className="ml-2" />
          </>
        }
        className="mt-16 text-2xl"
      />
    </div>
  );
};

export default NotFound;
