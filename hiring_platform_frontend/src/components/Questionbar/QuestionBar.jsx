import React, { useState } from "react";
import "./QuestionBar.scss";

function QuestionBar() {
    const [markedAnswers, setMarkedAnswers] = useState(Array(15).fill(false));
    const [markedAnswers1, setMarkedAnswers1] = useState(Array(15).fill(false));

    const handleButtonClick = (index) => {
        const updatedMarkedAnswers = [...markedAnswers];
        updatedMarkedAnswers[index] = !updatedMarkedAnswers[index];
        setMarkedAnswers(updatedMarkedAnswers);
    };
    const handleButtonClick1 = (index) => {
        const updatedMarkedAnswers = [...markedAnswers1];
        updatedMarkedAnswers[index] = !updatedMarkedAnswers[index];
        setMarkedAnswers1(updatedMarkedAnswers);
    };
    const getButtonColor = (index) => {
        return markedAnswers[index] ? "#606795" : "#BCBADB";
    };
    const getButtonColor1 = (index) => {
        return markedAnswers1[index] ? "#606795" : "#BCBADB";
    };

    return (
        <div className="side-bar">
            <p className="text-1">VERBAL</p>
            <div className="button-container">
                {Array.from({ length: 15 }, (_, index) => (
                    <button
                        key={index}
                        className="custom-button"
                        tabIndex="0"
                        type="button"
                        style={{ backgroundColor: getButtonColor(index) }}
                        onClick={() => handleButtonClick(index)}
                    >
                        <span className="button-text">{index + 1}</span>
                    </button>
                ))}
            </div>
            <p className="text-2">LOGICAL</p>
            <div className="button-container-1">
                {Array.from({ length: 15 }, (_, index) => (
                    <button
                        key={index}
                        className="custom-button-1"
                        tabIndex="0"
                        type="button"
                        style={{ backgroundColor: getButtonColor1(index) }}
                        onClick={() => handleButtonClick1(index)}
                    >
                        <span className="button-text-1">{index + 1}</span>
                    </button>
                ))}
            </div>
            <div className="legend">
                <div className="legend-item">
                    <div className="color-code-606795"></div>
                    <span>ANSWERED</span>
                </div>
                <div className="legend-item">
                    <div className="color-code-BCBADB"></div>
                    <span>NOT ANSWERED</span>
                </div>
                <div className="legend-item">
                    <div className="color-code-flag"></div>
                    <span>FLAGGED</span>
                </div>
            </div>
        </div>
    );
}

export default QuestionBar;
