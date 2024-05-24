import React from "react";
import { useNavigate } from "react-router";
import "./Sidebar.scss";
import { GrFormPrevious } from "react-icons/gr";

const HrSidebar = () => {
  const navigate = useNavigate();

  const handleHome = () => {
    navigate(`/hr/hrhome/${sessionStorage.getItem("userId")}`);
  };

  const handleContest = () => {
    navigate(`/hr/loghome/${sessionStorage.getItem("userId")}`);
  };
  return (
    <div className="sidebar-conatiner">
      <div className="sidebarButtonContainer">
        {/* <button>
          <GrFormPrevious />
          <a className="back-button" href="/back">
            BACK
          </a>
        </button> */}
        <button className="sidebar-content" onClick={handleHome}>
          HOME
        </button>
        <button className="sidebar-content" onClick={handleContest}>
          CONTEST LOG
        </button>
      </div>
    </div>
  );
};

export default HrSidebar;
