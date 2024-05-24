import React,{useEffect, useState} from 'react'
import "./AddMCQCoding.scss"
import InputTag from '../../../../components/InputTag'
import Button from '../../../../components/Button'

import Addpopupmcq from '../../../CreateContest-2'
import Popup from '../../../../components/Popup'
import RoundLog from '../../../RoundLog'

const AddMCQandCoding = ({setTotalrounds,Hrinfo}) => {
    const [popup,setPopup]=useState(false)
     const [array, setarray] = useState(() => {
    const storedArray = localStorage.getItem('array');
    return storedArray ? JSON.parse(storedArray) : [];
  });
  
  const handleDeletemcq = (index) => {
    const newRounds = [...array];
    newRounds.splice(index, 1);
    setarray(newRounds);
  };
  
  useEffect(() => {
    setTotalrounds(array.length)
    localStorage.setItem('array', JSON.stringify(array));
  }, [array]);
    const mcqCoding = () => {
        return <Addpopupmcq setPopup={setPopup} arrays={setarray} arraylength={array.length}/>;
      };

      
    
    return (
        <div>
            <div className='ContestnameEnter'>
                <h1 className='heading'>Basic Information</h1>
                <InputTag type={"text"} label={"Contest Name"} className={"contestinfo"} placeholder={"Enter contest Name"} />
            </div>
            <div className='ContestnameEnter'>
                <h2>Add Rounds</h2>
                <Button text={"Add +"} className={"addbuttonContestCreate"} onClick={()=>{setPopup(true)}} ></Button>
                {/* <img src={assets.noResults} alt="no-round" className='notfountimg' /> */}
               {array?.map((f,index)=>(
                   <RoundLog array={f} key={f}  index={index} handleDeletemcq={handleDeletemcq}/>
               ))}
               {console.log(array,'array')}
               
                <Popup trigger={popup} setTrigger={setPopup} body={mcqCoding()} />
            </div>

        </div>
    )
}

export default AddMCQandCoding
