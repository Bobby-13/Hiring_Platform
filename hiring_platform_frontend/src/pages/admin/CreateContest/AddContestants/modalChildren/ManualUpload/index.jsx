import React, { useEffect, useState } from 'react';
import "./ManualUpload.scss";
import Button from "../../../../../../components/Button/index"
import CollegeAddConatiner from './CollegeaddContainer';

const ManualUpload = ({setMainContestantAdd}) => {
  const [addCounter, setAddCounter] = useState(1); 
  const [removelement,setRemovelement]=useState()
  const [constinfo,setConstinfo]=useState([])
  const handleAddClick = () => {
    setAddCounter(addCounter + 1);   
  };
    
  const contestinfo = (value) => {
    value.forEach((newValue) => {
      const existingIndex = constinfo.findIndex(item => item.name === newValue.name || item.email === newValue.email);
      if (existingIndex !== -1) {
        const updatedConstinfo = [...constinfo];
        updatedConstinfo[existingIndex] = { ...updatedConstinfo[existingIndex], ...newValue };
        setConstinfo(updatedConstinfo);
      } else {
        setConstinfo(prevConstinfo => [...prevConstinfo, newValue]);
      }
    });
  };
  const removeParticular=(remove)=>{
      setConstinfo(previous=>previous.filter(item=>item.email!==remove))
  }

   const removedetail=(value,collegename)=>{
    setAddCounter(addCounter-1)
    setRemovelement(value)
    setConstinfo(prevInfo => prevInfo.filter((item, index) => {
      return item.college !== collegename && index !== value; 
  }));
  }

  useEffect(()=>{
    setMainContestantAdd(constinfo)
  },[constinfo])
  
  console.log(constinfo,"info")
   
  return (
    <div className='manualuploadcontainer'>
      {[...Array(addCounter)].map((_, index) => (
         index !== removelement && (
          <CollegeAddConatiner 
              key={index} 
              className={"CollegeAddConatiner"} 
              addcontestdetail={contestinfo} 
              index={index} 
              removevalue={removedetail}
              removeParticular={removeParticular}
          />
      )
        ))}
        <Button text={"Add College"} className={"addbutton"} onClick={handleAddClick} />
    </div>
  );
};

export default ManualUpload;
