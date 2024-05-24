import React, { useState, useEffect, useRef } from "react";
import "./Filter.scss";
import { FiFilter } from "react-icons/fi";
import Button from "../Button";
import axios from "axios";

const Filter = ({
  options,
  defaultOptions,
  className,
  pageNumber,
  pageSize,
  setFilteredData,
}) => {
  const [isFilterUp, setIsFilterUp] = useState(false);
  const [selectedOptions, setSelectedOptions] = useState([]);
  const filterRef = useRef(null);
  const [data, setData] = useState([]);
  const setupDefaultOptions = ["Easy", "Medium", "Hard"];
  defaultOptions = defaultOptions ? defaultOptions : setupDefaultOptions;
  useEffect(() => {
    const handleClickOutside = (event) => {
      if (filterRef.current && !filterRef.current.contains(event.target)) {
        setIsFilterUp(false);
      }
    };
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  const handleClick = () => {
    setIsFilterUp(!isFilterUp);
  };

  const handleCheckboxChange = (event) => {
    const { value } = event.target;
    if (selectedOptions.includes(value)) {
      setSelectedOptions(selectedOptions.filter((option) => option !== value));
    } else {
      setSelectedOptions([...selectedOptions, value]);
    }
  };

  const mcqFilter = async (finalData) => {
    try {
      console.log("shobi finalData", finalData);

      const response = await axios.get(
        `http://192.168.1.20:8081/api/v1/questions/mcq?page=${finalData[0].page}&size=${finalData[0].size}&type=${finalData[0].type}&difficulty=${finalData[0].difficulty}`,
        finalData
      );
      console.log("response?.data?.object?.content--->",response?.data?.object?.content);
      
      setFilteredData(response?.data?.object?.content);
    } catch (err) {
      console.log(err);
    }
  };
  const CodingFilter = async (finalData) => {
    try {
      console.log("shobi finalData", finalData);

      const response = await axios.get(
        `http://192.168.1.139:8082/api/v1/coding-question?page=${finalData[0].page}&size=${finalData[0].size}&type=${finalData[0].type}&difficulty=${finalData[0].difficulty}`,
        finalData
      );
      console.log("response?.data?.object?.content--->",response.data.object.QUESTIONS_FOR_THIS_PAGE
      );
      
      setFilteredData(response?.data?.object?.QUESTIONS_FOR_THIS_PAGE);
    } catch (err) {
      console.log("err",err);
    }
  };

  const handleApply = () => {
    let finalData;
    if (defaultOptions === setupDefaultOptions) {
      const difficultyOptions = selectedOptions.filter((option) =>
        defaultOptions.includes(option)
      );
      const categoryOptions = selectedOptions.filter(
        (option) => !defaultOptions.includes(option)
      );
  
      finalData = [
        {
          difficulty: difficultyOptions.map((option) => {
            switch (option) {
              case "Easy":
                return "EASY";
              case "Medium":
                return "MEDIUM";
              case "Hard":
                return "HARD";
              default:
                return null;
            }
          }),
          type: categoryOptions.map((option) => {
            switch (option) {
              case "Logical":
                return "LOGICAL_MCQ";
              case "Verbal":
                return "VERBAL_MCQ";
              case "Technical":
                return "TECHNICAL_MCQ";
              case "Aptitude":
                return "APTITUDE_MCQ";
              case "Data Structures":
                return "DATA_STRUCTURE_CODING";
              case "Algorithms":
                return "ALGORITHMS_CODING";
              case "Strings":
                return "STRINGS_CODING";
              case "Mathematics":
                return "MATHEMATICS_CODING";
              case "Patterns":
                return "PATTERNS_CODING";
              default:
                return null;
            }
          }),
          page: pageNumber,
          size: pageSize,
        },
      ];
    }
    
    const isCodingSelected = finalData[0].type.some(type => type.includes("_CODING"));
    
    if (isCodingSelected) {
      CodingFilter(finalData);
    } else {
      mcqFilter(finalData);
    }
  
    setIsFilterUp(false);
    console.log("data-->", finalData);
  };
  
  const handleClearAll = () => {
    setSelectedOptions([]);
  };

  return (
    <div
      className={className ? "Filter " + className : "Filter"}
      ref={filterRef}
    >
      <div className="Filtericon" onClick={handleClick}>
        <FiFilter />
      </div>
      {isFilterUp && (
        <div className="FilterPopup">
          <div className="buttonContainer">
            <Button
              className="clearButton"
              text="Clear All"
              onClick={handleClearAll}
            />
          </div>
          <div className="optionsGroup">
            {defaultOptions.map((option, index) => (
              <div key={index}>
                <input
                  type="checkbox"
                  id={option}
                  name={option}
                  value={option}
                  onChange={handleCheckboxChange}
                  checked={selectedOptions.includes(option)}
                />
                <label htmlFor={option}>{option}</label>
              </div>
            ))}
            {options && <hr />}
            {options &&
              options.map((option, index) => (
                <div key={index}>
                  <input
                    type="checkbox"
                    id={option}
                    name={option}
                    value={option}
                    onChange={handleCheckboxChange}
                    checked={selectedOptions.includes(option)}
                  />
                  <label htmlFor={option}>{option}</label>
                </div>
              ))}
          </div>

          <Button className="applyButton" text="Apply" onClick={handleApply} />
        </div>
      )}
    </div>
  );
};

export default Filter;
