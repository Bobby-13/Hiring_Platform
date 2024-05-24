import React from "react";
import "./RoundLog.scss";
import Table from "../../components/Table";
import { MdOutlineEdit } from "react-icons/md";
import Button from "../../components/Button";
import { MdDeleteOutline } from "react-icons/md";

const RoundLog = ({array,index,handleDeletemcq}) => {
       console.log(array)
      //  const verbal = (array.parts[0][0]?.easy ?? "") + (array.parts[0][0]?.medium ?? "") + (array.parts[0][0]?.hard ?? "");
      //  const aptitude = (array.parts[0][1]?.easy ?? "") + (array.parts[0][1]?.medium ?? "") + (array.parts[0][1]?.hard ?? "");
      //  const logical = (array.parts[0][2]?.easy ?? "") + (array.parts[0][2]?.medium ?? "") + (array.parts[0][2]?.hard ?? "");
      //  const technical = (array.parts[0][3]?.easy ?? "") + (array.parts[0][3]?.medium ?? "") + (array.parts[0][3]?.hard ?? "");
       
      const sampleData = {
        roundNumber: index + 1,
        category: array.roundType,
        passPercentage: array.PassPercentage,
        startDate: array.startTime,
        endDate: array.endTime,
        questions: array.parts.map((item,index) => ({
          Category: item?.category.category,
          Easy: item?.easy,
          Medium: item?.medium,
          Hard: item?.hard,
          // QuestionCount:index===0?verbal:index===1?aptitude:index===2?logical:technical ,
          Duration:item?.assignedTime
        }))
      };
      

  const { roundNumber, category, passPercentage, startDate, endDate, questions } = sampleData;

  return (
    <div className="RoundLog">
      <h2> Round {roundNumber} </h2>
      <div className="Container">
        <h2 className="RoundHeading">{category}</h2>
        <div className="Basicinfo">
          <div className="Tablecontent">
            <Table data={questions} className="Table" isPopupUp={false} />
          <div className="Passpercentage">
            <p><span>Pass Percentage :</span> {passPercentage}</p>
            <p><span>Start Date and time : </span>{startDate}</p>
            <p><span>End Date and time :</span>{endDate}</p>
            </div>
          </div>
          <div className="Buttons">
            <Button text={"Edit"} icon={<MdOutlineEdit style={{color:"white"}} />} className={"editDeleteicon"} style={{background:"#3c65c3"}}></Button>
            <Button text={"Delete"} icon={<MdDeleteOutline style={{color:"white"}}/>} className={"editDeleteicon"} style={{background:"rgba(195,60,60,1)"}} onClick={()=>{handleDeletemcq(index)}}></Button>

          </div>
        </div>
      </div>
    </div>
  );
};

export default RoundLog;
