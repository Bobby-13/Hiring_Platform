import React, { useState } from "react";
import styles from "./index.module.scss";
import { IoMdArrowDropdown, IoMdArrowDropup } from "react-icons/io";

const Select = ({ dataSet }) => {
  dataSet = ["All", "Manager"];
  const [value, setValue] = useState(dataSet[0]);
  const [showDropDown, setShowDropDown] = useState(false);

  return (
    <div className={`${styles.select_component} flex-align-center`}>
      <div className={styles.select_component_box}>
        <p>{value}</p>
        <p onClick={() => setShowDropDown(!showDropDown)}>
          {showDropDown ? <IoMdArrowDropup /> : <IoMdArrowDropdown />}
        </p>
      </div>
      {showDropDown && (
        <div className={`${styles.options} flex-column`}>
          {dataSet.map((filter) => (
            <div
              key={filter}
              className={filter === value && `${styles.selected}`}
              onClick={() => setValue(filter)}
            >
              {filter}
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default Select;
