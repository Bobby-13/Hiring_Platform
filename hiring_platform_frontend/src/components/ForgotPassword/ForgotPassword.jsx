import React, { useState } from "react";
import "./ForgotPassword.scss";
import { FiMail } from "react-icons/fi";
import assets from "../../assets";
import axios from "axios";
import { ToastContainer, toast } from "react-toastify";
const Forgetpassword = () => {
  const [email, setEmail] = useState("");
  const [showPopup, setShowPopup] = useState(false);
  const [emailError, setEmailError] = useState("");

  const validateEmail = (email) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(email);
  };

  const handleContinue = () => {
    if (validateEmail(email)) {
      setShowPopup(true);
      setEmailError("");
    } else {
      toast.error("Please enter a valid email address");
    }
  };

  const handleBackToLogin = () => {
    window.location.href = "/";
  };
  const handlePassword = async () => {
    try {
      const data = {
        email
      };
      const response = await axios.post(
        `http://192.168.1.20:8081/api/v1/employee/password-reset/email/${email}`,
        data,
        { params: { method: 'FORGET_PASSWORD' } }
      );
      console.log(response);
      toast.success("Mail sent successfully", 200);
      window.location.href = "/";
    } catch (error) {
      console.error("Error resetting password:", error);
    }
  };
  
  return (
    <div className="Forgetpasswordbox">
      <ToastContainer className="toast-message" closeButton={false} />
      <h1 className="password-heading">
        Forget Password? That's okay! Let's change it
      </h1>
      <div className="emailidbox">
        <h1 className="label-name"> Email id</h1>
        <FiMail className="mail-icon" />
        <input
          type="email"
          placeholder="Enter your email"
          className="input-box"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
      </div>
      {emailError && <p className="error-message">{emailError}</p>}
      <button className="continuebutton" onClick={handlePassword}>
        CONTINUE
      </button>
      <h1 className="backtologin" onClick={handleBackToLogin}>
        Back to Login
      </h1>
      {showPopup && (
        <div className="popup">
          <img className="done-icon" src={assets.doneIcon} alt="/"></img>
          <p>
            Woo-hoo. Well done! You’ve got an email to reset your password.
            Let’s make it happen.
          </p>
          <button onClick={() => setShowPopup(false)}>CLOSE</button>
        </div>
      )}
    </div>
  );
};

export default Forgetpassword;
