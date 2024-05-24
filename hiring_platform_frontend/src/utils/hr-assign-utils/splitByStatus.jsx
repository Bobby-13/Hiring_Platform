import {
  updateRequestSent,
  updateAccepted,
  updateRejected,
  updateNotSelected,
  updateSelected,
} from "../../slice/hr-assign-slice/hreassigning";
import { useEffect } from "react";

export const splitByStatus = (employees, dispatch) => {
  if (employees) {
    dispatch(
      updateRequestSent(
        employees.filter((employee) => employee.status === "PENDING")
      )
    );
    dispatch(
      updateAccepted(
        employees.filter((employee) => employee.status === "AVAILABLE")
      )
    );
    dispatch(
      updateNotSelected(
        employees.filter((employee) => employee.status === "Not assigned")
      )
    );
    dispatch(
      updateRejected(
        employees.filter((employee) => employee.status === "NOT_AVAILABLE")
      )
    );
  }
};
{
  /*
Not assigned,
not available,
availabe,
pending
*/
}
