import React, { useState } from "react";
import "../Table/Table.scss";
import Pagination from "../Pagination";
import Button from "../Button";
import { MdDelete, MdModeEdit } from "react-icons/md";
import Popup from "../Popup";
import PieChart from "../Piechart/index";
import { IoMdDownload } from "react-icons/io";
import { useNavigate } from "react-router";

const Table = ({
  data = [],
  onResume,
  isEditable,
  isDeletable,
  onEdit,
  onDelete,
  onStart,
  onView,
  onReschedule,
  onFeedback,
  onFeedbackClick,
  onStatus,
  currentData
}) => {
  if (!Array.isArray(data)) {
    console.error("Data is not an array:", data);
    return null;
  }
  const [showPopup, setShowPopup] = useState(false);
  const [selectedRow, setSelectedRow] = useState({});
  const [currentPage, setCurrentPage] = useState(1);
  const [sortBy, setSortBy] = useState(null);
  const [sortAsc, setSortAsc] = useState(true);
  const itemsPerPage = 8;
  const navigate = useNavigate()
  const handleClickRow =  (rowData) => {
    setSelectedRow(rowData);
    setShowPopup(!showPopup);
    document.body.style.overflow = 'hidden';
  };

  const closePopup = () => {
    setShowPopup(!showPopup);
    setSelectedRow({});
    document.body.style.overflow = 'auto';
  };

  const handlePageChange = (page) => {
    setCurrentPage(page);
  };

  const handleSort = (column) => {
    if (sortBy === column) {
      setSortAsc(!sortAsc);
    } else {
      setSortBy(column);
      setSortAsc(true);
    }
  };

  const sortedData = [...data];
  if (sortBy) {
    sortedData.sort((a, b) => {
      const valueA = a[sortBy];
      const valueB = b[sortBy];
      if (typeof valueA === "string" && typeof valueB === "string") {
        return valueA.localeCompare(valueB, undefined, { sensitivity: "base" });
      } else {
        return valueA - valueB;
      }
    });
    if (!sortAsc) {
      sortedData.reverse();
    }
  }

  const DynamicTable = () => {
    if (!Array.isArray(data) || data.length === 0) {
      return null;
    }
    const headers = Object?.keys(data[0]);
    const startIndex = (currentPage - 1) * itemsPerPage;
    const endIndex = startIndex + itemsPerPage;
    const displayedData = sortedData?.slice(startIndex, endIndex);

    return (
      <>
        <table>
          <thead>
            <tr>
              {headers?.map((header, index) => (
                <th key={index} onClick={() => handleSort(header)}>
                  {header?.replace(/_/g, " ")}{" "}
                  {sortBy === header && (sortAsc ? "▲" : "▼")}
                </th>
              ))}
              {onResume && <th>Resume</th>}
              {onView && <th>Coding</th>}
              {(isEditable ||
                isDeletable ||
                onStart ||
                onFeedback ||
                onReschedule) && <th>Action</th>}

              {onStatus && <th>Status</th>}
            </tr>
          </thead>
          <tbody>
            {displayedData?.map((row, rowIndex) => (
              <tr key={rowIndex}>
                {headers?.map((header, headerIndex) => {
                  if (header === "Score") {
                    const chartData = {
                      datasets: [
                        {
                          data: [
                            parseInt(row[header]),
                            100 - parseInt(row[header]),
                          ],
                          backgroundColor: ["lightgreen", "white"],
                          cutout: "0%",
                        },
                      ],
                    };
                    return (
                      <td
                        key={rowIndex}
                        style={{
                          display: "flex",
                          alignItems: "center",
                          gap: "10%",
                        }}
                      >
                        <PieChart
                          chartData={chartData}
                          className={"percentageChart"}
                        />
                      </td>
                    );
                  } else {
                    return (
                      <td key={headerIndex} onClick={() => handleClickRow(row)}>
                        {row[header]}
                      </td>
                    );
                  }
                })}
                {onResume && (
                  <td>
                    <Button
                      icon={<IoMdDownload />}
                      className="download-Button"
                      onClick={(e) => onResume(rowIndex)}
                    />
                  </td>
                )}
                {onView && (
                  <td>
                    <Button
                      text="View"
                      className="View-Button"
                      onClick={(e) => onView(rowIndex)}
                    />
                  </td>
                )}

                {(isEditable ||
                  isDeletable ||
                  onStart ||
                  onFeedback ||
                  onReschedule ||
                  onStatus) && (
                    <td>
                      {isEditable && (
                        <Button
                          icon={<MdModeEdit />}
                          className="Edit ButtonIcon"
                          onClick={(e) => onEdit(row)}
                        />
                      )}
                      {isDeletable && (
                        <Button
                          icon={<MdDelete />}
                          className="Delete ButtonIcon"
                          onClick={(e) => onDelete(row)}
                        />
                      )}
                      {onStart && (
                        <Button
                          text="Start"
                          className="Start-Button"
                          onClick={(e) => onStart(rowIndex)}
                        />
                      )}
                      {onReschedule && (
                        <Button
                          text="Reschedule"
                          className="reschedule-Button"
                          onClick={() => onReschedule(rowIndex)}
                        />
                      )}
                      {onFeedback && (
                        <Button
                          text="Feedback"
                          className="feedback-Button"
                          onClick={() => onFeedback(rowIndex)}
                        />
                      )}
                    </td>
                  )}

                {onStatus && (
                  <td>
                    <Button
                      icon={<MdModeEdit />}
                      className='Edit ButtonIcon'
                      onClick={(e) => onEdit(row, rowIndex)}
                    />
                  </td>
                )}
                {!isEditable && isDeletable && (
                  <td>
                    <Button
                      icon={<MdDelete />}
                      className='Delete ButtonIcon'
                      onClick={(e) => onDelete(row)}
                    />
                  </td>
                )}
                {!isEditable && !isDeletable && isViewbtn && (
                  <td>
                    <div className="viewButton-wrapper">
                      <Button
                        icon={<IoEyeSharp />}
                        text={"View"}
                        className="viewButtonIcon"
                        onClick={() => onView(data, rowIndex)}
                      />
                    </div>
                  </td>
                )}
              </tr>
            )
            )}
          </tbody>
        </table>
      </>
    );
  };

  return (
    <div className='TableStyle'>
      {DynamicTable(currentData, isEditable, isDeletable, onEdit, onDelete)}

      <div trigger={showPopup} setTrigger={closePopup} data={selectedRow}>
        <div>
          <div className='PopupContent'>
            <ul>
              {Object?.entries(selectedRow || {}).map(([key, value]) => (
                <li key={key} type="none" className="listitems">
                  <strong>{key}:</strong> {JSON.stringify(value)}
                </li>
              ))}
            </ul>
          </div>
        </div>
      </div>
    </div>
  );
  // return (
  //   <div className="TableStyle">
  //     {DynamicTable()}

  //     <Popup
  //       trigger={showPopup}
  //       setTrigger={closePopup}
  //       data={selectedRow}
  //       body={values()}
  //     />
  //     {data && (
  //       <Pagination
  //         totalItems={data.length}
  //         itemsPerPage={itemsPerPage}
  //         onPageChange={handlePageChange}
  //       />
  //     )}
  //   </div>
  // )

};




export default Table;
