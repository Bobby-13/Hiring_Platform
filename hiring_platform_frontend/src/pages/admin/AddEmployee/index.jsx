import React, { useState, useEffect } from "react";
import SearchBox from "../../../components/SearchBox";
import Button from "../../../components/Button";
import Table from "../../../components/Table";
import Filter from "../../../components/Filter";
import Switching from "../../../components/Switching/Switching";
import Popup from "../../../components/Popup";
import { ToastContainer, toast } from "react-toastify";
import axios from "axios";
import {
  employeeTypes,
  initialData as employeeTypeOptions,
} from "../../../helpers/addEmployeeHelper";
import "./addEmployee.scss";

const AddEmployee = () => {
  const [data, setData] = useState([]);
  const [selectedEmployee, setSelectedEmployee] = useState(null);
  const [isEditPopupOpen, setEditPopupOpen] = useState(false);
  const [empId, setEmpId] = useState("");
  const [firstName, setFirstName] = useState("");
  const [lastName, setLastName] = useState("");
  const [emailId, setEmailId] = useState("");
  const [password, setpassword] = useState("");
  const [type, setType] = useState("Employee");
  const [techStack, setTechStack] = useState("");
  const [yearOfExp, setYearOfExp] = useState("");
  const [employeeType, setEmployeeType] = useState("Personal HR");

  const handleEdit = (employee) => {
    setSelectedEmployee(employee);
    setEmpId(employee.employeeId);
    setFirstName(employee.firstName);
    setLastName(employee.lastName);
    setEmailId(employee.email);
    setType(employee.role);
    setTechStack(employee.stack);
    setYearOfExp(employee.yearsOfExperience);
    setEmployeeType(employee.employeeType);
    setEditPopupOpen(true);
  };

  const handleDelete = async (employee) => {
    try {
      await axios.delete(`http://192.168.1.20:8081api/v1/employee/${employee.employeeId}`,
      {
        headers:{
          'Authorization':`${sessionStorage.getItem("token")}`
        }
      });
      const updatedData = data.filter((emp) => emp.employeeId !== employee.employeeId);
      setData(updatedData);
      console.log(updatedData);
      toast.success("Employee deleted successfully.");
    } catch (error) {
      console.error("Error deleting employee:", error);
      toast.error("Error deleting employee. Please try again.");
    }
  };

  const handleAddEmployee = () => {
    setEditPopupOpen(true);
  };

  const handleSaveEmployee = async () => {
    if (!/^\d{5}$/.test(empId)) {
      alert("Employee ID must be a 5-digit number.");
      return;
    }
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(emailId)) {
      alert("Invalid email address.");
      return;
    }
  
    const newEmployee = {
      employeeId: parseInt(empId),
      email: emailId,
      password: password || "password",
      firstName: firstName,
      lastName: lastName,
      role: type,
      yearsOfExperience: yearOfExp,
      employeeType: employeeType,
      stack: techStack,
    };
  
    try {
      const response = await axios.post(
        `http://192.168.1.20:8081/api/v1/employee`,
        newEmployee,{
          headers:{
            'Authorization':`${sessionStorage.getItem("token")}`
          }
        }
      );
      console.log("Employee added successfully:", response.data);
  
      if (selectedEmployee) {
        const updatedData = data.map((emp) =>
          emp.employeeId === response.data.employeeId ? response.data : emp
        );
        setData(updatedData);
      } else {
        setData([...data, response.data]);
      }
  
      setEmpId("");
      setFirstName("");
      setLastName("");
      setEmailId("");
      setpassword("");
      setType("Employee");
      setTechStack("");
      setYearOfExp("");
      setEditPopupOpen(false);
    } catch (error) {
      console.error("Error adding employee:", error);
      toast.error("Error adding employee. Please try again.");
    }
  };
  
  useEffect(() => {
    const fetchData = async () => {
      try {
        const response = await axios.get(
          "http://192.168.1.20:8081/api/v1/employee",
          {
            headers:{
              'Authorization':`${sessionStorage.getItem("token")}`
            }
          }
        );
        setData(response?.data.object.content);
      } catch (error) {
        console.error("Error fetching employee data:", error);
      }
    };
    fetchData();
  }, []);

  const popupHeader = "Add New Employee";
  const popupBody = () => {
    return (
      <div className="popup_inner">
        <label>Employee ID</label>
        <input
          type="text"
          value={empId}
          onChange={(e) => setEmpId(e.target.value)}
        />
        <label>First Name</label>
        <input
          type="text"
          value={firstName}
          onChange={(e) => setFirstName(e.target.value)}
        />
        <label>Last Name</label>
        <input
          type="text"
          value={lastName}
          onChange={(e) => setLastName(e.target.value)}
        />
        <label>Email ID</label>
        <input
          type="text"
          value={emailId}
          onChange={(e) => setEmailId(e.target.value)}
        />
        <label>Tech Stack</label>
        <input
          type="text"
          value={techStack}
          onChange={(e) => setTechStack(e.target.value)}
        />
        <label>Year Of Experience</label>
        <input
          type="text"
          value={yearOfExp}
          onChange={(e) => setYearOfExp(e.target.value)}
        />
        <div className="EmployeeType">
          <label>Employee Type:</label>
          {employeeTypeOptions.map((typeOption, index) => (
            <div key={index} className="AddEmployeeTypeInner">
              <input
                type="radio"
                value={typeOption}
                checked={employeeType === typeOption}
                onChange={() => setEmployeeType(typeOption)}
              />
              <label>{typeOption}</label>
            </div>
          ))}
        </div>
        <div className="AddEmployeeType">
          <label>Type:</label>
          {employeeTypes.map((typeOption, index) => (
            <div key={index} className="AddEmployeeTypeInner">
              <input
                type="radio"
                value={typeOption}
                checked={type === typeOption}
                onChange={() => setType(typeOption)}
              />
              <label>{typeOption}</label>
            </div>
          ))}
        </div>
        <Button
          className="AddEmployeeSaveBtn"
          text={selectedEmployee ? "Update" : "Save"}
          onClick={selectedEmployee ? handleSaveEmployee : handleAddEmployee}
        />
        <Button
          className="AddEmployeeCancelBtn"
          text="Cancel"
          onClick={() => setEditPopupOpen(false)}
        />
      </div>
    );
  };

  return (
    <div className="AddEmployee">
      <Switching Arrayofvalue={["Employee Table"]} />
      <div className="addEmployeeTopContainer">
        <div className="addEmployeeTopContainerLeft">
          <SearchBox />
          <Filter
            defaultOptions={["Manager", "Employee"]}
            className={"AddEmployeeFilter"}
          />
        </div>
        <Button
          text="Add Employee"
          className="AddEmployeeButton"
          onClick={handleAddEmployee}
        />
      </div>
      <div className="addEmployeeTableContainer">
      {data.length === 0 ? (
        <p>No data available</p>
      ) : 
      (
        <Table
          data={data}
          isDeletable={true}
          isEditable={true}
          onDelete={handleDelete}
          onEdit={handleEdit}
        />
      )
      }
    </div>


      <Popup
        trigger={isEditPopupOpen}
        heading={popupHeader}
        body={popupBody()}
        setTrigger={setEditPopupOpen}
      >
        <div className="AddEmployeepopup"></div>
      </Popup>
    </div>
  );
};

export default AddEmployee;
