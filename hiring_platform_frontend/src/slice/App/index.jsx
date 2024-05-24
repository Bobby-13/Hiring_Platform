import { configureStore } from '@reduxjs/toolkit';
import incrementReducer from './Incrementslice';
import hrAssignReducer from '../hr-assign-slice/index';
import hrAssigningData from '../hr-assign-slice/hreassigning';
import count_percentage from '../hr-assign-slice/count_percentage';

export const store = configureStore({
  reducer: {
    increment: incrementReducer,
    hrAssignDetails: hrAssignReducer,
    assignHR: hrAssigningData,
    count_percentage,
  },
});
