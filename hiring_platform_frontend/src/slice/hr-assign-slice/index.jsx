import { createSlice } from "@reduxjs/toolkit";
import { splitHR } from "../../utils/splitHR";
import { generateScheduleAction, hrAssignedDetailedView, techHRDetails } from "./actions";

const initialState = {
  HR: {
    techHR: [],
    personalHR: [],
  },
  rescheduleRequest: [],
  detailedView: { type: "", data: [] },
};

const HRAssignSlice = createSlice({
  name: "hrassign",
  initialState,
  reducers: {
    modifyDateAndTime: (state, { payload }) => {
      state.detailedView.data = payload;
    }
  },
  extraReducers: (builder) => {
    builder
      .addCase(techHRDetails.fulfilled, (state, { payload }) => {
        if (payload.requestType === "INTERVIEW") {
          const hr = splitHR(payload.data);
         return {
            ...state,
            HR: {
              techHR: hr.techHRData,
              personalHR: hr.personalHRData,
            },
          };
        } else {
          return {
            ...state,
            rescheduleRequest: payload.data,
          };
        }
      })
      .addCase(hrAssignedDetailedView.fulfilled, (state, { payload }) => {
        state.detailedView.type = payload.type;
        state.detailedView.data = payload.data.object;
      })

      .addCase(generateScheduleAction.fulfilled, (state, { payload }) => {
        state.detailedView.type = payload.type;
        state.detailedView.data = payload.data.object;
      })
  },
});

export const { modifyDateAndTime } = HRAssignSlice.actions
export default HRAssignSlice.reducer;