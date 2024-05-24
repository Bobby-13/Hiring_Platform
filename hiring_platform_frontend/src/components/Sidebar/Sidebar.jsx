import React, { useState } from "react";
import "./Sidebar.scss";
import { IoMdArrowDropdown } from "react-icons/io";

const Sidebar = () => {
  const [isDropdownOpen, setDropdownOpen] = useState(false);

  const toggleDropdown = () => {
    setDropdownOpen(!isDropdownOpen);
  };
  return (
    <div className="sidebar">
      <div className="dropdown-container">
        <div
          className={`dropdown ${isDropdownOpen ? "open" : ""}`}
          onClick={toggleDropdown}
        >
          <button className="dropdown__text">
            RECRUITMENT PROCESS <IoMdArrowDropdown className="sidebar-icon" />{" "}
          </button>

          {isDropdownOpen && (
            <div className="dropdown-content">
              <a href="/" className="dropdown-list1">
                {" "}
                MCQ
              </a>
              <a href="/" className="dropdown-list1">
                {" "}
                CODING
              </a>
              <a href="/" className="dropdown-list">
                {" "}
                HR
              </a>
            </div>
          )}
        </div>
      </div>
    </div>
  );
};

export default Sidebar;
