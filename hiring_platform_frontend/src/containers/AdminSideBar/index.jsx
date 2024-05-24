
import React, { useState } from "react";
import "./sidebar.scss";
import assets from "../../assets/index";
import Button from "../../components/Button";
import { SideBarNavigation } from "../../helpers/sidebarList";
import { MdAccountCircle } from "react-icons/md";
import { FaChevronDown, FaChevronUp } from "react-icons/fa";
import { NavLink } from "react-router-dom";
import { FiLogOut } from "react-icons/fi";

function SideBar({ className }) {
  const [accountAction, setAccountAction] = useState(false);

  const handleAccountClick = () => {
    setAccountAction(!accountAction);
  };

  const adminAccessAction = () => {
    alert("button is been pressed !");

    setAccountAction(!accountAction);
  };

  const data = [
    {
      text: "admin@gmail.com",
      icon: <MdAccountCircle />,
    },
    {
      text: "Change Password",
    },
    {
      text: "Logout",
      icon: <FiLogOut />,
    },
  ];
  return (
    <div className={"SideBar " + className}>
      <div className="sideBarNavigationSection">
        <img src={assets.Logo} alt="Divum" />
        <div className="SideBarNavigationIconSection">
          {SideBarNavigation.map(({ image, text, path }, NaviIndex) => {
            return (
              <NavLink to={path} className="navigationButton" key={NaviIndex}>
                {image}
                <Button text={text} className="NavLinksIcons" />
              </NavLink>
            );
          })}
        </div>
      </div>
      <div className="AccountInfo" onClick={handleAccountClick}>
        {accountAction && (
          <div className="AccountActionSection">
            {data.map(({ text, icon }, dataIndex) => {
              return (
                <Button
                  text={text}
                  icon={icon}
                  className="adminAccountAction"
                  onClick={() => {
                    adminAccessAction();
                  }}
                  index={dataIndex}
                />
              );
            })}
          </div>
        )}
        <MdAccountCircle />
        <Button text={"Admin Access"} className="accountTitle" />
        {accountAction ? (
          <FaChevronUp className="fs-14" />
        ) : (
          <FaChevronDown className="AccountActionBTN" />
        )}
      </div>
    </div>
  );
}

export default SideBar;
