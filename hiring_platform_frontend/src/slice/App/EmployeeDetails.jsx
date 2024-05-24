import { createSlice } from "@reduxjs/toolkit";

export const userSlice = createSlice({
  name: "user",
  initialState: {
    email: "",
    id: "",
    fname: "",
    lname: "",
  },
  reducers: {
    setUser: (state, action) => {
      const data = action.payload;
      state.email = data.email;
      state.id = data.id;
      state.fname = data.fname;
      state.lname = data.lname;
    },
  },
});

export const { setUser } = userSlice.actions;

export const selectUser = (state) => state.user;

export default userSlice.reducer;
