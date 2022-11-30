import { FaArrowRight } from "react-icons/fa";
import { OutlinedButton } from "src/components/button/VariantButtons";
import StableImage from "src/components/StableImage";

interface SectionProps {
  title: string;
  text: string;
  button: string;
  link: string;
  src?: string;
  l2r: boolean;
}

const Welcome: React.FC<{}> = () => {
  return (
    <div className="flex flex-col justify-center items-center w-full h-[36rem] bg-gradient-to-br from-blue-500 to-violet-700">
      <h1 className="text-7xl font-bold">OpenRoadmaps</h1>
      <p className="mt-8 text-2xl font-semibold">Lorem ipsum dolor sit amet</p>
    </div>
  );
};

const Section: React.FC<SectionProps> = ({
  title,
  text,
  button,
  link,
  src,
  l2r,
}) => {
  const bgColor: string = l2r ? "bg-white" : "bg-slate-300";
  const flexDirection: string = l2r ? "flex-row" : "flex-row-reverse";
  const margin: string = l2r ? "mr-10" : "ml-10";
  const bgImage: string = l2r ? "bg-indigo-400" : "bg-indigo-600";

  return (
    <div className={`flex justify-center w-full ${bgColor}`}>
      <div className="flex flex-col items-center w-full max-w-screen-xl py-10">
        <h2 className="pb-4 text-4xl">{title}</h2>
        <div className={`w-full mx-auto flex ${flexDirection}`}>
          <StableImage
            src={src}
            alt=""
            className={`w-96 h-96 mx-10 p-10 rounded-[3rem] ${bgImage}`}
          />
          <div
            className={`flex flex-col flex-grow ${margin} justify-center items-start`}
          >
            <p className="text-3xl whitespace-pre-line">{text}</p>
            <OutlinedButton
              type="link"
              to={link}
              className="h-12 mt-6"
              text={
                <>
                  {button}
                  <FaArrowRight className="ml-2" />
                </>
              }
            />
          </div>
        </div>
      </div>
    </div>
  );
};

const Main: React.FC<{}> = () => {
  const sections: SectionProps[] = [
    {
      title: "Roadmaps",
      text: "개발 공부는 하고 싶은데 어떻게 시작해야 할지 막막하신가요?\n로드맵을 따라 기초부터 차근차근 공부해보세요!",
      button: "로드맵 바로가기",
      link: "/roadmaps",
      src: "/assets/roadmap.png",
      l2r: true,
    },
    {
      title: "Projects",
      text: "내가 만든 프로젝트도 자랑하고,\n다른 사람이 만든 프로젝트도 구경해보세요!",
      button: "프로젝트 바로가기",
      link: "/projects",
      src: "/assets/project.png",
      l2r: false,
    },
    {
      title: "Blog",
      text: "나만의 지식과 노하우를 공유해보세요!",
      button: "블로그 바로가기 ",
      link: "/blog",
      src: "/assets/blog.png",
      l2r: true,
    },
    {
      title: "Community",
      text: "궁금하거나 내 힘으로 해결되지 않는 문제가 있으신가요?\n혼자 해결하기 힘들 땐 다른 사람들에게 도움을 받아보세요!",
      button: "커뮤니티 바로가기",
      link: "/community",
      src: "/assets/community.png",
      l2r: false,
    },
  ];
  return (
    <div className="flex flex-col items-center w-full">
      <Welcome />
      {sections.map((section) => (
        <Section {...section} key={section.title} />
      ))}
    </div>
  );
};

export default Main;
