import React from "react";
import { useLocation } from "react-router";
import { hrAssignConstants } from "../../../../constants/hrAssign";
import SchedulingDetail from "../schedule";

const ViewSchedule = () => {
  const data = useLocation().state;
  return (
    <div>
      {hrAssignConstants.headingViewSchedule.map((heading) => (
        <SchedulingDetail
          key={heading}
          heading={heading}
          data={heading === "Contest Details" && data.data}
        />
      ))}
    </div>
  );
};

export default ViewSchedule;
