import { FaHome } from "react-icons/fa";
import { BsFillInfoSquareFill } from "react-icons/bs";
import { GrPlan } from "react-icons/gr";
import { IoPersonSharp } from "react-icons/io5";
import { MdSupervisorAccount } from "react-icons/md";
import { VscSettings } from "react-icons/vsc";
import { MdAddBox } from "react-icons/md";

export const SideBarNavigation = [
  {
    image: <FaHome />,
    text: "Home",
    path: "home",
  },
  {
    image: <GrPlan />,
    text: "Create Contest",
    path: "createContest",
  },
  {
    image: <VscSettings />,
    text: "Round Filter",
    path: "roundFilter",
  },
  {
    image: <IoPersonSharp />,
    text: "HR Assign",
    path: "hrAssign",
  },
  {
    image: <MdAddBox />,
    text: "Add Question",
    path: "addQuestion",
  },
  {
    image: <MdSupervisorAccount />,
    text: "Add Employee",
    path: "addEmployee",
  },
  {
    image: <BsFillInfoSquareFill />,
    text: "Contest Info",
    path: "contestinfo",// specify path to redirect
  },
];
