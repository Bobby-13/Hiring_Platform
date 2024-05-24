import React, { useState, useEffect } from "react";
import { FiEyeOff, FiEye, FiMail, FiKey } from "react-icons/fi";
import { ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import "./LoginPage.scss";
import axios from "axios";
import { useNavigate } from "react-router-dom";
function App() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: "",
    password: "",
    client: "",
  });

  const handleRoleData = (formData) => {
    const domain = formData.email?.substring(
      formData.email?.lastIndexOf("@") + 1
    );
    const role = domain === "divum.in" ? "EMPLOYEE" : "ADMIN";
    setFormData((prevState) => ({ ...prevState, client: role }));
  };

  const handleInputChange = ({ target: { name, value } }) => {
    setFormData({ ...formData, [name]: value });
  };

  const togglePassword = () => {
    setFormData({ ...formData, passwordVisible: !formData.passwordVisible });
  };

  const isEmailValid = () => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    return emailRegex.test(formData.email);
  };

  const handleForgotPassword = () => {
    window.location.href = "/forgetpassword";
  };

  const login = async (data) => {
    try {
      const response = await axios.post(
        "http://192.168.1.20:8081/api/v1/auth/login",
        data
      );
      console.log("response.data", response.data.object.role);
      if (response.data.object.role === "ADMIN") {
        const token=response?.data?.object?.token;
        const userId = response.data.object.id;
        const emailId=response.data.object.email;
        const Fname=response.data.object.fname;
        sessionStorage.setItem("userId",userId);
        sessionStorage.setItem("email",emailId);
        sessionStorage.setItem("name",Fname);
        sessionStorage.setItem("token",token);
        console.log("object")
        navigate(`admin/home/${userId}`);
      } 
     else {
        const token=response?.data?.object?.token;
        const userId = response.data.object.id;
        const emailId=response.data.object.email;
        const Fname=response.data.object.fname;
        sessionStorage.setItem("userId",userId);
        sessionStorage.setItem("email",emailId);
        sessionStorage.setItem("name",Fname);
        sessionStorage.setItem("token",token);
        navigate(`hr/hrhome/${userId}`);


      }
    } catch (err) {
      console.log(err);
    } 
  };

  const handleLogin = () => {
    if (isEmailValid()) {
      const passwordRegex =
        /^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$/;
      if (passwordRegex.test(formData.password)) {
        handleRoleData(formData);
        login(formData);
      } else {
        setFormData({
          ...formData,
          passwordError:
            "Password must contain at least one special character and one numeric character, and be at least 8 characters long",
        });
      }
    } else {
      setFormData({ ...formData });
      toast.error("Please enter a valid email address", {
        closeButton: true,
      });
    }
  };

  useEffect(() => {
    console.log("latest form data", formData);
  }, [formData]);

  return (
    <div className="LoginPage">
      <ToastContainer className="toast-message" closeButton={false} />
      <div>
        <img
          className="Loginimage"
          src="https://images.pexels.com/photos/4467739/pexels-photo-4467739.jpeg?auto=compress&cs=tinysrgb&w=600"
          alt="/"
        />
      </div>
      <div className="login-container">
        <h2>LOGIN</h2>
        <form>
          <label htmlFor="email">Email id:</label>
          <div className="inputBTN">
            <FiMail className="Icon" />
            <input
              placeholder="Email id"
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleInputChange}
            />
          </div>
          {formData.emailError && (
            <div className="email-error">{formData.emailError}</div>
          )}
          <label htmlFor="password">Password:</label>
          <div className="input-group">
            <div className="inputBTN">
              <FiKey className="Icon" />
              <input
                type="password"
                onChange={handleInputChange}
                value={formData.password}
                name="password"
                className="form-control"
                placeholder="Password"
              />
            </div>
            <div className="hidebutton" onClick={togglePassword}>
              {formData.passwordVisible ? <FiEye /> : <FiEyeOff />}
            </div>
            {formData.passwordError && (
              <div className="password-error">{formData.passwordError}</div>
            )}
            <div className="forgot-password" onClick={handleForgotPassword}>
              <p>Forgot Password?</p>
            </div>
          </div>
          <button type="button" onClick={() => handleLogin()}>
            Login
          </button>
        </form>
      </div>
    </div>
  );
}

export default App;
