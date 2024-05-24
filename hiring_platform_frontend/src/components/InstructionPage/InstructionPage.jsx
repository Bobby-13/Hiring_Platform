import React,{useState} from "react";
import "/home/divum/hiring_platform/hiring-platform-frontend/src/components/InstructionPage.scss";



function InstructionPage() {
   
        const [isChecked, setChecked] = useState(false);
      
        const handleCheckboxChange = () => {
          setChecked(!isChecked); 
        };
      
    return (
      <div className="main-container"> 
      
      <h1 className="heading">TEST INSTRUCTION</h1> 
      <div className="testinstructioncontent">
      <p className="instructions">You must only attempt this exam once. Any additional attempts should only be used in
         the event where a serious technical issue has occurred and 
         supporting evidence supporting this will be required. </p>
         <p className="instructions">You are not permitted to obtain assistance by improper means
          or ask for help from or give help to any other person. </p>
         <p className="instructions">You are not permitted to take screenshots, record the screen, 
            copy and paste questions or answers or otherwise attempt to take any of the 
            content of this exam out of the exam for any purpose. 
         </p>
         <p >You are not permitted to post any requests for clarification of exam content. 
            Answer all questions to the best of your ability and perception of the questions’ intent,
          make reasonable assumptions if necessary, to answer all questions. 
          UTS assessments never apply negative marking techniques.</p>
          <p>Misconduct action will be taken against you if you breach portal rules. </p>
          <p>TIME DURATION :1:30:00</p>
          
        <div className="checkboxcontent" >
        <input
          type="checkbox"
          checked={isChecked}
          onChange={handleCheckboxChange}
          className="checkbox"
        />
        I have read and understand the examination conduct requirements for the exam
      </div>
      <button className="agree">I,AGREE</button>
      </div>
      </div>
    )
  }
  
  export default InstructionPage;