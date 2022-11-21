import { FC, useState, useEffect } from "react";
import { Drawer, Image, Radio } from "@mantine/core";
import { Dropzone, MIME_TYPES } from "@mantine/dropzone";
import {
  Accessibility,
  TAccessibility,
  UploadRoadmap,
} from "src/apis/useRoadmap";
import {
  OutlinedButton,
  PrimaryButton,
} from "src/components/button/VariantButtons";

interface Props {
  opened: boolean;
  roadmap: UploadRoadmap | undefined;
  onClose: () => void;
  onFinish: () => void;
}

const RoadmapEditCompleteDrawer: FC<Props> = ({
  opened,
  roadmap,
  onClose,
  onFinish,
}) => {
  const [image, setImage] = useState<string | undefined>(undefined);
  const [accessibility, setAccessibility] = useState<TAccessibility>(
    roadmap?.accessibility ?? Accessibility.PUBLIC
  );

  useEffect(() => {
    if (roadmap) {
      setImage(roadmap.image);
      setAccessibility(roadmap.accessibility);
    }
  }, [roadmap]);

  const handleAccessibilityChange = (value: TAccessibility) => {
    if (roadmap) {
      roadmap.accessibility = value;
      setAccessibility(value);
    }
  };

  return (
    <Drawer opened={opened} onClose={onClose} position="bottom" size="lg">
      <div className="flex flex-row justify-center">
        <div className="flex flex-col max-w-7xl w-full">
          <h2 className="text-xl pb-2 mb-4 border-b">로드맵 발행</h2>
          <div className="flex flex-row w-full">
            <div className="flex-1 flex flex-col">
              <h2 className="mt-4 mb-4 text-2xl font-semibold">
                {roadmap?.title}
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
            </div>
            <div className="w-40 h-40">
              {image === undefined ? (
                <Dropzone
                  className="w-full h-full"
                  accept={[MIME_TYPES.png, MIME_TYPES.jpeg, MIME_TYPES.gif]}
                  onDrop={() => {}}
                >
                  <div>{}</div>
                </Dropzone>
              ) : (
                <Image src={image} alt="대표이미지" />
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

export default RoadmapEditCompleteDrawer;