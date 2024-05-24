import React, { useEffect, useState } from "react";
import { useDispatch, useSelector } from "react-redux";
import Button from "../button";
import ContentBox from "../text-match";
import {
  setValueToStore,
  filterSetFunction,
  handleAddSub,
} from "../../../utils/hr-assign-utils/index";
import { hrAssignConstants } from "../../../constants/hrAssign";
import styles from "./index.module.scss";

const HeaderDetails = ({ contestName, roundNo, type, handleSet }) => {
  const [value, setValue] = useState("");
  const count_percentage = useSelector((state) => state.count_percentage);
  const dispatch = useDispatch();
  const valueSet = {
    "Contest name:": contestName,
    "Round No:": roundNo,
  };
  const handleChange = (val) => {
    setValue(val);
  };
  useEffect(() => {
    if (count_percentage.filterType === "count") {
      setValue(count_percentage.count);
    } else if (count_percentage.filterType === "percentage") {
      setValue(count_percentage.percentage);
    }
  }, [
    count_percentage.filterType,
    count_percentage.count,
    count_percentage.percentage,
    handleSet,
  ]);

  const setValueIn = (val) => {
    if (val.length <= 3) {
      handleSet.setDuration(val);
    } else {
      handleSet.setDate(val);
    }
  };

  return (
    <div className={`${styles.head} flex-row`}>
      <div className={styles.head_details}>
        {hrAssignConstants.headerDetailsContent.map((contestSetUp) => {
          if (contestSetUp.inputBox) {
            contestSetUp.inputBox["onChangeFunction"] = setValueIn;
            if (contestSetUp.title === "Duration per HR:") {
              contestSetUp.inputBox.value = handleSet.durationVal;
            } else if (contestSetUp.title === "Start Date & Time:") {
              contestSetUp.inputBox.value = handleSet.dateVal;
            }
          }
          return (
            <ContentBox
              key={contestSetUp.title}
              title={contestSetUp.title}
              value={contestSetUp.value && valueSet[contestSetUp.title]}
              inputBox={contestSetUp.inputBox && contestSetUp.inputBox}
              direction={contestSetUp.direction}
            />
          );
        })}
      </div>
      {type === "Technical HR" && (
        <>
          <div className={styles.head_filter}>
            <div className={styles.head_filter_in}>
              <ContentBox
                title={"Filter:"}
                inputBox={{
                  type: "radio",
                  options: ["Percentage", "Count"],
                  min: [0, 0],
                  max: [null, 100],
                  direction: "row",
                  onClickFunction: (type) => filterSetFunction(type, dispatch),
                }}
                align={"start"}
              />
              {hrAssignConstants.operators.map((operator) => (
                <p
                  key={operator}
                  onClick={() =>
                    handleAddSub(
                      operator === "-" ? "sub" : "add",
                      count_percentage,
                      value,
                      dispatch
                    )
                  }
                >
                  {operator}
                </p>
              ))}
            </div>
            <div className={styles.head_filter_apply}>
              <ContentBox
                inputBox={{
                  placeholder: count_percentage.filterType
                    ? count_percentage.filterType
                    : "select",
                  type: "text",
                  value: value,
                  onChangeFunction: handleChange,
                }}
              />
              <Button
                text={"Apply"}
                onClickFunction={() =>
                  setValueToStore(count_percentage, value, dispatch)
                }
              />
            </div>
          </div>
        </>
      )}
    </div>
  );
};

export default HeaderDetails;
