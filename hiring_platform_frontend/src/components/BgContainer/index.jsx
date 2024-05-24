import React from "react";
import "./Container.scss";
import Button from "../Button";
import { FaLock } from "react-icons/fa";
import { useNavigate } from "react-router-dom";

function Container({ heading, path, hrpath,date }) {
  const navigate = useNavigate();

  const handleStart = () => {
    if (path) {
      navigate(path);
    } else if (hrpath) {
      navigate(hrpath);
    } else {
    }
  };

  return (
    <div className="Container">
      <h3 className="roundsdata">{heading}</h3>
      <Button
        className="startButton"
        text={date}
        onClick={handleStart}
      />
    </div>
  );
}

export default Container;
