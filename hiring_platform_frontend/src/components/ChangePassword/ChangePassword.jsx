import React, { useState } from "react";
import "./ChangePassword.scss";
import { ToastContainer, toast } from "react-toastify";
import axios from "axios";

const Resetpassword = () => {
  const [oldPassword, setOldPassword] = useState('');
  const [newPassword, setNewPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    if (name === "currentPassword") {
      setOldPassword(value);
    } else if (name === "newPassword") {
      setNewPassword(value);
    } else if (name === "confirmPassword") {
      setConfirmPassword(value);
    }
  };

  const handleReset = async () => {
    try {
      const data = {
        oldPassword,
        newPassword,
      };
      const response = await axios.post(
        `http://192.168.1.20:8081/api/v1/employee/password-reset/email/${sessionStorage.getItem("email")}`,
        data,
        { params: { method: 'CHANGE_PASSWORD' } , 
          headers:{
            'Authorization':`${sessionStorage.getItem("token")}`
          }
        }
      );
      console.log(response);
      toast.success("Reset successful",200);
      window.location.href = "/";
    } catch (error) {
      console.error("Error resetting password:", error);
    }
  };
  

  return (
    <div className="Resetpasswordbox">
      <ToastContainer className="toast-message" closeButton={false} />
      <h1 className="heading">CHANGE PASSWORD</h1>
      <div className="Resetpasswordcontent">
        <div className="passwordchange">
          <p className="labelname">CURRENT PASSWORD :</p>
          <input
            type="password"
            placeholder="Current password"
            className="inputbox"
            name="currentPassword"
            value={oldPassword}
            onChange={handleInputChange}
          />
        </div>
        <div className="newpassword">
          <p className="labelname">NEW PASSWORD :</p>
          <input
            type="password"
            placeholder="New password"
            className="inputbox"
            name="newPassword"
            value={newPassword}
            onChange={handleInputChange}
          />
        </div>
        <div className="newpassword">
          <p className="labelname">CONFIRM PASSWORD :</p>
          <input
            type="password"
            placeholder="Confirm password"
            className="inputbox"
            name="confirmPassword"
            value={confirmPassword}
            onChange={handleInputChange}
          />
        </div>
        <button className="Resetbutton" onClick={handleReset}>
          Reset
        </button>
      </div>
    </div>
  );
};

export default Resetpassword;
