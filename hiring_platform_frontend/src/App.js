import React from "react";
import Navbar from "./components/Navbar/Navbar.jsx";
import "./App.css";
import AdminRouter from "./routers/AdminRouter";
import CodingTest from "./components/CodingTest";

function App() {
  <Navbar />;
  return (
    <>
      <AdminRouter />
      {/* < CodingTest/> */}
    </>
  );
}

export default App;
