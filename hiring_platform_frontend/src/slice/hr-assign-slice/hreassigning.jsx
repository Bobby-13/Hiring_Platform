import { createSlice } from "@reduxjs/toolkit";

const initialState = {
  request_sent: [],
  accepted: [],
  rejected: [],
  not_selected: [],
  selected: [],
  assigned: [],
};

const hrAssigning = createSlice({
  name: "assigningHR",
  initialState,
  reducers: {
    updateRequestSent: (state, { payload }) => {
      return {
        ...state,
        request_sent: payload,
      };
    },
    updateAccepted: (state, { payload }) => {
      return { ...state, accepted: payload };
    },
    updateRejected: (state, { payload }) => {
      return { ...state, rejected: payload };
    },
    updateNotSelected: (state, { payload }) => {
      return { ...state, not_selected: payload };
    },
    updateSelected: (state, { payload }) => {
      return { ...state, selected: payload };
    },
  },
});

export const {
  updateRequestSent,
  updateAccepted,
  updateRejected,
  updateNotSelected,
  updateSelected,
} = hrAssigning.actions;
export default hrAssigning.reducer;
