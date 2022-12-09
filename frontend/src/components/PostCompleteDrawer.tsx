import { FC, useState, useEffect } from "react";
import { Drawer, Radio } from "@mantine/core";
import { Dropzone, FileWithPath, MIME_TYPES } from "@mantine/dropzone";
import {
  OutlinedButton,
  PrimaryButton,
} from "src/components/button/VariantButtons";
import { useImageUpload } from "src/apis/useImage";
import { AiFillCloseSquare } from "react-icons/ai";
import StableImage from "src/components/StableImage";
import { Accessibility, TAccessibility } from "src/utils/constants";
import { PostUploadRequest } from "src/apis/usePost";
import { RoadmapUploadRequest } from "src/apis/useRoadmap";
import { useCategoryList } from "src/apis/useCategory";
import { useRecoilState } from "recoil";
import { atomClientInfo } from "src/atoms/client";

interface Props {
  type: string;
  opened: boolean;
  uploadData: RoadmapUploadRequest | PostUploadRequest | undefined;
  onClose: () => void;
  onFinish: () => void;
}

const CompleteDrawer: FC<Props> = ({
  type,
  opened,
  uploadData,
  onClose,
  onFinish,
}) => {
  const [image, setImage] = useState<string | undefined>(undefined);
  const [accessibility, setAccessibility] = useState<TAccessibility>(
    uploadData?.accessibility ?? Accessibility.PUBLIC
  );
  const imageUpload = useImageUpload();

  const [clientInfo] = useRecoilState(atomClientInfo);
  const { data } = useCategoryList(clientInfo?.name ?? "");

  useEffect(() => {
    if (uploadData) {
      setImage(uploadData.image);
      setAccessibility(uploadData.accessibility);
    }
  }, [uploadData]);

  const handleAccessibilityChange = (value: TAccessibility) => {
    if (uploadData) {
      uploadData.accessibility = value;
      setAccessibility(value);
    }
  };

  const handleImageDrop = (files: FileWithPath[]) => {
    imageUpload.mutate(files[0], {
      onSuccess: ({ url }) => {
        if (uploadData) {
          uploadData.image = url;
          setImage(`/api/v1/images/${url}`);
        }
      },
    });
  };

  return (
    <Drawer opened={opened} onClose={onClose} position="bottom" size="lg">
      <div className="flex flex-row justify-center">
        <div className="flex flex-col max-w-7xl w-full">
          <h2 className="text-xl pb-2 mb-4 border-b">{type} 발행</h2>
          <div className="flex flex-row w-full">
            <div className="flex-1 flex flex-col">
              <h2 className="mt-4 mb-4 text-2xl font-semibold">
                {uploadData?.title}
              </h2>
              <div className="flex flex-row items-center">
                <p className="w-20 mr-4 text-lg">공개 여부</p>
                <Radio.Group
                  value={accessibility}
                  onChange={handleAccessibilityChange}
                >
                  <Radio value={Accessibility.PUBLIC} label="공개" />
                  <Radio value={Accessibility.PRIVATE} label="비공개" />
                </Radio.Group>
              </div>
              <div className="flex flex-row items-center">
                <p className="w-20 mr-4 text-lg">카테고리</p>
                <p>{JSON.stringify(data)}</p>
              </div>
            </div>
            <div className="w-40 h-40">
              {image === undefined ? (
                <Dropzone
                  className="w-full h-full"
                  loading={imageUpload.isLoading}
                  accept={[MIME_TYPES.png, MIME_TYPES.jpeg, MIME_TYPES.gif]}
                  onDrop={handleImageDrop}
                >
                  <div>{}</div>
                </Dropzone>
              ) : (
                <div className="w-full h-full relative">
                  <StableImage className="w-full h-full absolute" src={image} />
                  <button
                    type="button"
                    className="absolute top-0 right-0 text-xl bg-black text-white object-fill"
                    onClick={() => setImage(undefined)}
                  >
                    <AiFillCloseSquare />
                  </button>
                </div>
              )}
            </div>
          </div>
          <div className="flex flex-row justify-center items-center h-30 mt-4 pt-4 pb-6 border-t">
            <PrimaryButton
              className="flex justify-center w-24 h-full mr-4"
              type="button"
              text="발행"
              onClick={onFinish}
            />
            <OutlinedButton
              className="flex justify-center w-24 h-full"
              type="button"
              text="취소"
              onClick={onClose}
            />
          </div>
        </div>
      </div>
    </Drawer>
  );
};

export default CompleteDrawer;
