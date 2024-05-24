import React, { useState } from "react";
import { MdArrowForwardIos } from "react-icons/md";
import { MdArrowBackIosNew } from "react-icons/md";
import AddMCQandCoding from "./AddMCQCoding";
import AddContestants from "./AddContestants";
import HRassign from "./HRassign";
import Progressbar from "../../../components/ProgressBar";
import Button from "../../../components/Button";
import "./CreateContest.scss";
import Preview from "./Previewpage";
import axios from "axios";

const CreateContest = () => {
  const [progress, setProgress] = useState(-1);
  const [select, setSelect] = useState(0);
  const [totalrounds, setTotalrounds] = useState();
  const [MainContestinfo, setMainContestinfo] = useState([]);
  const [MainContestantAdd,setMainContestantAdd]=useState()
  const arrayvalue = JSON.parse(localStorage.getItem("array"));
  const [contesid,setContestid]=useState()
  const CreateContestApi=async()=>{
    try{
      const response=await axios.post("http://192.168.1.241:8081/api/v1/contest",MainContestinfo)
      console.log(response.data.object)
      // setContestid(response.data.object)
    }
    catch(err){
      console.log(err)

    }
  }

  const AddContestantAPI=async()=>{
    try{
      const contestId = 'c58cdf04-0625-4436-934d-fe664f8b5af4'
      const response=await axios.post(`http://192.168.1.241:8081/api/v1/contest/${contestId}/users`,MainContestantAdd)
      console.log(response.data)
    }
    catch(err){
      console.log(err)

    }
  }

  const handleNext = () => {
    setProgress(progress + 1);
    setSelect(select + 1);
  };

  if(select===2){
    CreateContestApi()
    // console.log(MainContestinfo)
    }
    if(select===4){
      AddContestantAPI()
      console.log(MainContestantAdd,"contestant")
      console.log(select)
      }
  

    const handleBack = () => {
    setProgress(progress - 1);
    setSelect(select - 1);
  };

  const Hrvalue = (value) => {
    const updatedArray = [...arrayvalue, ...value].map(item => ({
      ...item
  }));

  setMainContestinfo({name:"agilcontest",rounds:updatedArray});
  };
  return (
    <div style={{ width: "100%" }} className="createContainer">
      <h1
        style={{
          background: "rgb(245, 243, 243)",
          paddingLeft: "2rem",
          paddingTop: "1rem",
        }}
      >
        Create Contest
      </h1>
      <div className="headingProgress">
        <Progressbar
          className={"progressInCreateContest"}
          actives={progress}
          select={select}
        />
      </div>
      {progress === -1 ? (
        <AddMCQandCoding setTotalrounds={setTotalrounds}/>
      ) : progress === 0 ? (
        <HRassign totalrounds={totalrounds}Hrvalue={Hrvalue}/>
      ) : progress===1?(
        <Preview/>):
          (
            <AddContestants setMainContestantAdd={setMainContestantAdd}/>
          )
      }
      <div className="nextbackbuttons">
        {progress > -1 && (
          <Button
            icon={<MdArrowBackIosNew />}
            text={"Back"}
            className={"navigationButton"}
            onClick={() => {
              handleBack();
            }}
          />
        )}
        <Button
          icon={select>2?"Submit":"Next"}
          text={<MdArrowForwardIos />}
          className={"navigationButton"}
          onClick={() => {
            handleNext();
          }}
        />
      </div>
    </div>
  );
};

export default CreateContest;
