import React, { useEffect, useState } from "react";
import { hrAssignConstants } from "../../../constants/hrAssign";
import { hrComponents } from "../../../components/hr-components";
import { useDispatch, useSelector } from "react-redux";
import { techHRDetails } from "../../../slice/hr-assign-slice/actions";

//styles
import styles from "./index.module.scss";

const HrAssign = () => {
  const [page, setPage] = useState(
    hrAssignConstants.hrAssign ? hrAssignConstants.hrAssign[0] : ""
  );

  const dispatch = useDispatch();
  const handleToggle = (page) => {
    setPage(page);
  };

  const techHR = useSelector((state) => state.hrAssignDetails.HR.techHR);

  const personalHR = useSelector(
    (state) => state.hrAssignDetails.HR.personalHR
  );

  const rescheduleRequest = useSelector(
    (state) => state.hrAssignDetails.rescheduleRequest
  );

  useEffect(() => {
    dispatch(techHRDetails("INTERVIEW"));
    dispatch(techHRDetails("RESCHEDULE"));
  }, [dispatch]); //eslint-disable-line

  return (
    <div className={styles.hrassign}>
      <hrComponents.Toggle
        switchStates={hrAssignConstants.hrAssign}
        handleToggle={handleToggle}
        page={page}
      />
      {page === "Personal HR" ? (
        <hrComponents.Contest contests={personalHR} typeOfHR={"Personal HR"} />
      ) : page === "Technical HR" ? (
        <hrComponents.Contest contests={techHR} typeOfHR={"Technical HR"} />
      ) : (
        <hrComponents.Reschedule requests={rescheduleRequest} />
      )}
    </div>
  );
};

export default HrAssign;
