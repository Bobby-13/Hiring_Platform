import React, { useEffect, useState } from 'react';
import Button from '../../../../components/Button';
import "./HRassign.scss";
import { RiDeleteBin5Line } from "react-icons/ri";
import RadioTag from '../../../../components/RadioButton';

const HRassign = ({totalrounds,Hrvalue}) => {
  const [containervalue, setContainervalue] = useState(() => {
    const storedValue = localStorage.getItem('containervalue');
    return storedValue ? JSON.parse(storedValue) : [];
  });
  useEffect(() => {
    localStorage.setItem('containervalue', JSON.stringify(containervalue));
  }, [containervalue]);

  const [radivalue, setRadiovalue] = useState("")
  const [length, setLength] = useState(totalrounds);
  const radioHeading=["TECHNICAL_INTERVIEW","PERSONAL_INTERVIEW"]

  const handleAddRound = (roundType) => {
    const newRound = { roundNumber: length + 1, roundType };
    setContainervalue([...containervalue, newRound]);
    setLength(length + 1)
  };

  const handleDeleteRound = (index) => {
    const newRounds = [...containervalue];
    newRounds.splice(index, 1);
    setContainervalue(newRounds);
    setLength(length - 1)
  };
 useEffect(()=>{
   Hrvalue(containervalue)
 },[containervalue])
  return (
    <div className='Hraddcontainer'>
      <h1 className='headings'>Add HR Round </h1>
      <div className='radiobuttons'>
        <Button text={"Add +"} className={"HRAddbutton"} onClick={() => handleAddRound(radivalue)} isDisabled={radivalue?false:true} style={{cursor:radivalue?"pointer":"not-allowed"}}/>
        {radioHeading.map(data=>(
          <RadioTag  name={"radio"} className={"inputdiv"} key={data} lable={data} onChange={() => { setRadiovalue(data) }} />
        ))}
      </div>
      {containervalue.map((round, index) => (
        <div className='RoundisplayContainer' key={index}>
          <div key={index} className='roundinfo'>
            <h2 style={{color:"#543cc7"}}>{`Round  ${round.roundNumber}`}</h2>
            <p>{round.roundType}</p>
          </div>
          <RiDeleteBin5Line className='dustbin' onClick={() => handleDeleteRound(index)} />
        </div>
      ))}
    </div>
  );
};

export default HRassign;
