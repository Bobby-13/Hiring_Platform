import React, { useEffect, useState } from 'react'
import DynamicToggle from '../../../components/hr-components/dynamic-toggle'
import { Constestinfohome, CurrentContest, UpcomingContest, CompletedContest } from '../../../helpers/RoundList'

import "../Contestinfo/Contestinfo.scss"
import CompleteContest from './CompleteContest'
import { contestApi } from '../../../actions/API_actions'

const Contestinfo = () => {

  const [page, setPage] = useState("Completed Contest")
  const pagechange = (page) => {
    setPage(page)
  }
  const [upcomingData, setUpcomingData] = useState()
  const [currentData, setCurrentData] = useState()
  const [completedData, setCompletedData] = useState()

  useEffect(() => {
    const contestData = async () => {
      try {
        const res = await contestApi();
        // console.log(res)

        setUpcomingData(res.filter(contest => contest.contestStatus === 'UPCOMING')
          .map(({ contestId, startDate, name }, index) =>
            ({ No: index + 1, "Contest Name": name, "Start Date": startDate, contestId })))

        setCurrentData(res.filter(contest => contest.contestStatus === 'CURRENT')
          .map(({ contestId, startDate, name }, index) =>
            ({ No: index + 1, "Contest Name": name, "Start Date": startDate, contestId })))

        setCompletedData(res.filter(contest => contest.contestStatus === 'COMPLETED')
          .map(({ contestId, startDate, endDate, name }, index) =>
            ({ No: index + 1, "Contest Name": name, "Start Date": startDate, "End Date": endDate, contestId })))

      } catch (error) {
        console.error("Error in Contest api:", error);
      }
    };
    contestData();
  }, []);

  return (
    <div className='contestInfo'>
      <div className='togglebutton'>
        <DynamicToggle switchStates={Constestinfohome} page={page} handleToggle={pagechange} home={true} />
      </div>

      {page === "Completed Contest" ?
        //completed contest
        <CompleteContest RoutingLink={"completeContestView"} tableinfo={completedData} isview={true} searchtag={true} />
        ://curent contest
        page === "Current Contest" ? <CompleteContest tableinfo={currentData} isview={true} RoutingLink={"currentview"} searchtag={true} />
          :// upcoming contest
          <CompleteContest tableinfo={upcomingData} isEdit={true} isDelete={true} searchtag={true} />}
    </div>
  )
}

export default Contestinfo
