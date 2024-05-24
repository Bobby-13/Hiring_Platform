import React from "react";
import styles from "./index.module.scss";
import { BsPersonFill } from "react-icons/bs";
import ContentBox from "../../text-match";
import Button from "../../button";
import { FaCheckCircle } from "react-icons/fa";
import { CiCircleCheck } from "react-icons/ci";
import { toPascalCase } from "../../../../utils/splitHR";

const StatusCard = ({ data, handleClicks }) => {
  return (
    <div className={`${styles.status_card} flex-column-center`}>
      <div className={styles.status_card_content_box}>
        <BsPersonFill fontSize={45} />
        <ContentBox title={data.name} value={data.email} />
        {data.status === "AVAILABLE" && (
          <Button text={toPascalCase(data.status)} type={"success_status"} />
        )}
        {data.status === "PENDING" && (
          <Button text={"Requested"} type={"safe"} />
        )}
        {data.status === "SELECTED" && (
          <Button
            icon={<FaCheckCircle />}
            type={"icon"}
            onClickFunction={() => handleClicks.handleToRemoveSelect(data)}
          />
        )}
        {data.status === "Not assigned" && (
          <Button
            icon={<CiCircleCheck />}
            type={"icon"}
            onClickFunction={() => handleClicks.handleAddToSelect(data)}
          />
        )}
      </div>
      {data.status === "Accepted" && (
        <div className={styles.status_card_button}>
          {data.status === "ACCEPTED" && (
            <>
              <Button text={"Remove"} type={"danger"} />
            </>
          )}
          {data.status === "Assigned" && (
            <>
              <Button
                text={"Remove"}
                type={"danger"}
                onClickFunction={() => handleClicks.handleAddToAccepted(data)}
              />
            </>
          )}
        </div>
      )}
    </div>
  );
};

export default StatusCard;
