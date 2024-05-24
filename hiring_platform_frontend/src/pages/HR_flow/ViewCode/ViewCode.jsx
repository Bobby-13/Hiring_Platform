import React , { useEffect, useState }  from 'react'
import HRSidebar from '../../../components/HRSidebar/index';
import "./ViewCode.scss"
import axios from "axios";
import QuestionSet from '../../../components/Questionset/QuestionSet';
const ViewCode = () => {

  return (
    <div className='bg-container'>
      <HRSidebar/>
      <div className='questionSet-container'>
        <QuestionSet/>
      </div>
    </div>
  )
}

export default ViewCode
