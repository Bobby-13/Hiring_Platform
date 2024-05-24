import { useNavigate } from "react-router-dom";
import {
  increaseCount,
  increasePercentage,
  decreaseCount,
  decreasePercentage,
  placeCount,
  placePercentage,
  updateFilter,
} from "../../slice/hr-assign-slice/count_percentage";

//handleclick in contentDetails
export const useHandleClick = () => {
  const navigate = useNavigate();
  const handleClick = (contest, typeOfHR) => {
    contest.status === "Assigned"
      ? navigate("/admin/hrAssign/contestdetails", {
          state: { id: contest.roundId, typeOfHR, editable: false },
        })
      : navigate("/admin/hrAssign/contesthrset", {
          state: { id: contest.roundId, typeOfHR },
        });
  };
  return handleClick;
};

//setting value to store when clicking apply button
export const setValueToStore = (count_percentage, value, dispatch) => {
  if (count_percentage.filterType === "count") {
    if (value <= 100 && value >= 0) dispatch(placeCount(value));
  } else if (count_percentage.filterType === "percentage") {
    if (value <= 100 && value >= 0) dispatch(placePercentage(value));
  }
};

//setting the filter type to change state in page
export const filterSetFunction = (type, dispatch) => {
  dispatch(updateFilter(type.toLowerCase()));
};

//according to the props passed adding or subracting values
export const handleAddSub = (action, count_percentage, value, dispatch) => {
  if (count_percentage.filterType === "count") {
    if (action === "add") {
      if (value <= 99) {
        dispatch(increaseCount());
      }
    } else {
      if (value > 0) dispatch(decreaseCount());
    }
  } else {
    if (action === "add") {
      if (value <= 99) {
        dispatch(increasePercentage());
      }
    } else {
      if (value > 1) dispatch(decreasePercentage());
    }
  }
};
