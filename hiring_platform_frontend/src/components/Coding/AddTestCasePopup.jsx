import React, { useState } from "react";
import Button from "../Button";
import { TestCasesOptions } from "../../helpers/addQuestionsHelper";
import Popup from "../Popup/index";
import "/home/divum/shobi/trail/hiring-platform-frontend/src/components/Coding/AddTestCasePopup.jsx"; // Assuming you have a separate SCSS file for styling

const AddTestCasePopup = ({ onSave, onClose }) => {
  const [input, setInput] = useState("");
  const [output, setOutput] = useState("");
  const [casesType, setcasesType] = useState("sample");
  const [isPopupOpen, setIsPopupOpen] = useState(false);

  const handleSave = () => {
    onSave({ input, output, casesType});
    setInput("");
    setOutput("");
    setcasesType("sample");
    setIsPopupOpen(false);
  };

  const body = (
    <>
      <input
        type="text"
        placeholder="Input"
        value={input}
        onChange={(e) => setInput(e.target.value)}
      />
      <input
        type="text"
        placeholder="Output"
        value={output}
        onChange={(e) => setOutput(e.target.value)}
      />
      <div className="TestcaseType">
        <label>Type:</label>
        {TestCasesOptions.map((typeOption, index) => (
          <div key={index} className="TestCaseTypeInner">
            <input
              type="radio"
              value={typeOption}
              checked={casesType=== typeOption}
              onChange={() => setcasesType(typeOption)}
            />
            <label>{typeOption}</label>
          </div>
        ))}
      </div>
    </>
  );

  const heading = "Add Test Case";
  const footer = (
    <>
      <Button text="Save" onClick={handleSave} />
    </>
  );

  return (
    <Popup
      trigger={isPopupOpen}
      setTrigger={setIsPopupOpen}
      body={body}
      heading={heading}
      footer={footer}
    />
  );
};

export default AddTestCasePopup;
