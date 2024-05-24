import React, { useEffect, useState } from "react";
import BgContainer from "../../../components/BgContainer/index";
import HRSidebar from "../../../components/HRSidebar/index";
import "./LogHome.scss";
import axios from "axios";
import { useSelector } from "react-redux";
import { useParams, useNavigate } from "react-router-dom";

const HRhome = () => { 
  const { id } = useParams(); 
  const [interviewData, setInterviewData] = useState([]);
  const userdata = useSelector((state) => state.user)
  const navigate = useNavigate(); 

  useEffect(() => {
    const fetchInterviewData = async () => {
      try {
        const response = await axios.get(
          `http://192.168.1.20:8081/api/v1/employee/contests/log/${sessionStorage.getItem("userId")}`,
          {
            headers:{
              'Authorization':`${sessionStorage.getItem("token")}`
            }
          }
        );
        response.data.object.forEach((contest) => {
          console.log(`${contest.contestId}`);
        });
        setInterviewData(response.data.object);
      } catch (error) {
        console.error("Error fetching interview data:", error);
      }
    };

    fetchInterviewData();
  }, [id]);

  const handleClickContest = (contestId, contestName) => {
    navigate(`/hr/contestLog/${contestId}`, { state: { interviewName: contestName } });
  };

  return (
    <div className="bg-container">
      <HRSidebar />
      <div className="container-content">
        {interviewData.map((interview) => ( 
          <BgContainer
            key={interview.contestId}
            heading={interview.name}
            path={`/hr/contestlog/${interview.contestId}`} 
            date={interview.startTime}
            onClick={() => handleClickContest(interview.contestId, interview.name)}
          >
            <p>Name: {interview.name}</p>
            <p>Interview Time: {new Date(interview.startTime).toLocaleString()}</p>
          </BgContainer>
        ))}
      </div>
    </div>
  );
};

export default HRhome;
