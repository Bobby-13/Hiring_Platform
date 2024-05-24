import { createSlice } from "@reduxjs/toolkit";

export const incrementSlice = createSlice({
  name: "increment",
  initialState: {
    count: 0,
  },
  reducers: {
    increment: (state) => {
      state.count += 1;
    },
  },
});

export const { increment } = incrementSlice.actions;
export const selectCount = (state) => state.increment.count;

export default incrementSlice.reducer;
