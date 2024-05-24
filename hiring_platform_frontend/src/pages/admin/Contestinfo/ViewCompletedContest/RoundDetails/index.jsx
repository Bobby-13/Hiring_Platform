import React, { useEffect, useState } from 'react'
import ChartComponent from '../../../../../components/Piechart'
import "../RoundDetails/RoundDetails.scss"
import Button from '../../../../../components/Button';
const RoundDetails = ({ resultbutton, roundDetails }) => {
  if (!roundDetails || !roundDetails.partWiseMarkAllocations) {
    return null;
  }
  const [chartData, setChartData] = useState({})
  const [charttData, setCharttData] = useState({})
  // console.log(roundDetails)
  useEffect(() => {
    const labels = roundDetails?.partWiseMarkAllocations?.map((round) => round?.part);
    const allocatedTime = roundDetails?.partWiseMarkAllocations?.map((round) => round?.allocatedTime);
    const d = roundDetails?.partWiseMarkAllocations?.map((round) =>
      round.difficultyAndCountList.map((i) =>
        i.difficulty
      )
    )
    const c = roundDetails?.partWiseMarkAllocations?.map((round) =>
      round.difficultyAndCountList.map((i) =>
        i.count
      )
    )
    const sumArray = c.map((e) => (
      e.reduce((accumulator, currentValue) => {
        return accumulator + currentValue
      }, 0)
    ))

    console.log(sumArray)
    setCharttData({
      labels: [[labels[0], `${d[0]}-${c[0]}`], [labels[1], `${d[1]}-${c[1]}`], [labels[2], `${d[2]}-${c[2]}`], [labels[3], `${d[3]}-${c[3]}`]],
      datasets: [
        {
          data: sumArray,
          backgroundColor: ["#59ADFF", "#C5CAE9", "#0E5194", "#6885A1"],
          cutout: "70%",
        },
      ],
    });
    setChartData({
      labels: labels,
      datasets: [
        {
          data: allocatedTime,
          backgroundColor: ["#59ADFF", "#C5CAE9", "#0E5194", "#6885A1"],
          cutout: "70%",
        },
      ],
    });
  }, [roundDetails])
  return (
    <div>
      <div className='roundheadings'>
        <div className='viewresultbutton'>
          <h3>Round {roundDetails?.roundNumber} : {roundDetails?.roundType} | {roundDetails?.roundRange}</h3>
          <Button text={"View Result"} className={"resultbutton"} style={{ display: resultbutton ? "block" : "none" }}></Button>
        </div>
        <p>Pass Percentage : {roundDetails?.passPercentage}</p>
        <p>Participants Count : {roundDetails?.passCount}</p>
      </div>
      <div className='holechart'>
        <div className='pichartdata'>
          <ChartComponent chartData={charttData} className={"piechart"} question={roundDetails.totalNumberOfQuestions} />
        </div>
        <div className='pichartdata'>
          <ChartComponent chartData={chartData} className={"piechart"} question={roundDetails.totalAllocatedTime} time={true} />
        </div>
      </div>
    </div>
  )
}

export default RoundDetails
