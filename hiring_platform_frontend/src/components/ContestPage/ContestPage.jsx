import React from "react";
import "/home/divum/hiring_platform/hiring-platform-frontend/src/components/ContestPage.scss";
import ArrowForwardIosIcon from '@mui/icons-material/ArrowForwardIos';

function Container() {
  return (
    <div className="side-bar"> 
      <div className="home-bar">HOME</div>
      <div className="contestlog-bar">CONTEST LOG</div>
      <div className="contest-block">
        <span>CONTEST NAME</span>
        <ArrowForwardIosIcon className="next-icon" />
      </div>
    </div>
  );
}

export default Container;
