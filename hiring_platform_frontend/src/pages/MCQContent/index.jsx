import React, { useEffect, useState } from "react";
import "../MCQContent/MCQContent.scss";
import { Rounds1, Weightage } from "../../helpers/RoundList";
import InputTag from "../../components/InputTag";
import WeightageCard from "../../components/WeightageCard";
import { Percentageinfo } from "../../components/WeightageCard/PercentagetimeInfo";
import Button from "../../components/Button";
import axios from "axios";

const MCQContent = ({setTotalCountDisplay, totalCountDisplay,setCategoryTitle,contestInfo,getinfovalue,setPopup}) => {
  const [active, setActive] = useState({});
  const [mcqround, setMcqround] = useState([]);
  const [percentages,setPercentages]=useState()

  const toggleActive = (type, checked) => {
    setActive((prevState) => ({
      ...prevState,
      [type]: checked,
    }));
  };
  const [CheckboxInfo,setCheckboxInfo]=useState()

  useEffect(() => {
    const fetchDatamcq = async () => {
      try {
            const response = await axios.get(`http://localhost:8081/api/v1/contest/category?type=${"MCQ"}`);
            setCheckboxInfo(response.data.object);
            const headings = response.data.object.map(item => item.heading);
              const codeActiveUpdate = {};
              headings.forEach(heading => {
                codeActiveUpdate[heading] = false;
              });
              setActive(prevState => ({
                ...prevState,
                ...codeActiveUpdate
              }));
        } catch (error) {
            console.error("Error fetching data:", error);
        }
    };

    fetchDatamcq();
}, []);


  const handleCheckboxChange = (event) => {
    const { checked, value } = event.target;
    toggleActive(value, checked);
  };
  const addMcqRoundData = (roundType, partData, assignedTime, sumOfWeightages, title) => {
    setTotalCountDisplay(sumOfWeightages);
    setCategoryTitle(title)
    setMcqround(previous => {
        const newArray = [...previous];
        newArray[partData?.category?.categoryId] = partData;
        return newArray;
    });
};
  const percentage =(percentagevalue)=>{
    setPercentages(percentagevalue);
  }

  const updatevalue=()=>{
    getinfovalue(mcqround,"MCQ",percentages)
    setPopup(false)
  }
  // console.log(mcqround)
  return (
    <div>
      <div className="CodingContent">
        {CheckboxInfo?.map((item, index) => (
          <InputTag
            type="checkbox"
            Process={item.heading}
            key={index}
            value={item.heading}
            onChange={handleCheckboxChange}
            checkboxHeading={item.heading}
          />
        ))}
      </div>
      <div style={{ display: "flex" }}>
        {Object.entries(active).map(([key, value]) => (
          <WeightageCard
            key={key}
            active={value}
            title={key}
            add={addMcqRoundData}
            roundNumber={1}
            categoryId={CheckboxInfo}
          />
        ))}
      </div>
      <Percentageinfo percentage={percentage}/>
      <Button
          text={"Submit"}
          className={"CreateContestSubmitbutton"}
          onClick={()=>{updatevalue()}}
        />
    </div>
  );
};

export default MCQContent;
