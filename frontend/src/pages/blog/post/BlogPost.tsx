import { FC, useRef, useState } from "react";
import { Button } from "@mantine/core";
import { RichTextEditor, Link } from "@mantine/tiptap";
import { useEditor } from "@tiptap/react";
import Highlight from "@tiptap/extension-highlight";
import StarterKit from "@tiptap/starter-kit";
import Underline from "@tiptap/extension-underline";
import TextAlign from "@tiptap/extension-text-align";
import Superscript from "@tiptap/extension-superscript";
import SubScript from "@tiptap/extension-subscript";
import Image from "@tiptap/extension-image";
import Color from "@tiptap/extension-color";
import TextStyle from "@tiptap/extension-text-style";
import { IconColorPicker } from "@tabler/icons";
import withAuth from "src/hoc/withAuth";
import Header from "src/components/Header";
import { PostUploadRequest, usePostUpload } from "src/apis/usePost";
import { Accessibility } from "src/utils/constants";
import { useNavigate } from "react-router-dom";
import { useRecoilState } from "recoil";
import { atomClientInfo } from "src/atoms/client";
import BlogPostCompleteDrawer from "./_BlogPostCompleteDrawer";

interface Props {
  postId?: number;
  content?: string;
  roadmapItemId?: number;
}

const BlogPost: FC<Props> = ({ postId, content, roadmapItemId }) => {
  const editor = useEditor({
    extensions: [
      StarterKit,
      Underline,
      Link,
      Superscript,
      SubScript,
      Highlight,
      Image,
      Color,
      TextStyle,
      TextAlign.configure({ types: ["heading", "paragraph"] }),
    ],
  });

  const titleRef = useRef<HTMLInputElement>(null);
  const [titleWarning, setTitleWarning] = useState<boolean>(false);

  const [post] = useState<PostUploadRequest>({
    id: postId,
    title: "",
    content: content ?? "",
    image: undefined,
    accessibility: Accessibility.PUBLIC,
    categoryId: undefined,
    roadmapItemId,
  });

  const [completeDrawerOpen, setCompleteDrawerOpen] = useState(false);

  const postUpload = usePostUpload();

  const [clientInfo] = useRecoilState(atomClientInfo);
  const navigate = useNavigate();

  const handleUpload = () => {
    const title = titleRef.current?.value ?? "";
    if (title === "") {
      setTitleWarning(true);
      return;
    }

    post.title = title;
    post.content = editor?.getHTML() ?? "";
    setCompleteDrawerOpen(true);
  };

  const handlePostComplete = () => {
    postUpload.mutate(post, {
      onSuccess: (data) => {
        navigate(`/blog/@${clientInfo?.name}/posts/${data.postId}`);
      },
    });
  };

  return (
    <div className="flex flex-col items-center w-full">
      <div className="flex flex-col w-full max-w-7xl">
        <Header title="글 작성하기" text="" />
        <div className="flex flex-row justify-start items-center w-full h-20 mt-10 py-2 border-y">
          <input
            ref={titleRef}
            className={`flex-1 px-2 text-2xl focus-visible:outline-none ${
              titleWarning && "placeholder:text-red-400"
            }`}
            style={{ color: titleWarning ? "red" : "black" }}
            placeholder="제목을 입력하세요"
            onChange={() => setTitleWarning(false)}
          />
          <Button className="w-24 h-14 bg-blue-600" onClick={handleUpload}>
            완료
          </Button>
        </div>
        <RichTextEditor className="min-h-[36rem]" editor={editor}>
          <RichTextEditor.Toolbar sticky stickyOffset={60}>
            <RichTextEditor.ControlsGroup>
              <RichTextEditor.Control interactive={false}>
                <IconColorPicker size={16} stroke={1.5} />
              </RichTextEditor.Control>
              <RichTextEditor.Color color="#000000" />
              <RichTextEditor.Color color="#F03E3E" />
              <RichTextEditor.Color color="#F59F00" />
              <RichTextEditor.Color color="#37B24D" />
              <RichTextEditor.Color color="#1098AD" />
              <RichTextEditor.Color color="#7048E8" />
            </RichTextEditor.ControlsGroup>

            <RichTextEditor.ControlsGroup>
              <RichTextEditor.UnsetColor />
              <RichTextEditor.ColorPicker colors={[]} />
            </RichTextEditor.ControlsGroup>

            <RichTextEditor.ControlsGroup>
              <RichTextEditor.Bold />
              <RichTextEditor.Italic />
              <RichTextEditor.Underline />
              <RichTextEditor.Strikethrough />
              <RichTextEditor.ClearFormatting />
              <RichTextEditor.Highlight />
              <RichTextEditor.Code />
            </RichTextEditor.ControlsGroup>

            <RichTextEditor.ControlsGroup>
              <RichTextEditor.H1 />
              <RichTextEditor.H2 />
              <RichTextEditor.H3 />
              <RichTextEditor.H4 />
              <RichTextEditor.H5 />
              <RichTextEditor.H6 />
            </RichTextEditor.ControlsGroup>

            <RichTextEditor.ControlsGroup>
              <RichTextEditor.Blockquote />
              <RichTextEditor.Hr />
              <RichTextEditor.BulletList />
              <RichTextEditor.OrderedList />
              <RichTextEditor.Subscript />
              <RichTextEditor.Superscript />
            </RichTextEditor.ControlsGroup>

            <RichTextEditor.ControlsGroup>
              <RichTextEditor.Link />
              <RichTextEditor.Unlink />
            </RichTextEditor.ControlsGroup>

            <RichTextEditor.ControlsGroup>
              <RichTextEditor.AlignLeft />
              <RichTextEditor.AlignCenter />
              <RichTextEditor.AlignJustify />
              <RichTextEditor.AlignRight />
            </RichTextEditor.ControlsGroup>
          </RichTextEditor.Toolbar>

          <RichTextEditor.Content />
        </RichTextEditor>
      </div>
      <BlogPostCompleteDrawer
        post={post}
        opened={completeDrawerOpen}
        onClose={() => setCompleteDrawerOpen(false)}
        onFinish={handlePostComplete}
      />
    </div>
  );
};

export default withAuth(BlogPost, true);
