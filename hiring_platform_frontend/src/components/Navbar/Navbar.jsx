import React, { useState } from "react";
import "./Navbar.scss"; 
import assets from "../../assets";
import { BsPersonCircle } from "react-icons/bs";
import { RiArrowDropDownLine } from "react-icons/ri";
import { FiLogOut } from "react-icons/fi";
import Popup from "../Popup";
import Button from "../Button";

const Navbar = () => {
  const [isDropdownOpen, setDropdownOpen] = useState(false);
  const [showLogoutConfirmation, setShowLogoutConfirmation] = useState(false);

  const toggleDropdown = () => {
    setDropdownOpen(!isDropdownOpen);
  };

  const handleResetPassword = () => {
    window.location.href = "/hr/changepassword";
  };

  const handleLogout = () => {
    setShowLogoutConfirmation(true);
  };

  const confirmLogout = () => {
    sessionStorage.clear();
    window.location.href = "/";
  };

  const cancelLogout = () => {
    setShowLogoutConfirmation(false);
  };

  return (
    <div className="navbar">
      <div className="navcontent">
        <div>
          <img className="logoimg" src={assets.Logo} alt="/" />
        </div>
        <div className="Heading">DIVUM HIRING PLATFORM</div>
        <div className="profile">
          <BsPersonCircle className="profile-icon" />
          <div className="usercontent">
            <div className={`dropdown ${isDropdownOpen ? "open" : ""}`} onClick={toggleDropdown}>
              <p className="dropdown__text">{sessionStorage.getItem("name")}</p>
              {isDropdownOpen && (
                <div className="dropdown-content">
                  <div className="dropdown-item">{sessionStorage.getItem("email")}</div>
                  <div className="dropdown-item" onClick={handleResetPassword}>Change Password</div>
                  <div className="dropdown-item" onClick={handleLogout}>
                    <FiLogOut className="logouticon" /> Log Out
                  </div>
                </div>
              )}
              <RiArrowDropDownLine className="dropicon" />
            </div>
          </div>
        </div>
      </div>
      <Popup
      
        trigger={showLogoutConfirmation}
        setTrigger={setShowLogoutConfirmation}
        body={<p>Are you sure you want to log out?</p>}
        footer={
          <>
            <Button onClick={confirmLogout} className="key-button" text="Yes"/>
            <Button onClick={cancelLogout} className="key-button" text="No"/>
          </>
        }
      />
    </div>
  );
};

export default Navbar;
