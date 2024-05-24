import React, {useEffect, useState} from 'react';
import InputTag from '../../InputTag';
import '../PercentagetimeInfo/Percentageinfo.scss';
import { percentageinfo } from '../../../helpers/RoundList';
import { Placeholders } from '../../../helpers/RoundList';

export const Percentageinfo = ({ className,percentage,roundNumber }) => {

  const [basicinfo, setBasicinfo] = useState([]);

  const commonInfo = (value,item) =>{
    const parsedValue = value !== '' ? parseInt(value) : 0;
    const title = item.trim().replace(/\s+/g, '').replace('&', '');
    setBasicinfo(prevWeightages => ({
        ...prevWeightages,
        [title]:item==="Pass Percentage"?parsedValue:value,
        ["roundNumber"]:roundNumber,
    }));
} 
useEffect(()=>{
  percentage(basicinfo)
},[basicinfo])
  return (
    <div className={className}>
      {percentageinfo.map((f, index) => (
        <div className='percentageinfoConatiner' key={index}>
          <p style={{ width: '10rem' }}>{f}</p>
          <InputTag 
            type={index === 1 || index === 2 ? 'datetime-local' : 'number'} 
            placeholder={Placeholders[index]} 
            className='placeholder' 
            onChange={(e)=>{commonInfo(e.target.value,f)}}
          />
        </div>
      ))}
    </div>
  );
};
