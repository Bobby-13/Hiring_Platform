import React, { useEffect, useState } from "react";
import "./Graph.scss";
import {
  LineChart,
  ResponsiveContainer,
  Legend,
  Tooltip,
  Line,
  XAxis,
  YAxis,
  CartesianGrid,
} from "recharts";
import axios from "axios";

function Graph() {
  const [loading, setLoading] = useState(true);
  const [responseValue, setResponseValue] = useState({});
  const [graphData, setGraphData] = useState([]);

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
        const contestCountByYear = response.data.object.contestCountByYear;
        const data = Object.keys(contestCountByYear).map((year) => ({
          name: year,
          student: contestCountByYear[year],
        }));
        setGraphData(data);
      } catch (error) {
        console.error("Error fetching admin home data:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchAdminHomeData();
  }, []);

  return (
    <>
      <ResponsiveContainer width="100%" aspect={3}>
        <LineChart data={graphData} margin={{ right: 300 }}>
          <CartesianGrid />
          <XAxis dataKey="name" interval={"preserveStartEnd"} />
          <YAxis />
          <Legend />
          <Tooltip />
          <Line dataKey="student" stroke="black" activeDot={{ r: 8 }} />
        </LineChart>
      </ResponsiveContainer>
    </>
  );
}

export default Graph;
