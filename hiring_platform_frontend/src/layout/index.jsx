import React, { useEffect } from "react";
import "./layout.scss";
import SideBar from "../containers/AdminSideBar";
import { Outlet, useNavigate } from "react-router-dom";


const Layout = () => {

  const navigate = useNavigate();

  useEffect(() => {
    const checkPath = () => {
      if (window.location.pathname.endsWith("/admin")) {
        navigate("home");
      } else if (window.location.pathname.endsWith("/admin/")) {
        navigate("home");
      }
    };

    checkPath();
  }, [navigate]);

  return (
    <div className="Layout">
      <SideBar />
      <div className="Outlet">
        <Outlet />
      </div>
    </div>

  );

};



export default Layout;

