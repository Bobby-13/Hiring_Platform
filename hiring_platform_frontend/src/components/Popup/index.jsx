import React from 'react';
import "./Popup.scss";
import { RxCross2 } from "react-icons/rx";


function Popup({ trigger=false, setTrigger=()=>{}, body="", heading="", footer="" }) {
  return trigger ? (
    <div className='popup-background'>
      <div className='popup-container'>
        <div className='popup-header'>
        <h1>{heading}</h1>
          <button className='close-btn' onClick={() => setTrigger(false)}>
            <RxCross2 />
          </button>
        </div>
        <div className='popup-content'>
          {body}</div>
        <div className='popupbuttons'>
          {footer}
        </div>
      </div>
    </div>
  ) : null;
}

export default Popup;
