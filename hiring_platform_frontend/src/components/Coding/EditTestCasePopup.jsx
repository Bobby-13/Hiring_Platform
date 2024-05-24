import React, { useState } from "react";
import Button from "../Button";
import { TestCasesOptions } from "../../helpers/addQuestionsHelper";
import Popup from "../Popup/index";
import "/home/divum/shobi/trail/hiring-platform-frontend/src/components/Coding/AddTestCasePopup.jsx"; // Assuming you have a separate SCSS file for styling

const EditTestCasePopup = ({ testCase, onSave, onClose }) => {
  const [input, setInput] = useState(testCase.input);
  const [output, setOutput] = useState(testCase.output);
  const [type, setType] = useState(testCase.type);
  const [isPopupOpen, setIsPopupOpen] = useState(false);

  const handleSave = () => {
    onSave({ input, output, type });
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
              checked={type === typeOption}
              onChange={() => setType(typeOption)}
            />
            <label>{typeOption}</label>
          </div>
        ))}
      </div>
    </>
  );

  const heading = "Edit Test Case";
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

export default EditTestCasePopup;
