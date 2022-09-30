import React from "react";
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Navigation from "./components/Navigation";

function App() {
  return (
    <div className="App min-w-[640px]">
      <BrowserRouter>
        <Navigation />
        <Routes>
          <Route path="/" element={<></>} />
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
