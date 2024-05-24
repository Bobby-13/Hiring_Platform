import React from 'react';
import { hrAssignConstants } from '../../../../constants/hrAssign';
import styles from './index.module.scss';
import SchedulingDetail from '../../../../components/hr-components/reschedule/schedule';
import MultiValueTable from '../../../../components/hr-components/multiTable';
import HrSplit from '../../../../components/hr-components/hr-split-category';

const Scheduling = ({ dataSet }) => {
  const data1 = hrAssignConstants.personalHR.map(
    ({ roundId, status, ...rest }) => rest
  );
  return (
    <div className={styles.Rescheduling_schedule}>
      <div className={styles.Rescheduling_schedule_division}>
        <SchedulingDetail data={data1[0]} heading={'Contest Details'} />
        <SchedulingDetail
          data={hrAssignConstants.reschedule}
          heading={'Reschedule'}
          typeInfo={'reschedule'}
        />
      </div>
      {dataSet.typeofScheduling === 'Reschedule' ? (
        <div className={styles.Rescheduling_schedule_division}>
          <SchedulingDetail
            data={hrAssignConstants.hrDetails}
            heading={'HR Details'}
          />
          <div>
            <h3>HR Schedule List</h3>
            <MultiValueTable data={data1} />
          </div>
        </div>
      ) : (
        <div className={styles.Rescheduling_schedule_assigning}>
          <HrSplit />
        </div>
      )}
    </div>
  );
};

export default Scheduling;
