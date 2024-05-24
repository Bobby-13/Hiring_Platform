import React, { useState } from "react";
import "./ViewFeedback.scss";
import Button from "../../../components/Button";

const ViewFeedback = () => {
    return (
        <div className="viewfeed-container">
            FEEDBACK SUMMARY
            <div className="feed-container">
                DESCRIPTION:
                <div>
                    <input type="text" className="input"  readOnly />
                </div>
            </div>
            <Button
                className="submit"
                text="Cancel"
            />
        </div>
    );
};

export default ViewFeedback;
