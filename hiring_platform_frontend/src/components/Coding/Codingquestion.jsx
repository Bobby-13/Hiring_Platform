import React, { useState, useRef, useEffect } from "react";
import "./Codingquestion.scss";
import Button from "../Button";
import { FiImage, FiPlus, FiEdit, FiTrash } from "react-icons/fi";
import Popup from "../Popup";
import { TestCasesOptions } from "../../helpers/addQuestionsHelper";
import Bulkdata from "../Bulkdata/index";
import "react-accessible-accordion/dist/fancy-example.css";
import axios from "axios";
const CodingQuestion = ({ getvalue }) => {
  const [testCases, setTestCases] = useState([]);
  const [isEditPopupOpen, setIsEditPopupOpen] = useState(false);
  const [uploadpopup, setUploadpopup] = useState(false);
  const [selectedTestCaseIndex, setSelectedTestCaseIndex] = useState(null);
  const [isAddTestCasePopupOpen, setIsAddTestCasePopupOpen] = useState(false);
  const [input, setInput] = useState("");
  const [output, setOutput] = useState("");
  const [type, setType] = useState("sample");
  const [imagePreviews, setImagePreviews] = useState([]);
  const questionTypeLabels = [
    { value: "ALGORITHMS_CODING", label: "Algorithms" },
    { value: "DATA_STRUCTURE_CODING", label: "Data structures" },
    { value: "STRINGS_CODING", label: "Strings" },
    { value: "MATHEMATICS_CODING", label: "Mathematics" },
    { value: "PATTERN_CODING", label: "Patterns" },
  ];

  const weightageLabels = [
    { value: "EASY", label: "Easy" },
    { value: "MEDIUM", label: "Medium" },
    { value: "HARD", label: "Hard" },
  ];

  const questionInputRef = useRef("");
  const constraintRef = useRef("");
  const cFunctionalCodeRef = useRef("");
  const cppFunctionalCodeRef = useRef("");
  const javaFunctionalCodeRef = useRef("");
  const pythonFunctionalCodeRef = useRef("");
  const cStaticCodeRef = useRef("");
  const cppStaticCodeRef = useRef("");
  const javaStaticCodeRef = useRef("");
  const pythonStaticCodeRef = useRef("");
  const imageUrlRef = useRef([]);

  const [CodeDropDown, setCodeDropDown] = useState({
    StaticC: false,
    StaticCPP: false,
    StaticJAVA: false,
    StaticPYTHON: false,
    FunctionalC: false,
    FunctionalCPP: false,
    FunctionalJAVA: false,
    FunctionalPYTHON: false,
  });

  const handleCodeDropDown = (language) => {
    setCodeDropDown((prevState) => ({
      ...prevState,
      [language]: !prevState[language],
    }));
  };

  const popupBody = () => {
    return <Bulkdata />;
  };

  const codingadd = async (data) => {
    try {
      const response = await axios.post(
        "http://192.168.1.20:8081/api/v1/coding-question",
        data
      );
      console.log("out", response.data);
    } catch (err) {
      console.log(err);
    }
  };
  const handleSave = () => {
    const questionTypeInput = document.querySelector(
      'input[name="question-type"]:checked'
    );
    const weightageInput = document.querySelector(
      'input[name="weightage"]:checked'
    );

    if (!questionTypeInput || !weightageInput) {
      console.error("Question type or weightage not selected.");
      return;
    }

    const selectedData = {
      category: questionTypeInput.value,
      difficulty: weightageInput.value,
    };

    const questionInputValue = questionInputRef.current.value;
    const constraints = constraintRef.current.value;

    const StaticCode = [
      {
        codeLanguage: "C",
        code: cStaticCodeRef?.current?.value,
      },
      {
        codeLanguage: "CPP",
        code: cppStaticCodeRef?.current?.value,
      },
      {
        codeLanguage: "JAVA",
        code: javaStaticCodeRef?.current?.value,
      },
      {
        codeLanguage: "PYTHON",
        code: pythonStaticCodeRef?.current?.value,
      },
    ];

    const FunctionalCode = [
      {
        codeLanguage: "C",
        code: cFunctionalCodeRef?.current?.value,
      },
      {
        codeLanguage: "CPP",
        code: cppFunctionalCodeRef?.current?.value,
      },
      {
        codeLanguage: "JAVA",
        code: javaFunctionalCodeRef?.current?.value,
      },
      {
        codeLanguage: "PYTHON",
        code: pythonFunctionalCodeRef?.current?.value,
      },
    ];

    const finalData = {
      category: selectedData.category,
      difficulty: selectedData.difficulty,
      question: questionInputValue,
      constraints: constraints,
      staticCodes: StaticCode,
      functionCodes: FunctionalCode,
      casesList: testCases,
      imageUrl: imageUrlRef.current,
    };
    console.log("finalData image", finalData.imageUrl);
    codingadd(finalData);

    setTestCases([]);
    setIsEditPopupOpen(false);
    setUploadpopup(false);
    setSelectedTestCaseIndex(null);
    setImagePreviews([]);
    setIsAddTestCasePopupOpen(false);
    setInput("");
    setOutput("");
    setType("");
    questionInputRef.current.value = "";
    constraintRef.current.value = "";
    cFunctionalCodeRef.current.value = "";
    cppFunctionalCodeRef.current.value = "";
    javaFunctionalCodeRef.current.value = "";
    pythonFunctionalCodeRef.current.value = "";
    cStaticCodeRef.current.value = "";
    cppStaticCodeRef.current.value = "";
    javaStaticCodeRef.current.value = "";
    pythonStaticCodeRef.current.value = "";

    const categoryRadios = document.querySelectorAll(
      'input[name="question-type"]'
    );
    categoryRadios.forEach((radio) => {
      radio.checked = false;
    });

    const weightageRadios = document.querySelectorAll(
      'input[name="weightage"]'
    );
    weightageRadios.forEach((radio) => {
      radio.checked = false;
    });
  };

  const handleBulkData = () => {
    const selectedFile = {
      name: "Example.pdf",
      type: "application/pdf",
    };
    getvalue(selectedFile);
    setUploadpopup(true);
  };

  const handleAddTestCase = () => {
    setIsAddTestCasePopupOpen(true);
  };

  const handleSaveTestCase = (i, j, type) => {
    setTestCases([...testCases, { input: i, output: j, casesType: type }]);
  };

  const handleEditTestCase = (index) => {
    setSelectedTestCaseIndex(index);
    setIsEditPopupOpen(true);
  };

  const handleImageUpload = (event) => {
    const files = event?.target.files;
    const uploadedImagePreviews = [];

    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      if (
        file.type === "image/jpeg" ||
        file.type === "image/png" ||
        file.type === "image/jpg"
      ) {
        const imageUrl = URL.createObjectURL(file);
        uploadedImagePreviews.push({ url: imageUrl });
      } else {
        alert("Invalid file type. Please select JPEG, JPG, or PNG files.");
      }
    }

    const filteredImagePreviews = uploadedImagePreviews.filter(
      (preview) => Object.keys(preview).length !== 0
    );

    setImagePreviews([...imagePreviews, ...filteredImagePreviews]);
    imageUrlRef.current = [...imageUrlRef.current, ...filteredImagePreviews];
  };

  const handleDeleteImage = (index) => {
    const updatedImagePreviews = [...imagePreviews];
    const deletedImageUrl = updatedImagePreviews.splice(index, 1)[0];
    setImagePreviews(updatedImagePreviews);
    imageUrlRef.current = imageUrlRef.current.filter(
      (url) => url !== deletedImageUrl
    );
  };
  useEffect(() => {
    console.log(testCases);
  }, [testCases]);
  const popUpBody = () => {
    return (
      <>
        <input
          className="add-testcase-popup"
          type="text"
          placeholder="Input"
          value={input}
          onChange={(e) => setInput(e.target.value)}
        />
        <input
          className="add-testcase-popup"
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
  };
  const popUpbody = () => {
    return (
      <>
        <input
          className="edit-testcase-popup"
          type="text"
          placeholder="Input"
          value={input}
          onChange={(e) => setInput(e.target.value)}
        />
        <input
          className="edit-testcase-popup"
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
  };

  const handleSaveEditedTestCase = () => {
    const editedTestCase = {
      input: input,
      output: output,
      casesType: type,
    };
    const updatedTestCases = [...testCases];
    updatedTestCases[selectedTestCaseIndex] = editedTestCase;
    setTestCases(updatedTestCases);
    setIsEditPopupOpen(false);
  };

  const handleDeleteTestCase = (index) => {
    const updatedTestCases = [...testCases];
    updatedTestCases.splice(index, 1);
    setTestCases(updatedTestCases);
  };

  return (
    <div className="CodingQuestionContainer">
      <Button
        text="Upload Bulk Data"
        className="bulkupload-button"
        onClick={handleBulkData}
      />
      <Popup
        trigger={uploadpopup}
        setTrigger={setUploadpopup}
        body={popupBody()}
      ></Popup>
      <hr className="line" />
      <div className="mcq-heading">
        <h2>Coding Type:</h2>
        <div className="radio-container">
          {questionTypeLabels.map((radio, index) => (
            <label key={index}>
              <input type="radio" name="question-type" value={radio.value} />
              {radio.label}
            </label>
          ))}
        </div>
      </div>
      <div className="weightage-setup">
        <h2>Weightage:</h2>
        <div className="radio-container-wt">
          {weightageLabels.map((radio, index) => (
            <label key={index}>
              <input type="radio" name="weightage" value={radio.value} />
              {radio.label}
            </label>
          ))}
        </div>
      </div>
      <div className="question-setup">
        <h2>Question:</h2>
        <div className="question-container">
          <input
            type="file"
            id="image-upload"
            style={{ display: "none" }}
            className="image-file"
            multiple
            onChange={handleImageUpload}
            accept=".jpg,.jpeg,.png"
          />

          <div className="Questioninput-container">
            <label htmlFor="image-upload">
              <FiImage className="image-icon" />
            </label>
            <h3>Question</h3>
            <textarea
              className="textarea-container"
              ref={questionInputRef}
              placeholder="Enter question..."
            />
          </div>
          <div className="Questioninput-container">
            <h3>Contraints</h3>
            <textarea
              className="textarea-container"
              placeholder="Enter constraint..."
              ref={constraintRef}
            />
          </div>
        </div>
        <div className="image-previews-container">
          <div className="image-previews">
            {imagePreviews.map((imageUrl, index) => (
              <React.Fragment key={index}>
                <img src={imageUrl} alt={`Image ${index + 1}`} />

                <FiTrash className="delete-icons" onClick={handleDeleteImage} />
              </React.Fragment>
            ))}
          </div>
        </div>
      </div>
      <div className="additional-input">
        <h2>Static code</h2>
        <div className="functional-container">
          <div className="functionalinput-container">
            <h3 onClick={() => handleCodeDropDown("StaticC")}>C</h3>
            {CodeDropDown.StaticC && (
              <textarea className="textarea-container" ref={cStaticCodeRef} />
            )}
          </div>
          <div className="functionalinput-container">
            <h3 onClick={() => handleCodeDropDown("StaticCPP")}>C++</h3>
            {CodeDropDown.StaticCPP && (
              <textarea className="textarea-container" ref={cppStaticCodeRef} />
            )}
          </div>
          <div className="functionalinput-container">
            <h3 onClick={() => handleCodeDropDown("StaticJAVA")}>JAVA</h3>
            {CodeDropDown.StaticJAVA && (
              <textarea
                className="textarea-container"
                ref={javaStaticCodeRef}
              />
            )}
          </div>
          <div className="functionalinput-container">
            <h3 onClick={() => handleCodeDropDown("StaticPYTHON")}>PYTHON</h3>
            {CodeDropDown.StaticPYTHON && (
              <textarea
                className="textarea-container"
                ref={pythonStaticCodeRef}
              />
            )}
          </div>
        </div>
      </div>
      <div className="additional-input">
        <h2>Functional code</h2>

        <div className="functional-container">
          <div className="functionalinput-container">
            <h3 onClick={() => handleCodeDropDown("FunctionalC")}>C</h3>
            {CodeDropDown.FunctionalC && (
              <textarea
                className="textarea-container"
                ref={cFunctionalCodeRef}
              />
            )}
          </div>
          <div className="functionalinput-container">
            <h3 onClick={() => handleCodeDropDown("FunctionalCPP")}>C++</h3>
            {CodeDropDown.FunctionalCPP && (
              <textarea
                className="textarea-container"
                ref={cppFunctionalCodeRef}
              />
            )}
          </div>
          <div className="functionalinput-container">
            <h3 onClick={() => handleCodeDropDown("FunctionalJAVA")}>JAVA</h3>
            {CodeDropDown.FunctionalJAVA && (
              <textarea
                className="textarea-container"
                ref={javaFunctionalCodeRef}
              />
            )}
          </div>
          <div className="functionalinput-container">
            <h3 onClick={() => handleCodeDropDown("FunctionalPYTHON")}>
              PYTHON
            </h3>
            {CodeDropDown.FunctionalPYTHON && (
              <textarea
                className="textarea-container"
                ref={pythonFunctionalCodeRef}
              />
            )}
          </div>
        </div>
      </div>
      <div className="add-option">
        <FiPlus className="plus-icon" onClick={handleAddTestCase} />
        <span>Add Test Cases</span>
      </div>
      <div className="test-cases-container">
        {testCases.map((testCase, index) => (
          <div key={index} className="test-case">
            <div>
              <p
                onClick={() => handleEditTestCase(index)}
                className="edit-icon"
              >
                Test Case {index + 1}
              </p>
              <FiTrash
                className="delete-icon"
                onClick={() => handleDeleteTestCase(index)}
              />{" "}
            </div>
          </div>
        ))}
      </div>
      <Button text="Save" className="save-button" onClick={handleSave} />

      <Popup
        trigger={isAddTestCasePopupOpen}
        setTrigger={setIsAddTestCasePopupOpen}
        heading="Add Test Case"
        body={popUpBody()}
        footer={
          <Button
            className="AddEmployeeSaveBtn"
            text="Save"
            onClick={() => handleSaveTestCase(input, output, type)}
          />
        }
      />

      <Popup
        trigger={isEditPopupOpen}
        setTrigger={setIsEditPopupOpen}
        heading="Edit Test Case"
        body={popUpbody()}
        footer={
          <Button
            className="AddEmployeeSaveBtn"
            text="Save"
            onClick={handleSaveEditedTestCase}
          />
        }
      />
    </div>
  );
};

export default CodingQuestion;
