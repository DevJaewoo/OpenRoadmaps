import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Footer from "./components/Footer";
import Navigation from "./components/Navigation";
import NotFound from "./pages/error/NotFound";
import Register from "./pages/login/register";
import Main from "./pages/main/Main";

function App() {
  return (
    <div className="flex flex-col App min-w-[640px] min-h-screen pt-16">
      <BrowserRouter>
        <Navigation />
        <Routes>
          <Route path="/" element={<Main />} />
          <Route path="/register" element={<Register />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
        <Footer />
      </BrowserRouter>
    </div>
  );
}

export default App;
