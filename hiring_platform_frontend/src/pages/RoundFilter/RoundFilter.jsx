import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import "./RoundFilter.scss";
import Button from "../../components/Button";
import axios from "axios";

const FilterContests = () => {
  const [round, setRound] = useState([]);
  const [roundId, setRoundId] = useState(""); 

  const roundFilter = async () => {
    try {
      const response = await axios.get(
        "http://192.168.1.20:8081/api/v1/contest/live-rounds",
        {
          headers:{
            'Authorization':`${sessionStorage.getItem("token")}`
          }
        }
      );
      setRound(response.data.object);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    roundFilter();
  }, []);

  const navigate = useNavigate();

  const handleRoundNavigate = (val) => {
    const { status, roundId } = val; 
    setRoundId(roundId); 
    if (status === "Pending") {
      navigate("/admin/roundFilterdetails", {
        state: { buttonText: status, roundId: roundId } 
      });
    } else {
      navigate("/admin/roundFilterdetails", {
        state: { buttonText: status, roundId: roundId }
      });
    }
  };

  return (
    <div>
      <div className="filtered-contests">
        <h2 className="Round-header">Round Filter</h2>
        {round.length === 0 ? (
          <p className="Nodata">No data is updated</p>
        ) : (
          round.map((val, index) => (
            <div className="filter-container" key={index}>
              <div className="contest">
                <p>
                  <strong>Contest Name:</strong> {val.contestName}
                </p>
                <p>
                  <strong>Contest Date:</strong> {val.roundCompletedTime}
                </p>
                <p>
                  <strong>Round No:</strong> {val.roundNumber}
                </p>
                <p
                  className={
                    val.status === "Published" ? "published" : "pending"
                  }
                >
                  <strong>Status:</strong> {val.status}
                </p>
              </div>
              {val.status === "Published" ? (
                <Button
                  text="View"
                  className="update-button"
                  onClick={() => handleRoundNavigate(val)}
                />
              ) : (
                <Button
                  text="Update"
                  className="update-button"
                  onClick={() => handleRoundNavigate(val)}
                />
              )}
            </div>
          ))
        )}
      </div>
    </div>
  );
};

export default FilterContests;
