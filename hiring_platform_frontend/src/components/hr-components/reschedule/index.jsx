import React, { useEffect, useState } from "react";
import ContentBox from "../text-match";
import Button from "../button";
import { hrAssignConstants } from "../../../constants/hrAssign";
import styles from "./index.module.scss";
import { useLocation, useNavigate } from "react-router";
import DynamicToggle from "../dynamic-toggle";
import SearchBox from "../../SearchBox";
import { camelToSpacePascal } from "../../../utils/splitHR";

const Reschedule = (props) => {
  const [requests, setRequests] = useState();
  const navigate = useNavigate();
  const data = useLocation().state;
  const [status, setStatus] = useState("Accepted");
  const handleClick = (status, data) => {
    status === "Accept" &&
      navigate("/admin/hrAssign/rescheduleaccepted", { state: { data } });
  };

  const handleLog = () => {
    navigate("/admin/hrAssign/reschedulelog", {
      state: {
        data: requests,
        toggleButton: ["Accepted", "Rejected"],
      },
    });
  };
  const handleToggle = (item) => {
    setStatus(item);
  };

  const handleViewSchedule = (dataToView) => {
    navigate("/admin/hrAssign/reschedulelog/viewstatus", {
      state: { data: dataToView },
    });
  };

  let keys = requests && requests[0] ? Object.keys(requests[0]) : [];
  keys = keys.filter(
    (item) =>
      item === "round" ||
      item === "contestName" ||
      item === "employeeName" ||
      item === "requestType"
  );
  useEffect(() => {
    if (data) {
      setRequests(data.data);
    } else {
      setRequests(props.requests);
    }
  }, [data,props.requests]);

  return (
    <div className={styles.reschedule}>
      {data ? (
        <div className={styles.reschedule_header}>
          <h2>Reschedule Log List</h2>
          <DynamicToggle
            switchStates={data?.toggleButton}
            handleToggle={handleToggle}
            page={status}
          />
          <SearchBox />
        </div>
      ) : (
        keys[0] && (
          <div className={styles.button_style}>
            <Button
              text={"View Request Log"}
              className={styles.button_style}
              onClickFunction={handleLog}
            />
          </div>
        )
      )}
      {requests?.map((request) => {
        return (
          <div
            key={request.id}
            className={`${styles.reschedule_requests} flex-column`}
          >
            <div
              className={`${styles.reschedule_requests_heading} flex-justify-between`}
            >
              {keys.map((key) => (
                <ContentBox
                  key={key}
                  title={camelToSpacePascal(key)}
                  value={request[key]}
                  direction={"row"}
                />
              ))}
            </div>
            <div className={styles.reschedule_requests_hrreason}>
              <ContentBox
                title={"HR Reason"}
                value={request.reason}
                align={"start"}
              />
            </div>
            <div className={styles.reschedule_requests_details}>
              <div>
                <ContentBox
                  title={"Contestant Name:"}
                  value={request.contestantName}
                  direction={"row"}
                />
              </div>
              <div className={styles.reschedule_requests_details_buttons}>
                {data ? (
                  <Button
                    text={"View Schedule"}
                    onClickFunction={() => handleViewSchedule(request)}
                  />
                ) : (
                  hrAssignConstants.buttonConstant.map((button) => {
                    return (
                      <Button
                        key={button.text}
                        text={button.text}
                        icon={button.icon}
                        type={button.text === "Accept" ? "success" : "danger"}
                        onClickFunction={() =>
                          handleClick(button.text, request)
                        }
                      />
                    );
                  })
                )}
              </div>
            </div>
          </div>
        );
      })}
    </div>
  );
};

export default Reschedule;
