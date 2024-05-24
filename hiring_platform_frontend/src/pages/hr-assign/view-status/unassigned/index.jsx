import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useLocation, useNavigate } from "react-router-dom";
import Table from "../../../../components/Table";
import Button from "../../../../components/hr-components/button/index";
import HeaderDetails from "../../../../components/hr-components/details-head";
import HrSplit from "../../../../components/hr-components/hr-split-category";
import { generateScheduleAction, hrAssignedDetailedView } from "../../../../slice/hr-assign-slice/actions";
import { splitByStatus } from "../../../../utils/hr-assign-utils/splitByStatus";
import { missing, sweetalert } from "../../../../utils/hr-assign-utils/sweetAlert";
import styles from "./index.module.scss";

const HRUnAssigned = () => {
  const data = useLocation().state;
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const headData = useSelector(
    (state) => state.hrAssignDetails.detailedView.data
  );
  const [tableData, setTableData] = useState(headData.contestants);
  const count_percentage = useSelector((state) => state.count_percentage);
  const [date, setDate] = useState();
  const [duration, setDuration] = useState();
  let selected = useSelector((state) => state.assignHR.accepted)

  const nav = () => {
    const employeeIds = []
    selected.forEach(element => {
      employeeIds.push(element.id);
    });

    dispatch(generateScheduleAction({
      id: data.id, options: {
        "startTime": date,
        duration,
        employeeId: employeeIds
      }, dispatch
    }))
    navigate("/admin/hrAssign/contestdetails", {
      state: {
        data: data.id,
        typeOfHR: data.typeOfHR,
        editable: true,
      },
    });

  };

  const generateSchedule = (actionType) => {
    if (date && duration) {
      let text;
      if (actionType === "generate") {
        //eslint-disable-line
        text = "Generated";
      } else {
        text = "Canceled";
      }
      sweetalert(text, actionType, nav);
    }
    else {
      missing()
    }
  };

  const handleSet = {
    dateVal: date,
    durationVal: duration,
    setDate,
    setDuration,
  };
  useEffect(() => {
    dispatch(hrAssignedDetailedView(data.id));
  }, [dispatch,data.id]);

  useEffect(() => {
    if (headData && Object.keys(headData).length > 0) {
      if (count_percentage.filterType === "percentage") {
        setTableData(
          headData.contestants.filter(
            (contestant) => contestant.score >= count_percentage.percentage
          )
        );
      } else if (count_percentage.filterType === "count") {
        setTableData(headData.contestants.slice(0, count_percentage.count));
      } else {
        setTableData(headData.contestants);
      }
    }
  }, [data.id, count_percentage, date, headData, duration, dispatch]);

  useEffect(() => {
    splitByStatus(headData.employees, dispatch);
  }, [headData]); // eslint-disable-line
  return (
    <>
      {
        Object.keys(headData).length!==0 ?

          <div className={styles.unassigned}>
            <h1>{data.typeOfHR}</h1>
            <div className={styles.unassigned_head}>
              <HeaderDetails
                contestName={headData.contestName}
                roundNo={headData.roundsNumber}
                type={data.typeOfHR}
                handleSet={handleSet}
              />
            </div>
            <div className={styles.unassigned_body}>
              <div className={styles.unassigned_body_table}>
                {tableData && tableData.length > 0 ? (
                  <Table data={tableData} />
                ) : (
                  <h3>Sorry Love no data to display!</h3>
                )}
              </div>
              <div className={styles.unassigned_body_hrdetails}>
                <HrSplit id={data.id} percentage={count_percentage.percentage} />
              </div>
            </div>
            <div className={styles.unassigned_generate_button}>
              <Button
                text={"Cancel Round"}
                type="danger"
                onClickFunction={() => generateSchedule("cancel round")}
              />
              <Button
                text={"Generate Schedule"}
                type="success"
                onClickFunction={() => generateSchedule("generate")}
              />
            </div>
          </div>
          : <div>Loading</div>
      }
    </>
  );
};

export default HRUnAssigned;
