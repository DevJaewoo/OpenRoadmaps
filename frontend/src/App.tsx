import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { useRecoilState } from "recoil";
import { atomClientInfo } from "./atoms/client";
import Footer from "./components/Footer";
import Navigation from "./components/Navigation";
import NotFound from "./pages/error/NotFound";
import Login from "./pages/login/login";
import OAuth from "./pages/login/oauth";
import Register from "./pages/login/register";
import Main from "./pages/main/Main";
import Roadmaps from "./pages/roadmaps/roadmaps";

function App() {
  const [clientInfo] = useRecoilState(atomClientInfo);

  return (
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
              clientInfo === undefined ? <Login /> : <Navigate replace to="/" />
            }
          />
          <Route
            path="/oauth"
            element={
              clientInfo === undefined ? <OAuth /> : <Navigate replace to="/" />
            }
          />
          <Route path="/roadmaps" element={<Roadmaps />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
        <Footer />
      </BrowserRouter>
    </div>
  );
}

export default App;
