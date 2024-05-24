import { createSlice } from '@reduxjs/toolkit';

const initialState = {
  filterType: '',
  count: 30,
  percentage: 1,
};

const countSlice = createSlice({
  name: 'countSlice',
  initialState,
  reducers: {
    updateFilter: (state, { payload }) => {
      state.filterType = payload;
    },
    decreasePercentage: (state) => {
      state.percentage--;
    },
    decreaseCount: (state) => {
      state.count--;
    },
    increasePercentage: (state) => {
      state.percentage++;
    },
    increaseCount: (state) => {
      state.count++;
    },
    placeCount: (state, { payload }) => {
      state.count = payload;
    },
    placePercentage: (state, { payload }) => {
      state.percentage = payload;
    },
  },
});

export const {
  updateFilter,
  decreasePercentage,
  decreaseCount,
  increaseCount,
  increasePercentage,
  placeCount,
  placePercentage,
} = countSlice.actions;

export default countSlice.reducer;
