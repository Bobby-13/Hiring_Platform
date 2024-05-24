import React from "react";
import Button from "../Button/index";
import assets from "../../assets";
import Images from "../Image/index";
import "./NavBar.scss";

function NavBar() {
  return (
    <div className="NavBar">
      <Images src={assets.Logo} className="Logo" alt="Divum"  />
      <h2 className="navbarTitle">Divum Hiring Platform</h2>
      <div className="navbarTopRight">
        <Button text="About" />
        <Button text="Contact"  />
        <Button text="Login" className="navbarLogin" />
      </div>
    </div>
  );
}

export default NavBar;
