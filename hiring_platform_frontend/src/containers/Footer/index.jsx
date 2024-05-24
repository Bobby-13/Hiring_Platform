import React from "react";
import "./Footer.scss";
import {
  FooterList,
  FooterList2,
  FooterList3,
  FooterList4,
} from "../../helpers/FooterList";
function Footer() {
  return (
    <div className="Footer">
      <div className="footerSection">
        <h2>Our Work</h2>
        {FooterList.map((data, index) => {
          return <p key={index}>{data}</p>;
        })}
      </div>
      <div className="footerSection">
        <h2>Company</h2>
        {FooterList2.map((data, index) => {
          return <p key={index}>{data}</p>;
        })}
      </div>{" "}
      <div className="footerSection">
        <h2>Business</h2>
        {FooterList3.map((data, index) => {
          return <p key={index}>{data}</p>;
        })}
      </div>
      <div className="footerSection">
        <h2>Let's Meet</h2>
        {FooterList4.map((data, index) => {
          return <p key={index}>{data}</p>;
        })}
      </div>
    </div>
  );
}

export default Footer;
