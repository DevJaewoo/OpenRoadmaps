import { AxiosError } from "axios";
import { ErrorResponse } from "src/apis/errorResponse";

const ErrorMessage = ({ error }: { error: AxiosError }) => {
  let message: string | undefined = (error.response?.data as ErrorResponse)
    .message;
  if (message === undefined) message = error.response?.statusText;

  return (
    <div className="flex items-center w-full mb-5 p-3 border border-red-600 bg-red-100 rounded-md text-red-500">
      {message}
    </div>
  );
};

export default ErrorMessage;
