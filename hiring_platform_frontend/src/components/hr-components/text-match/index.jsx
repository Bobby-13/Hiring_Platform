import React from "react";
import styles from "./index.module.scss";

const ContentBox = ({
  title,
  value,
  direction,
  align,
  inputBox = {
    type: "",
    placeholder: "",
    options: [],
    onClickFunction: () => {},
    onChangeFunction: () => {},
    direction: "column",
    value,
    disabled: false,
  },
}) => {
  let statusClass = "values";
  if (title === "Status") {
    statusClass = value === "Assigned" ? "assign" : "pending";
  }
  let boxDirection = direction ? direction : "content_box";
  let alignItem = align ? align : "";
  return (
    <div
      className={`${styles[boxDirection]}  ${styles[alignItem]} ${styles.bt_component}`}
    >
      {title && <p>{title}</p>}
      {inputBox.type ? (
        inputBox.type === "radio" ? (
          <div className={styles[inputBox.direction]}>
            {inputBox.options.map((label) => (
              <React.Fragment key={label}>
                <input
                  type="radio"
                  id={`radio-${label}`}
                  name="radio-group"
                  onClick={() => inputBox.onClickFunction(label)}
                />
                <label htmlFor={`radio-${label}`}>{label}</label>
              </React.Fragment>
            ))}
          </div>
        ) : (
          <input
            type={inputBox.type}
            placeholder={inputBox.placeholder}
            value={inputBox.value ? inputBox.value : ""}
            onChange={(e) =>
              inputBox.onChangeFunction &&
              inputBox.onChangeFunction(e.target.value)
            }
            disabled={inputBox.disabled}
          />
        )
      ) : (
        <p className={`${styles[statusClass]}`}>{value}</p>
      )}
    </div>
  );
};

export default ContentBox;
