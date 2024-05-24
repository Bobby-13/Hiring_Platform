import React, { useState } from 'react';
import DynamicToggle from '../../../components/hr-components/dynamic-toggle';
import Scheduling from './scheduling';
import { useLocation } from 'react-router-dom';
import Button from '../../../components/hr-components/button';
import styles from './index.module.scss';

const RescheduleAccepted = () => {
  const data = useLocation().state.data;
  const [page, setPage] = useState('Reschedule');
  const dataSet = { data, typeofScheduling: page };
  const handleToggle = (page) => {
    setPage(page);
  };
  return (
    <div className={styles.rescheduling_page}>
      <DynamicToggle
        switchStates={['Reschedule', 'Reschedule Assign']}
        page={page}
        handleToggle={handleToggle}
      />
      <div className={styles.rescheduling_page_content}>
        <Scheduling dataSet={dataSet} />
      </div>
      <Button text={'Reschedule'} type={'success'} />
    </div>
  );
};

export default RescheduleAccepted;
