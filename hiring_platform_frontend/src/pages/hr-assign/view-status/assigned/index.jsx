import React, { useEffect, useState } from 'react';
import Table from '../../../../components/Table';
import styles from './index.module.scss';
import { useLocation, useNavigate } from 'react-router';
import { useDispatch, useSelector } from 'react-redux';
import { generateScheduleAction, hrAssignedDetailedView } from '../../../../slice/hr-assign-slice/actions';
import ModalPopUp from '../../../../components/hr-components/modalPopUp';
import Button from '../../../../components/hr-components/button';
import { modifyDateAndTime } from '../../../../slice/hr-assign-slice';

const HRAssigned = () => {
  const data = useLocation();
  const dispatch = useDispatch();
  const [editable, setEditable] = useState(false);
  const [modal, setModal] = useState({ modal: false, data: {}, id: '' });
  const [details, setDetails] = useState([]);
  const [changed, setChanged] = useState([]);
  const value = useSelector((state) => state.hrAssignDetails.detailedView);
  const navigate = useNavigate();

  useEffect(() => {
    if (data.state?.id) {
      dispatch(hrAssignedDetailedView(data.state.id));
    } else {
      setEditable(data.state.editable);
    }
  }, [data.state]); //eslint-disable-line

  useEffect(() => {
    if (value && Array.isArray(value.data)) {
      if (value?.data?.length !== 0)
        setDetails(value.data)
    }
  }, [value.data])

  const onUpdation = (arr, id) => {
    let updatedDetails = [];
    setDetails(prevState => {
      updatedDetails = prevState.map(item => {
        if (item.interviewId === id) {

          return { ...item, interviewDate: arr[0], interviewTime: arr[1] }
        }
        return item;
      })
      return updatedDetails;
    })
    dispatch(modifyDateAndTime(updatedDetails));
    // setUpdate(prevState=>([...prevState,arr]));
    closePop();
    const idExist = changed.some(idhere => idhere === id);
    if (!idExist) {
      setChanged(prevState => ([...prevState, id]))
    }
  }

  useEffect(() => {

  })
  const modalPop = (val, index) => {
    setModal((prevState) => {
      return {
        modal: !prevState.modal,
        data: val,
        id: value.data[index].interviewId
      };
    });
  };

  const closePop = () => {
    setModal((prevState) => {
      return {
        modal: false,
        data: {},
        id: ''
      };
    });
  };

  const navFunction = () => {
    const update = []
    changed.forEach(id => {
      const filter=value.data.filter(item =>item.interviewId===id)
      update.push(filter[0]);
    })
    dispatch(generateScheduleAction({ id: data.state.data, options: update,action:"PUT" }))
    navigate('/admin/hrAssign/contestdetails', {
      state: { data: details, typeOfHR: data.state.typeOfHR, editable: false },
    });
  };

  return (
    <>
      {details.length !== 0 ?
        (<div className={styles.hrassigned_detailed_view}>
          <h1>{data.state.typeOfHR}</h1>
          <h3>Generated Schedule</h3>
          {editable && details ? (
            <>
              <Table
                data={/*details*/ value.data.map(({ interviewId, ...rest }) => rest)}
                isEditable={editable}
                onEdit={modalPop}
              />
              <div className={styles.button}>
                <Button
                  text={'Confirm Schedule'}
                  type={'success'}
                  className={styles.button}
                  onClickFunction={navFunction}
                />
              </div>
            </>
          ) : (
            <Table data={value.data.map(({ interviewId, ...rest }) => rest)} />
          )}
          {modal.modal && (
            <ModalPopUp
              data={modal.data}
              heading={'Update Date & Time'}
              crossAction={() => closePop}
              id={modal.id}
              onUpdation={onUpdation}
            />
          )}
        </div>) : (<div>Loading</div>)}
    </>
  );
};

export default HRAssigned;
