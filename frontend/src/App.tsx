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
import RoadmapMain from "src/pages/roadmaps/Roadmaps";
import withAuth from "src/hoc/withAuth";
import Profile from "./pages/profile/Profile";

const App: React.FC<{}> = () => {
  const AuthRegister = withAuth(Register, false);
  const AuthLogin = withAuth(Login, false);
  const AuthOAuth = withAuth(OAuth, false);

  return (
    <Suspense fallback={<div>Loading...</div>}>
      <ErrorBoundary fallback={<div>Error!</div>}>
        <div className="flex flex-col App min-w-[640px] min-h-screen pt-16">
          <BrowserRouter>
            <Navigation />
            <Routes>
              <Route path="/" element={<Main />} />
              <Route path="/register" element={<AuthRegister />} />
              <Route path="/login" element={<AuthLogin />} />
              <Route path="/oauth" element={<AuthOAuth />} />
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
