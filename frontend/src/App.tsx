import React, { Suspense } from "react";
import { ErrorBoundary } from "react-error-boundary";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Footer from "src/components/Footer";
import Navigation from "src/components/Navigation";
import NotFound from "src/pages/error/NotFound";
import Login from "src/pages/login/Login";
import OAuth from "src/pages/login/OAuth";
import Register from "src/pages/login/Register";
import Main from "src/pages/main/Main";
import Roadmaps from "src/pages/roadmaps/Roadmaps";
import Blog from "src/pages/blog/Blog";
import Profile from "src/pages/profile/Profile";
import { useCurrentClient } from "src/apis/useClient";

const App: React.FC<{}> = () => {
  useCurrentClient();

  return (
    <Suspense fallback={<div>Loading...</div>}>
      <ErrorBoundary fallback={<div>Error!</div>}>
        <div className="flex flex-col App min-w-[640px] min-h-screen pt-16">
          <BrowserRouter>
            <Navigation />
            <Routes>
              <Route path="/" element={<Main />} />
              <Route path="/register" element={<Register />} />
              <Route path="/login" element={<Login />} />
              <Route path="/oauth" element={<OAuth />} />
              <Route path="/roadmaps/*" element={<Roadmaps />} />
              <Route path="/blog/*" element={<Blog />} />
              <Route
                path="/projects/*"
                element={<NotFound error="추가중인 기능입니다." navigate="/" />}
              />
              <Route
                path="/community/*"
                element={<NotFound error="추가중인 기능입니다." navigate="/" />}
              />
              <Route path="/clients/:clientId" element={<Profile />} />
              <Route path="*" element={<NotFound />} />
            </Routes>
            <Footer />
          </BrowserRouter>
        </div>
      </ErrorBoundary>
    </Suspense>
  );
};

export default App;
