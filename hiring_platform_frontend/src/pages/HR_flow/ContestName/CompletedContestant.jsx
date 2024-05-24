import React, { useEffect, useState } from "react";
import Table from "../../../components/Table";
import "./CompletedContestant.scss";
import axios from "axios";
import { useParams } from "react-router-dom";

const CompletedContestant = () => {
  const { contestId } = useParams();
  const [completedContestantData, setCompletedContestantData] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchCompletedContestantData = async () => {
      try {
        const response = await axios.get(
          `http://192.168.1.20:8081/api/v1/employee/${sessionStorage.getItem("userId")}/interviews/${contestId}`,
          {
            headers:{
              'Authorization':`${sessionStorage.getItem("token")}`
            }
          }
        );
        if (response && response.data && Array.isArray(response.data.object)) {
          const completedInterviews = response.data.object.filter(
            (interview) => interview.status === "Completed"
          );
          const extractedData = completedInterviews.map((contestant) => ({
            Name: contestant.userName,
            Email: contestant.userEmail,
            Schedule: new Date(contestant.interviewTime).toLocaleString(),
            Status: contestant.status
          }));
          setCompletedContestantData(extractedData);
        } else {
          console.error("Invalid response or data structure:", response);
        }
      } catch (error) {
        console.error("Error fetching completed interview data:", error);
      } finally {
        setLoading(false); // Set loading to false regardless of success or failure
      }
    };
     
    fetchCompletedContestantData();
  }, []);

  return (
    <div className="tablecontent">
      {loading ? (
        <p>Loading...</p>
      ) : completedContestantData.length === 0 ? (
        <p>No data available</p>
      ) : (
        <Table data={completedContestantData} onFeedback={true} />
      )}
    </div>
  );
};

export default CompletedContestant;
