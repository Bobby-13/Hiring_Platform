import React, { useEffect, useState } from 'react'
import "../CompleteContest/CompleteContest.scss"
import Viewbarchart from '../PopupChartdetails'
import InputTag from '../../../../components/InputTag'
import Button from '../../../../components/Button'
import { FaSearch } from "react-icons/fa";
import { round3 } from '../../../../helpers/RoundList'
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faDownload } from '@fortawesome/free-solid-svg-icons'
import { xlContestApi } from '../../../../actions/API_actions'
import ContestinfoTable from '../../../../components/ContestinfoTable'

const CompleteContest = ({round,participant, roundDetails, tableinfo, isview, isDelete, isEdit, heading, className, RoutingLink, searchtag, contestIds }) => {
  const [clickdata, setClickdata] = useState()
  const [popupclick, setPopupclick] = useState(false);
  
  const [searchQuery, setSearchQuery] = useState("")
  const [filteredContests, setFilteredContests] = useState(tableinfo);
  
  const inputChange = (e) => {
    const searchTerm = e.target.value
    setSearchQuery(searchTerm)
    const filteredData = tableinfo?.filter(contest => {
      if(participant){
        return contest['Name']?.toLowerCase().includes(searchTerm.toLowerCase());
      }else if (round) {
        return contest['Contestant Name'].toLowerCase().includes(searchTerm.toLowerCase());
      }  else{
        return contest['Contest Name'].toLowerCase().includes(searchTerm.toLowerCase());
      }
    });
    setFilteredContests(filteredData);
  }
  useEffect(() => {
    setFilteredContests(tableinfo);
  }, [tableinfo]);

  const handleDownload = async () => {
    const res = await xlContestApi(contestIds)
    console.log(res)
  }
  const[finalResult,setFinalResult]=useState()

  return (
    <div className={className}>
      {
        heading === "Final Result" ?
          <div className='download-btn-wrapper'>
            <button onClick={handleDownload} className='download-btn'>
              <FontAwesomeIcon icon={faDownload} size='xl' ></FontAwesomeIcon></button>
          </div> :
          <>
            {round &&
              <h3 className='round-head'>Round {roundDetails?.roundNumber} : {roundDetails?.roundType} | {roundDetails?.roundRange}</h3>
            }
          </>
      }
      <div className='searchoption' style={{ display: searchtag ? "block" : "none", display: "flex" }}>
        <InputTag type={"text"} className={"searchinput"} placeholder="Enter Contest Name"
          name="search"
          value={searchQuery} onChange={inputChange} />
        <Button icon={<FaSearch />} className={"searchbutton"} />
      </div>
      <ContestinfoTable setFinalResult={setFinalResult} heading={heading} data={filteredContests} isViewbtn={heading === "Final Result" ? true : isview} isDeletable={isDelete} isEditable={isEdit} RoutingLink={RoutingLink} clickeddata={setClickdata} setPopupclick={setPopupclick} contestIds={contestIds}/>
      <Viewbarchart trigger={popupclick} settrigger={setPopupclick} finalResult={finalResult}/>
    </div>
  )
}

export default CompleteContest


