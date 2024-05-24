import { createAsyncThunk, createSlice } from '@reduxjs/toolkit';
import { api } from '../../actions/apiConstants';
import { splitHR } from '../../utils/splitHR';

export const techHRDetails = createAsyncThunk(
  'hrassign/techHRDetails',
  async (page) => {
    try {
      if (page === 'Technical HR' || page === 'Personal HR') {
        const response = await api.get(
          'api/v1/contest/round/interview/live?request=INTERVIEW'
        );
        const splitData = splitHR(response.data.object);
        console.log(splitData, 'de');
        return {
          data: {
            techHRValue: splitData.techHRData,
            personalHRValue: splitData.personalHRData,
          },
          page: page,
        };
      } else {
        const response = await api.get(
          'api/v1/contest/round/interview/live?request=RESCHEDULE'
        );
        console.log(response, 'de');
        return { data: response.data, page: page };
      }
    } catch (err) {
      console.log(err, 'r');
      return { data: '', page: page };
    }
  }
);

const initialState = {
  techHR: {},
  personalHR: {},
  rescheduleRequest: {},
};

const HRAssignSlice = createSlice({
  name: 'hrassign',
  initialState,
  reducers: () => {},
  extraReducers: (builder) => {
    builder.addCase(techHRDetails.fulfilled, (state, { payload }) => {
      console.log(payload, ' kj');
      if (payload.page === 'Technical HR') {
        return {
          ...state,
          techHR: payload.data.techHRValue,
        };
      } else if (payload.page === 'Personal HR') {
        return {
          ...state,
          personalHR: payload.data.personalHRValue,
        };
      } else {
        return {
          ...state,
          rescheduleRequest: payload.data,
        };
      }
    });
  },
});

export default HRAssignSlice.reducer;
