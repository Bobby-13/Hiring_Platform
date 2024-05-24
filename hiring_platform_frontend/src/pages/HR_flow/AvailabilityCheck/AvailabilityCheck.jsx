import React, { useState, useEffect } from "react";
// import done from "/home/divum/shobi/trail/hiring-platform-frontend/src/assets/doneicon.png";
import assets from "../../../assets";
import "./AvailabilityCheck.scss"
import axios from "axios";


const AvailabilityCheck = () => {
    const [token, setTokenData] = useState("");

    useEffect(() => {
        const url = new URLSearchParams(window.location.search);
        const tokenData = url.get("token");
        setTokenData(tokenData);
        handleReset()
      }, []);
    
      const handleReset = async () => {
        console.log("object")
        try {
          const response = await axios.post(
            `http://192.168.1.20:8081/api/v1/employee/response?token=${token}`,
            {},
            {
              headers: {
                Authorization: `${sessionStorage.getItem("token")}`,
              },
            }
          );
          console.log(response);
        } catch (error) {
          console.error("Error:", error);
        }
      };
  return (
    <div className="availabilty-container">
      <div>
        <img src={assets.doneIcon} className="image"></img>
      </div>
      Thank you for your response
    </div>
  );
};

export default AvailabilityCheck;
