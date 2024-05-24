
import React, { useEffect } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import "./Hrlayout.scss";
import Navbar from "../../components/Navbar/Navbar";
const HrLayout = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const checkPath = () => {
      if (window.location.pathname.endsWith("/hr")) {
        navigate("LoginPage");
      } else if (window.location.pathname.endsWith("/hr")) {
        navigate("LoginPage");
      }
    };

    checkPath();
  }, [navigate]);

  return (
    <div className="layout">
      <Navbar className="Navbar" />
      <div className="outlet">
        <Outlet />
      </div>
    </div>
  );
};

export default HrLayout;
