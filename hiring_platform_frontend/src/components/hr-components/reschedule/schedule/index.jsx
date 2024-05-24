import React from 'react';
// import styles from '../../../../pages/hr-assign/reschedule/scheduling/index';
import styles from './index.module.scss';
import ContentBox from '../../text-match';
import { camelToSpacePascal } from '../../../../utils/splitHR';

const SchedulingDetail = ({ data, heading, typeInfo }) => {
  let keys;
  if (typeInfo !== 'reschedule') keys = Object.keys(data);
  return (
    <div className={styles.schedule_content}>
      <h3>{heading}</h3>
      <div className={styles.schedule_content_value}>
        {typeInfo === 'reschedule'
          ? data.map((item, index) => (
              <ContentBox
                key={index}
                title={item.key}
                inputBox={item.inputBox}
                direction={'row'}
              />
            ))
          : keys.map((key) => (
              <ContentBox
                key={key}
                title={camelToSpacePascal(key) + ':'}
                value={data[key]}
                direction={'row'}
              />
            ))}
      </div>
    </div>
  );
};

export default SchedulingDetail;
