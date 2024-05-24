import React, { useState } from 'react';
import styles from './index.module.scss';
import ContentBox from '../text-match';
import { camelToSpacePascal } from '../../../utils/splitHR';
import { MdOutlineCancel } from 'react-icons/md';
import Button from '../button/index';

const ModalPopUp = ({ data, heading, crossAction,id,onUpdation}) => {
  const keys = Object.keys(data);
  const [date,setDate]=useState(data.interviewDate);
  const [time,setTime]=useState(data.interviewTime);
  return (
    <div className={styles.modal}>
      <div className={styles.modal_pop}>
        <MdOutlineCancel
          fontSize={27}
          className={styles.cross_icon}
          onClick={crossAction()}
        />
        <h2>{heading}</h2>
        <div className={styles.modal_pop_form}>
          {keys.map((key) =>
            key !== 'interviewDate' && key!=="interviewTime" ? (
              <ContentBox
                title={camelToSpacePascal(key) + ':'}
                direction={'row'}
                inputBox={{ value: data[key], disabled: true, type: 'text' }}
              />
            ) : key === 'interviewDate'?(<ContentBox
              title={key}
              inputBox={{ value: date, type: 'date',onChangeFunction:(date)=>setDate(date)}}
              direction={'row'}
            />):(
              <ContentBox
                title={key}
                inputBox={{ value: time, type: 'time',onChangeFunction:(time)=>setTime(time)}}
                direction={'row'}
              />
            )
          )}
        </div>
        <div className={styles.modal_pop_button}>
          <Button text={'cancel'} type={'danger'} />
          <Button text={'Update'} type={'success'} onClickFunction={()=>onUpdation([date,time],id)}/>
        </div>
      </div>
    </div>
  );
};

export default ModalPopUp;
