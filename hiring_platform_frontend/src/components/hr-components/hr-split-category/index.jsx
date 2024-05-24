import React, { useEffect, useState } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import Select from './select';
import styles from './index.module.scss';
import Button from '../button';
import Search from './search';
import StatusCard from './hr-status/index';
import { updateNotSelected, updateSelected } from '../../../slice/hr-assign-slice/hreassigning';
import { sendEmailRequest } from '../../../slice/hr-assign-slice/actions';

const HrSplit = ({ id, percentage }) => {
  const empData = useSelector((state) => state.assignHR);
  const [notSelected, setNotSelected] = useState([]);
  const [requestSent, setRequestSent] = useState([]);
  const [selected, setSelected] = useState([]);
  const dispatch = useDispatch();
  const handleAddToSelect = (data) => {
    const hasObjectWithKey = selected.some((employee) => employee?.id === data.id)
    if (!hasObjectWithKey) {
      setNotSelected(notSelected.filter(employee => employee.id !== data.id));
      setSelected(prevState => ([...prevState, {
        ...data,
        status: "SELECTED"
      }]))
    }
  }

  const handleToRemoveSelect = (data) => {
    const hasObjectWithKey = notSelected.some(employee => employee.id === data.id)
    if (!hasObjectWithKey) {
      setNotSelected(prevState => ([...prevState, {
        ...data,
        status: "Not assigned"
      }]))
      setSelected(selected.filter(select => select.id !== data.id))
    }
  }

  const handleClicks = {
    handleAddToSelect,
    handleToRemoveSelect,
  };
  const handleSendRequest = () => {
    const employeeIds = []
    selected.forEach(element => {
      employeeIds.push(element.id);
    });
    dispatch(sendEmailRequest({ employeeIds, id, percentage, dispatch }))
    setSelected([]);
  }
  useEffect(() => {
    setNotSelected(empData.not_selected);
    setRequestSent(empData.request_sent);
  }, [empData])
  useEffect(() => {
    dispatch(updateNotSelected(notSelected))
    dispatch(updateSelected(selected))
  }, [notSelected, requestSent,selected,dispatch])
  return (
    <div className={styles.hrsplit}>
      <div className={styles.hrsplit_assigned}>
        <h3>Assigned HR</h3>
        <div className={styles.hrsplit_assigned_cards}>
          {empData.accepted.map((item, ind) => {
            return (
              <StatusCard
                key={ind/*item.employeeId*/}
                data={item}
                handleClicks={handleClicks}
              />
            );
          })}
        </div>
      </div>
      <div className={styles.hrsplit_assigned}>
        <h3>Send Request</h3>
        <div className={styles.hrsplit_assigned_search}>
          <Select />
          <Search />
          <Button text="Send" onClickFunction={handleSendRequest} />
        </div>
        <div className={styles.hrsplit_assigned_cards}>
          {empData.request_sent.map((item, ind) => {
            return (
              <StatusCard
                key={ind/*item.employeeId*/}
                data={item}
                handleClicks={handleClicks}
              />
            );
          })}
          {empData.selected.map((item, ind) => {
            return (
              <StatusCard
                key={ind/*item.employeeId*/}
                data={item}
                handleClicks={handleClicks}
              />
            );
          })}
          {empData.not_selected.map((item,ind) => {
            return (
              <StatusCard
                key={ind/*item.employeeId*/}
                data={item}
                handleClicks={handleClicks}
              />
            );
          })}
        </div>
      </div>
    </div>
  )
}

export default HrSplit
