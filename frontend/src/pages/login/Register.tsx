import {
  FieldError,
  FieldValues,
  SubmitHandler,
  useForm,
} from "react-hook-form";
import { FcGoogle } from "react-icons/fc";
import { FaGithub } from "react-icons/fa";
import { useRef } from "react";
import axios, { AxiosError } from "axios";
import useRegister from "src/apis/useRegister";
import { PrimaryButton } from "src/components/button/VariantButtons";
import withAuth from "src/hoc/withAuth";
import ErrorMessage from "./ErrorMessage";
import PatternError from "./PatternError";

axios.defaults.withCredentials = true;

const Register: React.FC<{}> = () => {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors },
  } = useForm();
  const password = useRef({});
  password.current = watch("password", "");

  const { isLoading, isError, error, mutate } = useRegister();

  const onSubmit: SubmitHandler<FieldValues> = (data) => {
    if (isLoading) return;
    mutate({
      email: data.email,
      name: data.name,
      password: data.password,
    });
  };

  return (
    <div className="flex flex-col items-center flex-1 w-full h-full my-10">
      <h1 className="w-full text-center text-3xl font-semibold">회원가입</h1>
      <form
        onSubmit={handleSubmit(onSubmit)}
        className="flex flex-col w-full max-w-2xl mt-5 p-10 border-gray-100 border rounded-xl shadow-md"
      >
        {isError && <ErrorMessage error={error as AxiosError} />}

        <div className="w-full mb-5">
          <h3 className="text-lg">이메일</h3>
          <input
            type="email"
            placeholder="example@email.com"
            className="w-full mt-2 p-3 border border-gray-300 rounded-md"
            {...register("email", {
              required: "이메일을 입력해주세요.",
              pattern: {
                value: /\S+@\S+/,
                message: "이메일 형식이 올바르지 않습니다.",
              },
            })}
          />
          <PatternError field={errors.email as FieldError} />
        </div>
        <div className="w-full mb-5">
          <h3 className="text-lg">닉네임</h3>
          <input
            type="text"
            placeholder="Enter nickname"
            className="w-full mt-2 p-3 border border-gray-300 rounded-md"
            {...register("name", {
              required: "이름을 입력해주세요.",
              pattern: {
                value: /^[0-9a-z가-힣]{1,10}$/,
                message:
                  "숫자, 영문 소문자, 한글로 이루어진 10자리 이내의 이름이어야 합니다.",
              },
            })}
          />
          <PatternError field={errors.name as FieldError} />
        </div>
        <div className="w-full mb-5">
          <h3 className="text-lg">비밀번호</h3>
          <input
            type="password"
            placeholder="Enter password"
            className="w-full mt-2 p-3 border border-gray-300 rounded-md"
            {...register("password", {
              required: "비밀번호를 입력해주세요.",
              pattern: {
                value:
                  /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,14}$/,
                message:
                  "하나 이상의 대문자, 소문자, 숫자, 특수문자를 포함한 8~14 자리의 비밀번호이어야 합니다.",
              },
            })}
          />
          <PatternError field={errors.password as FieldError} />
        </div>
        <div className="w-full mb-5">
          <h3 className="text-lg">비밀번호 확인</h3>
          <input
            type="password"
            placeholder="Re-enter password"
            className="w-full mt-2 p-3 border border-gray-300 rounded-md"
            {...register("repassword", {
              validate: (value) =>
                value === password.current || "비밀번호가 일치하지 않습니다.",
            })}
          />
          <PatternError field={errors.repassword as FieldError} />
        </div>

        <PrimaryButton
          type="button"
          text="회원가입"
          to="/api/oauth2/authorization/google"
          className="w-full mt-4 py-3 justify-center text-xl"
          props={{ disabled: isLoading }}
        />
      </form>

      <div className="flex flex-col w-full max-w-2xl mt-10 p-10 border-gray-100 border rounded-xl shadow-md">
        <a
          href="/api/oauth/authorization/google"
          className="flex justify-center items-center w-full py-3 text-xl font-semibold rounded-lg bg-gray-50 hover:bg-gray-200 border border-gray-200 transition-colors"
        >
          <FcGoogle className="inline mr-3 text-2xl" />
          Google 회원가입
        </a>
        <a
          href="/api/oauth/authorization/github"
          className="flex justify-center items-center w-full mt-3 py-3 text-xl font-semibold text-center text-white rounded-lg bg-gray-700 hover:bg-gray-500 border border-gray-200 transition-colors"
        >
          <FaGithub className="inline mr-3 text-2xl" />
          Github 회원가입
        </a>
      </div>
    </div>
  );
};

export default withAuth(Register, false);
