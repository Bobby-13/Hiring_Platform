import { createAsyncThunk } from "@reduxjs/toolkit";
import { api } from "../../actions/apiConstants";

export const techHRDetails = createAsyncThunk(
  "hrassign/techHRDetails",
  async (requestType) => {
    try {
      const response = await api.get(
        `api/v1/contest/round/interview/live?request=${requestType}`
      );
      return { data: response.data.object, requestType: requestType };
    } catch (err) {
      return { data: "", requestType: requestType };
    }
  }
);

export const hrAssignedDetailedView = createAsyncThunk(
  "detailedView",
  async (id) => {
    try {
      let type = "";
      const response = await api.get(
        `/api/v1/contest/round/${id}/interview`
      );
      if (response)
        if (response.data.object.message === "Scheduled Interview lis") {
          type = "assigned"
        }
        else {
          type = "unassigned"
        }
      return { type, data: response.data };
    } catch (err) {
      console.log(err);
    }
  }
);

export const sendEmailRequest = createAsyncThunk('sendEmailRequest', async ({ employeeIds, id, percentage, dispatch }) => {
  try {
    const response = await api.post(`api/v1/contest/round/${id}/interview`, {
      ids: employeeIds
    }
    )
    dispatch(hrAssignedDetailedView((id)));
    return response;
  }
  catch (err) {
    console.log(err)
  }
})

export const generateScheduleAction = createAsyncThunk("generateSchedule", async ({ id, options, action }) => {
  try {
    if (action === 'PUT') {
      const response = await api.put(`/api/v1/contest/round/${id}/interview/schedule`, options);
      return { type: 'editable', data: response.data };
    }

    const response = await api.post(`/api/v1/contest/round/${id}/interview/schedule`, options);
    return { type: 'editable', data: response.data };
  } catch (err) {
    alert(err); // Handle errors if any
    throw err; // Rethrow the error to be handled by Redux Toolkit
  }
})