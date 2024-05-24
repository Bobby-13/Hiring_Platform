import React, { useState } from "react";
import "../Switching/Switching.scss";

const Switching = ({
  Arrayofvalue,
  children,
  renderContent,
  className,
  click,
}) => {
  const [selectedOption, setSelectedOption] = useState("MCQ");

  const handleCheck = (value) => {
    setSelectedOption(value);
    renderContent(value);
    sessionStorage.setItem("value",value)
  };

  return (
    <>
      <div className={"switchbox " + className}>
        {Arrayofvalue.map((f) => (
          <div
            key={f}
            style={{
              paddingBottom:"7px",
              borderBottom:
                selectedOption === f ? "4px green solid" : "none",
              cursor: "pointer"
            }}
            onClick={() => {
              handleCheck(f);
            }}
            className={className}
          >
            {f}
          </div>
        ))}
      </div>
      {children && children[selectedOption]}
    </>
  );
};

export default Switching;
