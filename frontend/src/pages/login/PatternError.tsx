import { FieldError } from "react-hook-form";

const PatternError = ({ field }: { field: FieldError | undefined }) => {
  return field?.message ? (
    <small role="alert" className="text-red-500">
      {field?.message.toString()}
    </small>
  ) : null;
};

export default PatternError;
