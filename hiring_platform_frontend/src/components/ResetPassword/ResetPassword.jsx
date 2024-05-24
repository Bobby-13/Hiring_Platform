import React, { useState, useEffect } from "react";
import "./ResetPassword.scss";
import { ToastContainer, toast } from "react-toastify";
import axios from "axios";

const Resetpassword = () => {
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");

  const [token, setTokenData] = useState("");

  useEffect(() => {
    console.log("user");
    const url = new URLSearchParams(window.location.search);
    const tokenData = url.get("token");
    setTokenData(tokenData);
    console.log("token-->", tokenData);
  }, []);

  const handleInputChange = (event) => {
    const { name, value } = event.target;
    if (name === "newPassword") {
      setNewPassword(value);
    } else if (name === "confirmPassword") {
      setConfirmPassword(value);
    }
  };

  const handleReset = async () => {
    try {
      const data = {
        newPassword,
        token,
      };
      const response = await axios.post(
        `http://192.168.1.20:8081/api/v1/employee/password-reset?token=${token}`,
        data, {
          headers:{
            'Authorization':`${sessionStorage.getItem("token")}`
          }
        }
      );
      console.log(response);
      toast.success("Reset successful");
      window.location.href = "/";
    } catch (error) {
      console.error("Error resetting password:", error);
    }
  };

  return (
    <div className="Resetpasswordbox">
      <ToastContainer className="toast-message" closeButton={false} />
      <h1 className="heading">RESET PASSWORD</h1>
      <div className="Resetpasswordcontent">
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
