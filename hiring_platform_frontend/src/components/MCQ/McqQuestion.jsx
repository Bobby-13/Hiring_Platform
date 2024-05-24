import React, { useState, useRef, useEffect } from "react";
import "./McqQuestion.scss";
import Button from "../Button";
import { FiImage, FiPlus, FiTrash } from "react-icons/fi";
import Popup from "../Popup";
import Bulkdata from "../Bulkdata/index";
import axios from "axios";

const McqQuestion = ({ getvalue }) => {
  const questionTypeLabels = [
    { value: "LOGICAL_MCQ", label: "Logical" },
    { value: "VERBAL_MCQ", label: "Verbal" },
    { value: "APTITUTE_MCQ", label: "Aptitude" },
    { value: "TECHNICAL_MCQ", label: "Technical" },
  ];

  const weightageLabels = [
    { value: "EASY", label: "Easy" },
    { value: "MEDIUM", label: "Medium" },
    { value: "HARD", label: "Hard" },
  ];

  const buttonTypeLabels = [
    { value: "radio", label: "Radio Button" },
    { value: "checkbox", label: "Checkbox" },
  ];

  const defaultOptions = [
    { name: "Option 1", type: "radio", isChecked: false },
    { name: "Option 2", type: "radio", isChecked: false },
    { name: "Option 3", type: "radio", isChecked: false },
  ];

  const [options, setOptions] = useState(defaultOptions);
  const [correctAnswerIndex, setCorrectAnswerIndex] = useState("");
  const [correctAnswer, setCorrectAnswer] = useState([]);
  const [uploadpopup, setUploadpopup] = useState(false);
  const [imagePreviews, setImagePreviews] = useState([]);
  const questionInputValueRef = useRef("");
  const [correctOptionAnswer, setCorrectOptionAnswer] = useState([]);
  const imageUrlRef = useRef([]);

  const handleAddOption = () => {
    if (options.length < 5) {
      const selectedButtonType = document.querySelector("select").value;
      const newOption = {
        name: `Option ${options.length + 1}`,
        type: selectedButtonType,
        isChecked: false, 
      };
      setOptions([...options, newOption]);
    }
  };

  const popupBody = () => {
    return <Bulkdata />;
  };

  const handleOptionChange = (index, newValue) => {
    const updatedOptions = [...options];
    updatedOptions[index].name = newValue;
    setOptions(updatedOptions);
  };

  const handleDeleteOption = (index) => {
    const updatedOptions = [...options];
    updatedOptions.splice(index, 1);
    setOptions(updatedOptions);
    if (correctAnswerIndex === index) {
      setCorrectAnswer("");
      setCorrectAnswerIndex(null);
    } else if (correctAnswerIndex > index) {
      setCorrectAnswerIndex(correctAnswerIndex - 1);
    }
  };

  const handleSelectCorrectAnswer = (indexNum, option, type) => {
    if (type === "radio") {
      const arr = options.map((val, ind) => ({
        options: val.name,
        isCorrect: ind === indexNum,
      }));
      setCorrectAnswerIndex(indexNum);
      setCorrectOptionAnswer(arr);
      setCorrectAnswer(options[indexNum].name);
    } else if (type === "checkbox") {
      const updatedOptions = options.map((opt, idx) => {
        if (idx === indexNum) {
          return { ...opt, isChecked: !opt.isChecked };
        }
        return opt;
      });

      setOptions(updatedOptions);

      const selectedOptions = updatedOptions
        .filter((opt) => opt.isChecked)
        .map((opt) => opt.name);

      const arr = options.map((opt) => ({
        options: opt.name,
        isCorrect: selectedOptions.includes(opt.name),
      }));

      setCorrectOptionAnswer(arr);
      setCorrectAnswer(selectedOptions.join(", "));
    }
  };

  const handleDeleteImage = (index) => {
    const updatedImagePreviews = [...imagePreviews];
    const deletedImageUrl = updatedImagePreviews.splice(index, 1)[0];
    setImagePreviews(updatedImagePreviews);
    imageUrlRef.current = imageUrlRef.current.filter(
      (url) => url !== deletedImageUrl
    );
  };

  const handleImageUpload = (event) => {
    const files = event.target.files;
    const uploadedImagePreviews = [];

    for (let i = 0; i < files.length; i++) {
      const file = files[i];
      if (
        file.type === "image/jpeg" ||
        file.type === "image/png" ||
        file.type === "image/jpg"
      ) {
        const imageUrl = URL.createObjectURL(file);
        uploadedImagePreviews.push(imageUrl);
      } else {
        alert("Invalid file type. Please select JPEG, JPG, or PNG files.");
      }
    }

    setImagePreviews([...imagePreviews, ...uploadedImagePreviews]);
    imageUrlRef.current = [...imageUrlRef.current, ...uploadedImagePreviews];
  };

  const mcqadd = async (data) => {
    try {
      const response = await axios.post(
        "http://192.168.1.20:8081/api/v1/questions/mcq",
        data
      );
      console.log("shobiiiiii--->",response.data);
    } catch (err) {
      console.log(err);
    }
  };

  const handleSave = () => {
    const questionText = questionInputValueRef.current.value;
    const imageUrls = imageUrlRef.current.map((url) => `${url}`).flat() || [];
    const selectedData = {
      category: document.querySelector('input[name="question-type"]:checked')
        .value,
      difficulty: document.querySelector('input[name="weightage"]:checked')
        .value,
      options: options.map((option, idx) => ({
        option: option.name,
        isCorrect: correctAnswer.includes(idx),
      })),
      question: questionText,
      imageUrl: imageUrls,
    };

    mcqadd(selectedData);

    console.log(selectedData);
    setOptions(defaultOptions);
    setCorrectAnswerIndex("");
    setCorrectAnswer("");
    setImagePreviews([]);
    questionInputValueRef.current.value = "";
    imageUrlRef.current = [];
    const categoryRadios = document.querySelectorAll(
      'input[name="question-type"]'
    );
    categoryRadios.forEach((radio) => {
      radio.checked = false;
    });

    const difficultyRadios = document.querySelectorAll(
      'input[name="weightage"]'
    );
    difficultyRadios.forEach((radio) => {
      radio.checked = false;
    });
  };

  useEffect(() => {
    console.log(correctOptionAnswer, correctAnswer);
  }, [correctOptionAnswer, correctAnswer]);

  return (
    <div>
      <Button
        text="Upload Bulk Data"
        className="bulkupload-button"
        onClick={() => setUploadpopup(true)}
      />
      <Popup
        trigger={uploadpopup}
        setTrigger={setUploadpopup}
        body={popupBody()}
      />
      <hr className="line" />
      <div className="mcq-heading">
        <h2>MCQ Type:</h2>
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
        <div className="image-upload">
          <input
            type="file"
            id="image-upload"
            style={{ display: "none" }}
            className="image-file"
            multiple
            onChange={handleImageUpload}
            accept=".jpg,.jpeg,.png"
          />
          <label htmlFor="image-upload" className="image-icon">
            <FiImage />
          </label>
          <textarea
            ref={questionInputValueRef}
            className="question-textarea"
            placeholder="Enter your question here..."
          />
        </div>
      </div>
      <div className="image-previews-container">
        <div className="image-preview">
          {imagePreviews.map((imageUrl, index) => (
            <React.Fragment key={index}>
              <img src={imageUrl} alt={`Image ${index + 1}`} />
              <FiTrash className="delete-icon" onClick={handleDeleteImage} />
            </React.Fragment>
          ))}
        </div>
      </div>
      <div className="button-type-setup">
        <h2>Button Type:</h2>
        <div className="dropdown-container">
          <select>
            {buttonTypeLabels.map((button, index) => (
              <option key={index} value={button.value}>
                {button.label}
              </option>
            ))}
          </select>
          <div className="add-option">
            <FiPlus className="plus-icon" onClick={handleAddOption} />
            <span>Add Option</span>
          </div>
          <div className="new-option">
            {options.map((option, index) => (
              <div className="optionDiv" key={index}>
                {option.type === "radio" ? (
                  <input
                    type="radio"
                    name="correct-answer"
                    checked={correctAnswerIndex === index}
                    onChange={() =>
                      handleSelectCorrectAnswer(index, option, "radio")
                    }
                  />
                ) : (
                  <input
                    type="checkbox"
                    checked={option.isChecked} 
                    onChange={() =>
                      handleSelectCorrectAnswer(index, "", "checkbox")
                    }
                  />
                )}
                <input
                  type="text"
                  value={option.name}
                  onChange={(e) => handleOptionChange(index, e.target.value)}
                  className="input-cont"
                />
                <FiTrash
                  className="trash-icon"
                  onClick={() => handleDeleteOption(index)}
                />
              </div>
            ))}
          </div>
        </div>
      </div>
      <div className="correct-answer-section">
        <h2>Correct Answer:</h2>
        <p>{correctAnswer}</p>
      </div>
      <Button text="Save" className="save-button" onClick={handleSave} />
    </div>
  );
};

export default McqQuestion;
