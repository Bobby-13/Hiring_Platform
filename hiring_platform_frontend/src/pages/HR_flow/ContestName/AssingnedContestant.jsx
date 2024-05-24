import React, { useEffect, useState } from "react";
import Table from "../../../components/Table";
import "./AssingnedContestant.scss";
import axios from "axios";
import { useParams } from "react-router-dom";
import { useNavigate } from "react-router-dom";
import Popup from "../../../components/Popup";
import { assignedData } from "../../../helpers/ContestLog";
const AssingnedContestant = () => {
  const { contestId } = useParams();
  const [contestantData, setContestantData] = useState([]);
  const [loading, setLoading] = useState(true);
  const [responseValue, setResponseValue] = useState({});
  const navigate = useNavigate();
  const [popupContent, setPopupContent] = useState({
    visibility: false,
    content: "",
  });

  useEffect(() => {
    const fetchContestantData = async () => {
      try {
        const response = await axios.get(
          `http://192.168.1.20:8081/api/v1/employee/${sessionStorage.getItem(
            "userId"
          )}/interviews/${contestId}`, {
            headers:{
              'Authorization':`${sessionStorage.getItem("token")}`
            }
          }
        );
        setResponseValue(response);

        if (response && response.data && Array.isArray(response.data.object)) {
          const assignedInterviews = response.data.object.filter(
            (interview) => interview.status === "Assigned"
          );
          const extractedData = assignedInterviews.map((contestant) => ({
            Name: contestant.userName,
            Email: contestant.userEmail,
            Schedule: new Date(contestant.interviewTime).toLocaleString(),
          }));
          setContestantData(extractedData);
        } else {
          console.error("Invalid response or data structure:", response);
        }
      } catch (error) {
        console.error("Error fetching interview data:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchContestantData();
  }, []);

  const handleRescheduleClick = (index) => {
    navigate(
      `/hr/reschedulereq/${responseValue?.data?.object[index]?.interviewId}`
    );
  };
  const handlefeedbackClick = (index) => {

    navigate(
      `/hr/hrfeedback/${responseValue?.data?.object[index]?.interviewId}`,
      {
        state: {
          name: responseValue?.data?.object[index]?.userName,
          schedule: responseValue?.data?.object[index]?.interviewTime,
        },
      }
    );
  };
  const handleView = (index) => {
    const previousRoundResult =
      responseValue.data.object[index]?.prevoiusRoundResult;
    let previousRoundResultData;
    if (previousRoundResult !== undefined) {
      Object.keys(previousRoundResult).map((i) => {
        previousRoundResultData = i;
      });
      if (previousRoundResultData === "INTERVIEW") {
        const interviewFeedback = previousRoundResult.INTERVIEW;
        console.log(interviewFeedback);
        setPopupContent({ visibility: true, content: interviewFeedback });
      } else if (previousRoundResultData === "CODING") {
        const codingId = previousRoundResult.CODING;
        sessionStorage.setItem("codingId", codingId);
        navigate(`/hr/viewcode/${codingId}`);
      } else {
        console.error(
          "Invalid keys in PreviousRoundResult:",
          previousRoundResult
        );
      }
    } else {
      console.error("PreviousRoundResult is undefined or not available.");
    }
  };

  const handleStart = (index) => {
    const meetingLink = responseValue.data.object[index]?.meetingLink;
    if (meetingLink) {
      window.location.href = meetingLink;
    } else {
      console.error("Meeting link not available.");
    }
  };
  const handleResume = (index) => {
    const ResumeDownload = responseValue.data.object[index]?.resume;
    if (ResumeDownload) {
      const downloadLink = document.createElement('a');
      downloadLink.href = ResumeDownload;
      downloadLink.setAttribute('download', 'resume.pdf'); 
      document.body.appendChild(downloadLink);
      downloadLink.click();
      document.body.removeChild(downloadLink);
    } else {
      console.error("Resume not available.");
    }
  };
  

  return (
    <div className="tablescontent">
      {loading ? (
        <p>Loading...</p>
      ) : contestantData.length === 0 ? (
        <p>No assigned contestants available</p>
      ) : (
        <React.Fragment>
          <Table
            data={contestantData}
            onResume={(index) => handleResume(index)}
            onStart={(index) => handleStart(index)}
            onReschedule={(index) => handleRescheduleClick(index)}
            onView={(index) => handleView(index)}
            onFeedback={(index) => handlefeedbackClick(index)}
          />
          <Popup
            body={popupContent.content}
            trigger={popupContent.visibility}
            setTrigger={setPopupContent}
            heading="FEEDBACK"
          />
        </React.Fragment>
      )}
    </div>
  );
};

export default AssingnedContestant;
