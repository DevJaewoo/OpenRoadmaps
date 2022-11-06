import React, { Suspense } from "react";
import { ErrorBoundary } from "react-error-boundary";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { useRecoilState } from "recoil";
import { atomClientInfo } from "src/atoms/client";
import Footer from "src/components/Footer";
import Navigation from "src/components/Navigation";
import NotFound from "src/pages/error/NotFound";
import Login from "src/pages/login/Login";
import OAuth from "src/pages/login/OAuth";
import Register from "src/pages/login/Register";
import Main from "src/pages/main/Main";
import RoadmapMain from "src/pages/roadmaps/Roadmaps";
import Profile from "./pages/profile/Profile";

const App: React.FC<{}> = () => {
  const [clientInfo] = useRecoilState(atomClientInfo);

  return (
    <Suspense fallback={<div>Loading...</div>}>
      <ErrorBoundary fallback={<div>Error!</div>}>
        <div className="flex flex-col App min-w-[640px] min-h-screen pt-16">
          <BrowserRouter>
            <Navigation />
            <Routes>
              <Route path="/" element={<Main />} />
              <Route
                path="/register"
                element={
                  clientInfo === undefined ? (
                    <Register />
                  ) : (
                    <Navigate replace to="/" />
                  )
                }
              />
              <Route
                path="/login"
                element={
                  clientInfo === undefined ? (
                    <Login />
                  ) : (
                    <Navigate replace to="/" />
                  )
                }
              />
              <Route
                path="/oauth"
                element={
                  clientInfo === undefined ? (
                    <OAuth />
                  ) : (
                    <Navigate replace to="/" />
                  )
                }
              />
              <Route path="/roadmaps/*" element={<RoadmapMain />} />
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
