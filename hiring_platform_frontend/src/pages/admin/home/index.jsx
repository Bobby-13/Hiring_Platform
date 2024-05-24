import React, { useEffect, useState } from "react";
import "./Home.scss";
import NotificationSlideBar from "../../../components/NotificationSlider/NotificationSlideBar";
import Graph from "../../../components/Graph";
import ChartComponent from "../../../components/Piechart/index"; 
import axios from "axios";

const Home = () => {
  const [responseValue, setResponseValue] = useState({});
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchAdminHomeData = async () => {
      try {
        const response = await axios.get(
          `http://192.168.1.20:8081/api/v1/contest/admin/home`,
          {
            headers:{
              'Authorization':`${sessionStorage.getItem("token")}`
            }
          }
        );
        setResponseValue(response.data.object);
      } catch (error) {
        console.error("Error fetching admin home data:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchAdminHomeData();
  }, []);

  const formattedChartData = {
    labels: ["HR Assigned Contest", "HR Not Assigned Contest"],
    datasets: [
      {
        data: [
          responseValue.assignedHR?.ASSIGNED || 0,
          responseValue.assignedHR?.NOT_ASSIGNED || 0
        ],
        backgroundColor: ["rgb(211, 99, 132)", "#72CC52"],
        cutout: "0%",
      },
    ],
  };

  return (
    <div className="adminHome">
      <div>
        <NotificationSlideBar className={"notificationSidebar"} />
      </div>
      <div className="container-row">
        <div className="completecontest-container" style={{ background: "#F7DFFE" }}>
          <p className="value">{responseValue.contestCount?.COMPLETED}</p>
          <p className="title">Completed Contest</p>
        </div>
        <div className="completecontest-container" style={{ background: "#FFF2C5" }}>
          <p className="value">{responseValue.contestCount?.CURRENT}</p>
          <p className="title">Current Contest</p>
        </div>
        <div className="completecontest-container" style={{ background: "#CEFFD5" }}>
          <p className="value">{responseValue.contestCount?.UPCOMING}</p>
          <p className="title">Upcoming Contest</p>
        </div>
      </div>
      <div className="Picto">
        <h1>Yearly Contest Conducted Chart</h1>
        <div className="graph-chart">
          <Graph data={responseValue.contestCountByYear} />
        </div>
        <div>
          <ChartComponent 
            chartData={formattedChartData}
            type="doughnut" 
            className={"pie-chart"}
          />
          <div className="color-descriptions">
            <div className="color-description">
              <div className="color-box" style={{ backgroundColor: "rgb(211, 99, 132)" }}></div>
              <p>HR Assigned Contest: {responseValue.assignedHR?.ASSIGNED}</p>
            </div>
            <div className="color-description">
              <div className="color-box" style={{ backgroundColor: "#72CC52" }}></div>
              <p>HR Not Assigned Contest: {responseValue.assignedHR?.NOT_ASSIGNED}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Home;
