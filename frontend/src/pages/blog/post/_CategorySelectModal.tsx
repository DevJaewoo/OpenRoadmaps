import { Modal } from "@mantine/core";
import { FC, useRef, useState } from "react";
import { AiOutlinePlus } from "react-icons/ai";
import { CategoryListItem, useCategoryUpload } from "src/apis/useCategory";

interface Props {
  opened: boolean;
  onClose: () => void;
  onSelect: (category: CategoryListItem) => void;
  categoryList: CategoryListItem[];
}

const CategorySelectModal: FC<Props> = ({
  opened,
  onClose,
  onSelect,
  categoryList,
}) => {
  const inputRef = useRef<HTMLInputElement>(null);
  const [appending, setAppending] = useState(false);
  const cateogryUpload = useCategoryUpload();

  const handleKeyDown = (event: React.KeyboardEvent<HTMLInputElement>) => {
    if (event.key === "Enter") {
      inputRef.current?.blur();
    }
  };

  const handleCategoryAdd = () => {
    if (!inputRef.current || inputRef.current.value === "") return;
    const name = inputRef.current.value;
    cateogryUpload.mutate(
      { name },
      {
        onSuccess: ({ categoryId }) => {
          categoryList.push({ id: categoryId, name });
          setAppending(false);
        },
      }
    );
  };

  return (
    <Modal opened={opened} onClose={onClose}>
      <div className="flex flex-col items-center">
        {categoryList.map((category) => (
          <button
            key={category.id}
            type="button"
            className="flex justify-center items-center w-full mb-2 py-2 rounded-lg border-2 border-blue-600 hover:text-white hover:bg-blue-600 transition-colors"
            onClick={() => onSelect(category)}
          >
            {category.name}
          </button>
        ))}
        {!appending ? (
          <button
            type="button"
            className="flex justify-center items-center w-full py-2 rounded-lg border-2 border-dashed border-blue-600 hover:text-white hover:bg-blue-600 transition-colors"
            onClick={() => setAppending(true)}
          >
            <AiOutlinePlus />
          </button>
        ) : (
          <input
            ref={inputRef}
            className="w-full p-2 border border-gray-200 rounded-lg"
            placeholder="카테고리"
            onBlur={handleCategoryAdd}
            onKeyDown={handleKeyDown}
          />
        )}
      </div>
    </Modal>
  );
};

export default CategorySelectModal;
