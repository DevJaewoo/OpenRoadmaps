import { BiErrorCircle } from "react-icons/bi";
import { FaArrowRight } from "react-icons/fa";
import { OutlinedButton } from "src/components/button/VariantButtons";

const NotFound: React.FC<{}> = () => {
  return (
    <div className="flex flex-col justify-center items-center h-screen -mt-16">
      <h1 className="flex items-center text-7xl font-bold">
        <BiErrorCircle className="inline" />
        &nbsp;페이지를 찾을 수 없습니다.
      </h1>
      <OutlinedButton
        type="link"
        to="/"
        text={
          <>
            홈페이지로 이동하기
            <FaArrowRight className="ml-2" />
          </>
        }
        className="mt-16 text-2xl"
      />
    </div>
  );
};

export default NotFound;
