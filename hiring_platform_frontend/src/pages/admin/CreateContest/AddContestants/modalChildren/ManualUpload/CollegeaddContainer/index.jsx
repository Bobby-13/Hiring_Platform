import React, { useState, useEffect } from 'react';
import InputTag from "../../../../../../../components/InputTag/index";
import Button from "../../../../../../../components/Button/index";
import "./Collegeadd.scss";
import { RxCross2 } from "react-icons/rx";
import { IoRemoveCircleOutline } from "react-icons/io5";

const CollegeAddConatiner = ({ className, addcontestdetail,index,removevalue,removeParticular }) => {
  const [collegenames, setCollegenames] = useState("");
  const [emailContainers, setEmailContainers] = useState([{ email: '', name: '', college:"" }]);
  useEffect(() => {
    handleAddEmailContainer(); 
  }, []); //eslint-disable-line

  const handleAddEmailContainer = () => {
    setEmailContainers([...emailContainers, { email: '', name: '', college:'' }]);

  };
 
  const handleRemoveEmailContainer = (index,remove) => {
    setEmailContainers(emailContainers.filter((_, idx) => idx !== index));
    removeParticular(remove)
    console.log(index)
  };

  const handleEmailChange = (index, field, value) => {
    const updatedContainers = [...emailContainers];
    updatedContainers[index][field] = value;
    updatedContainers[index]["college"] =collegenames;
    setEmailContainers(updatedContainers);
  };

  return (
    <div className={className}>
      <RxCross2 style={{ float: "right",display:index>0?"block":"none" }} onClick={()=>{removevalue(index,collegenames)}}  />
      <InputTag type={"text"} label={"College Name"} className={"collegename"} placeholder={"Enter College Name"}  onChange={(e) => setCollegenames(e.target.value)} />
      {emailContainers.map((container, index) => (
        <div className='emailidCard' key={index}>
          <div>
            <InputTag
              type={"text"}
              className={"emailInfo"}
              placeholder={"Email ID"}
              value={container.email}
              onChange={(e) => handleEmailChange(index, 'email', e.target.value)}
            />
          </div>
          <div>
            <InputTag
              type={"text"}
              className={"emailInfo"}
              placeholder={"Name"}
              value={container.name}
              onChange={(e) => handleEmailChange(index, 'name', e.target.value)}
            />
          </div>
          {index !== 0 && <IoRemoveCircleOutline className='minusbutton' onClick={() => handleRemoveEmailContainer(index,container.email)} />}
        </div>
      ))}
      <Button text={"Add Student"} className={"collegeaddbuttons"} onClick={handleAddEmailContainer} />
      <Button text={"Submit"} className={"submitbutton"} onClick={() => { addcontestdetail(emailContainers) }}></Button>

    </div>
  );
};

export default CollegeAddConatiner;
