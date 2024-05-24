import React, { useEffect } from "react";
import Button from "../button";
import ContentBox from "../text-match";
import { useHandleClick } from "../../../utils/hr-assign-utils";

//stylesheet
import styles from "./index.module.scss";
import { camelToSpacePascal } from "../../../utils/splitHR";

const ContestDisplay = ({ contests, typeOfHR }) => {
  const handleClick = useHandleClick();
  let keys = Object.keys(contests[0] ? contests[0] : {});
  keys = keys.filter((key) => key !== "roundId" && key !== "roundType");
  useEffect(() => {}, [contests]);
  return (
    <div className={styles.contest_display}>
      {contests.map((contest, index) => {
        return (
          <div key={index} className={styles.contest_display_details}>
            <div
              className={`${styles.contest_display_details_box} flex-justify-between`}
            >
              {keys.map((key) => {
                return (
                  <ContentBox
                    key={key}
                    title={camelToSpacePascal(key)}
                    value={contest[key]}
                  />
                );
              })}
            </div>
            <div style={{ textAlign: "center", width: "10%" }}>
              <Button
                text={
                  contest.status === "Assigned" ? "View Schedule" : "Assign HR"
                }
                onClickFunction={() => handleClick(contest, typeOfHR)}
              />
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default ContestDisplay;
