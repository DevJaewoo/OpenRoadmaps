import { FieldValues, SubmitHandler, useForm } from "react-hook-form";
import { PrimaryButton } from "../../components/button/VariantButtons";
import { FcGoogle } from "react-icons/fc";
import { FaGithub } from "react-icons/fa";

const Register: React.FC<{}> = () => {
  const { register, handleSubmit } = useForm();

  const onSubmit: SubmitHandler<FieldValues> = (data) => {
    console.log(data);
  };

  return (
    <div className="flex flex-col items-center flex-1 w-full h-full">
      <h1 className="w-full mt-10 text-center text-3xl font-semibold">
        회원가입
      </h1>
      <form
        onSubmit={handleSubmit(onSubmit)}
        className="flex flex-col w-full max-w-2xl mt-5 p-10 border-gray-100 border rounded-xl shadow-md"
      >
        <div className="w-full mb-5">
          <h3 className="text-lg">이메일</h3>
          <input
            type="email"
            placeholder="example@email.com"
            className="w-full mt-2 p-3 border border-gray-300 rounded-md"
            {...register("email")}
          />
        </div>
        <div className="w-full mb-5">
          <h3 className="text-lg">비밀번호</h3>
          <input
            type="password"
            placeholder="Enter password"
            className="w-full mt-2 p-3 border border-gray-300 rounded-md"
            {...register("password")}
          />
        </div>
        <div className="w-full mb-5">
          <h3 className="text-lg">비밀번호 확인</h3>
          <input
            type="password"
            placeholder="Re-enter password"
            className="w-full mt-2 p-3 border border-gray-300 rounded-md"
            {...register("repassword")}
          />
        </div>

        <PrimaryButton
          type="button"
          text="회원가입"
          to="http://localhost:8080/oauth2/authorization/google"
          class="w-full mt-4 py-3 justify-center text-xl"
          onClick={() => {
            console.log("Click!");
          }}
        />
      </form>

      <div className="flex flex-col w-full max-w-2xl mt-10 p-10 border-gray-100 border rounded-xl shadow-md">
        <a
          href="http://localhost:8080/oauth/authorization/google"
          className="flex justify-center items-center w-full py-3 text-xl font-semibold rounded-lg bg-gray-50 hover:bg-gray-200 border border-gray-200 transition-colors"
        >
          <FcGoogle className="inline mr-3 text-2xl" />
          Google 회원가입
        </a>
        <a
          href="http://localhost:8080/oauth/authorization/github"
          className="flex justify-center items-center w-full mt-3 py-3 text-xl font-semibold text-center text-white rounded-lg bg-gray-700 hover:bg-gray-500 border border-gray-200 transition-colors"
        >
          <FaGithub className="inline mr-3 text-2xl" />
          Github 회원가입
        </a>
      </div>
    </div>
  );
};

export default Register;
