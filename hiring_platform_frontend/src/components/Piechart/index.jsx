import React, { useEffect, useRef } from "react";
import Chart from "chart.js/auto";

const ChartComponent = ({ question, chartData, type = "doughnut", className, time }) => {
  const chartRef = useRef(null);
  useEffect(() => {
    const ctx = chartRef.current.getContext("2d");
    const chartInstance = new Chart(ctx, {
      type: type,
      type: type,
      data: chartData,
      options: {
        plugins: {
          legend: {
            display: false,
          },
        },
      },
    });

    return () => {
      chartInstance.destroy();
    };
  }, [chartData]);

  return (
    <div className={"maindiv " + className}>
      {
        time ?
          <p className='totalcount'>Total Time : {question}</p> :
          <p className='totalcount'>Total Question : {question}</p>
      }
      <canvas ref={chartRef} />

    </div>
  );
};

export default ChartComponent;
