import React, { useEffect, useState } from 'react'
import Popup from '../../../../components/Popup'
import "../PopupChartdetails/Popupchart.scss"
import DynamicToggle from '../../../../components/hr-components/dynamic-toggle'
import ChartComponent from '../../../../components/Piechart'

const Viewbarchart = ({ trigger, settrigger, finalResult }) => {
  const roundNumbers = finalResult?.partWiseDto?.map((round) => round?.roundNum);
  roundNumbers?.unshift("All");

  const [pageschange, setPageschange] = useState("All");
  const [pagesindex, setpagesindex] = useState(0);
  const [roundDetails, setRoundDetails] = useState(finalResult?.roundWiseDto)

  const [chartDatabar, setChartDatabar] = useState({
    labels:["MCQ","CODING"],
    datasets: [
      {
        data: [],
        backgroundColor: ["rgb(211, 99, 132)", "#72CC52", "blue"],
        cutout: "70%",
      },
    ],
  });

  const [groupChart, setGroupChart] = useState({
    labels: [],
    datasets: [
      {
        label: 'Easy',
        data: [],
        backgroundColor: "green",
        cutout: "70%",
      },
      {
        label: 'Medium',
        data: [],
        backgroundColor: "orange",
        cutout: "70%",
      },
      {
        label: 'Hard',
        data: [],
        backgroundColor: "red",
        cutout: "70%",
      },
    ],
  });

  const pagechanges = (page, index) => {
    setPageschange(page);
    const pageNumber = page.replace(/\D/g, '');
    setpagesindex(pageNumber)
    if (page === "All") {
      setRoundDetails(finalResult?.roundWiseDto)
    } else {
      setRoundDetails(finalResult?.partWiseDto[index - 1]?.partWisePercentageDto)
    }
  };

  useEffect(() => {
    if (finalResult) {
      const roundData1 = finalResult?.roundWiseDto
        ?.filter((round) => round?.roundType === "MCQ" || round?.roundType === "CODING")
        ?.map((round) => round?.result);

      const roundLabels1 = finalResult?.roundWiseDto
        ?.filter((round) => round?.roundType === "MCQ" || round?.roundType === "CODING")
        ?.map((round) => round?.roundType);

      const roundLabels2 = roundDetails?.map((round) => round?.part);

      const easy = roundDetails?.filter((round) => round?.difficultyWisePercentage !== null)
        ?.map((round) => round?.difficultyWisePercentage?.EASY);

      const medium = roundDetails?.filter((round) => round?.difficultyWisePercentage !== null)
        ?.map((round) => round?.difficultyWisePercentage?.MEDIUM);

      const hard = roundDetails?.filter((round) => round?.difficultyWisePercentage !== null)
        ?.map((round) => round?.difficultyWisePercentage?.HARD);
// console.log("roundLabels1",roundLabels1)
      setChartDatabar((prevChartDatabar) => ({
        ...prevChartDatabar,
        labels: roundLabels1,
        datasets: [
          {
            ...prevChartDatabar.datasets[0],
            data: roundData1,
          },
        ],
      }));

      setGroupChart((prevChartDatabar) => ({
        ...prevChartDatabar,
        labels: roundLabels2,
        datasets: [
          {
            ...prevChartDatabar.datasets[0],
            label: 'Easy',
            data: easy,
            backgroundColor: "green",
            cutout: "70%",
          },
          {
            ...prevChartDatabar.datasets[0],
            label: 'Medium',
            data: medium,
            backgroundColor: "orange",
            cutout: "70%",
          },
          {
            ...prevChartDatabar.datasets[0],
            label: 'Hard',
            data: hard,
            backgroundColor: "red",
            cutout: "70%",
          },
        ],
      }));
    }
  }, [finalResult, roundDetails, pageschange]);
  // console.log(chartDatabar)
  const viewbody = () => {
    return (
      <>
        <div className='topstatus'>
          <div className='topemail'>
            <p>{finalResult?.name}</p>
            <p>{finalResult?.email}</p>
            <p>{finalResult?.college}</p>
          </div>
          {/* <div className='topemail'>
            <p>Total Percentage : 80%</p>
            <p>Status : <span style={{ color: "green" }}>Selected</span></p>
          </div> */}
        </div>
        <hr></hr>
        <div className='roundbutton'>
          <DynamicToggle
            switchStates={roundNumbers?.map((round) => round === "All" ? round : `Round ${round}`)}
            // switchStates={roundNumbers?.map((round) => roundNumbers?.length==round?roundTypes[round-1]:`Round ${round}`)}
            page={pageschange}
            handleToggle={pagechanges}
          />
        </div>
        {pageschange === "All" ?
          <>
            <div className='barchart'>
              <ChartComponent type='bar' chartData={chartDatabar} className={"pichart"} />
            </div>

            {roundDetails?.map((e) => (
              e?.roundType !== "MCQ" && e?.roundType !== "CODING" &&
              <p>Round {e?.roundNum} : ( {e?.roundType}) - {e.result}</p>
            ))}
          </>
          :
          <div className='barchart'>
            <ChartComponent type='bar' chartData={groupChart} className={"pichart"} />
          </div>
        }
      </>
    )
  }
  return (
    <div>
      <Popup trigger={trigger} setTrigger={settrigger} heading={"Name of participant"} body={viewbody()} />
    </div>
  )
}

export default Viewbarchart
