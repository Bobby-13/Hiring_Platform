import React, { useEffect, useState } from "react";
import Table from "../../../components/Table";
import "./CompletedContestant.scss";
import { useParams } from "react-router-dom";
import axios from "axios";
import Popup from "../../../components/Popup";

const CompletedContestant = () => {
  const { contestId } = useParams();
  const [completedContestantData, setCompletedContestantData] = useState(" ");
  const [loading, setLoading] = useState(true);
  const [showPopup, setShowPopup] = useState(false);
  const [popupContent, setPopupContent] = useState({
    visibility: false,
    content: "",
  });
  const [dataCheck, setDataCheck] = useState("");

  useEffect(() => {
    const fetchCompletedContestantData = async () => {
      try {
        const response = await axios.get(
          ` http://192.168.1.20:8081/api/v1/employee/${sessionStorage.getItem("userId" )}/interviews/2hbygb2`, {
            headers:{
              'Authorization':`${sessionStorage.getItem("token")}`
            }
          }
        );

        const completedInterviews = response.data.object.filter(
          (interview) => interview.status === "SELECTED"
        );
        setDataCheck(completedInterviews);
        const extractedData = completedInterviews.map((contestant) => ({
          Name: contestant.userName,
          Email: contestant.userEmail,
          Schedule: new Date(contestant.interviewTime).toLocaleString(),
          Status: contestant.status,
        }));
        setCompletedContestantData(extractedData);
      } catch (error) {
        console.error("Error fetching completed interview data:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchCompletedContestantData();
  }, []); 

  const handlefeedbackClick = (index) => {
    setPopupContent(dataCheck[index].feedBack);
    setShowPopup(true);
  };

  return (
    <div className="tables-content">
      {loading ? (
        <p>Loading...</p>
      ) : (
        <>
          <Table
            data={completedContestantData}
            onFeedback={(index) => handlefeedbackClick(index)}
          />
          <Popup
            trigger={showPopup}
            setTrigger={setShowPopup}
            body={popupContent}
      
          />
        </>
      )}
    </div>
  );
};

export default CompletedContestant;
