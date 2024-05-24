import { GiCrossMark } from "react-icons/gi";
import { TiTick } from "react-icons/ti";

const hrAssign = ["Technical HR", "Personal HR", "Reschedule Request"];

const buttonConstant = [
  { icon: <GiCrossMark fontSize={13} />, text: "Reject" },
  { icon: <TiTick fontSize={13} />, text: "Accept" },
];
export const headerDetailsContent = [
  {
    title: "Contest name:",
    value: true,
    direction: "row",
  },
  {
    title: "Start Date & Time:",
    value: false,
    inputBox: {
      type: "datetime-local",
      placeholder: "",
    },
    direction: "row",
  },
  {
    title: "Round No:",
    value: true,
    direction: "row",
  },
  {
    title: "Duration per HR:",
    value: false,
    inputBox: {
      type: "number",
      placeholder: "",
    },
    direction: "row",
  },
];
export const operators = ["-", "+"];
export const headingViewSchedule = [
  "Contest Details",
  "HR Details",
  "Reassigned Schedule Details",
];
export const hrAssignConstants = {
  hrAssign,
  buttonConstant,
  headerDetailsContent,
  operators,
  headingViewSchedule
};
