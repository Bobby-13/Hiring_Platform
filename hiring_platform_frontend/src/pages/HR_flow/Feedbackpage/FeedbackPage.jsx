import React, { useEffect, useState } from "react";
import HRfeedback from "../../../components/HRfeedback/HrFeedback";
import "./FeedbackPage.scss";
import axios from "axios";

const FeedbackPage = () => {
  const [interviewId, setInterviewId] = useState('6ced5829-3569-4221-a327-3efa36ddb47e');
  const [interviewData, setInterviewData] = useState(null);
  
  return (
    <div>
      <HRfeedback feedbackData={interviewData} />
    </div>
  );
};

export default FeedbackPage;
