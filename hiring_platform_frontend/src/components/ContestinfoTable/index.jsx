import React, { useState } from "react";
import "./ContestinfoTable.scss";
import Pagination from "../Pagination";
import Button from "../Button";
import { MdDelete, MdModeEdit } from "react-icons/md";
import Popup from "../Popup";
import { IoEyeSharp } from "react-icons/io5";
import { Link, useNavigate } from "react-router-dom";
import { finalResultApi, singleContestApi } from "../../actions/API_actions";
const ContestinfoTable = ({
  data = [],
  isEditable,
  isDeletable,
  onEdit,
  onDelete,
  isViewbtn,
  RoutingLink,
  clickeddata,
  heading,
  setPopupclick,
  contestIds,
  setFinalResult
}) => {
  if (!Array.isArray(data)) {
    console.error("Data is not an array:", data);
    return null; 
  }
  const [showPopup, setShowPopup] = useState(false);
  const [selectedRow, setSelectedRow] = useState({});
  const [currentPage, setCurrentPage] = useState(1);
  const itemsPerPage = 8;
  const navigate = useNavigate()
  const handleClickRow =  (rowData) => {
    setSelectedRow(rowData);
    setShowPopup(!showPopup);
    document.body.style.overflow = "hidden";
  }

  const closePopup = () => {
    setShowPopup(!showPopup);
    setSelectedRow({});
    document.body.style.overflow = "auto";
  };
  // console.log(data)
  const startIndex = (currentPage - 1) * itemsPerPage;
  const endIndex = currentPage * itemsPerPage;
  const currentData = data?.slice(startIndex, endIndex);
  const handlePageChange = (page) => {
    setCurrentPage(page);
  };
  // console.log(contestIds)
  const onview = async (data,index) => {
    clickeddata(data)
    // console.log(data[index].contestId)
    if (heading !== "Final Result") {
      
      try {
        const res = await singleContestApi(data[index].contestId)
        navigate(RoutingLink, { state: { contestData: res, contestId: data[index].contestId } });
      } catch (error) {
        console.error("Error in Single Contest api:", error);
      }
    } else {
      try {
        const res = await finalResultApi(contestIds,data[index].userId)
        setPopupclick(true)
        setFinalResult(res)
      } catch (error) {
        console.error("Error in Final result api:", error);
      }
    }

  }
  // console.log(RoutingLink)
  const DynamicTable = (data, isEditable, isDeletable, onEdit, onDelete, isViewbtn, RoutingLink) => {
    const headers = data?.length > 0 ? Object.keys(data[0]) : [];
    let finalData;
    if (data) {
      finalData = data?.map(({ contestId, ...rest }) => rest);
      finalData = data?.map(({ userId, ...rest }) => rest);
    }
    // console.log(data[0])
    return (
      <>
        <table>
          <thead>
            <tr>
              {headers?.map((header, index) => {
                return (
                  (header !== "contestId" && header !== "userId"&& header !== "score") &&
                  <th key={index}>{header}</th>
                )
              })}
              {((isEditable || isDeletable || isViewbtn) && finalData?.length > 0) && <th>Action</th>}
            </tr>
          </thead>
          <tbody>
            {finalData?.map((row, rowIndex) => {
              return (<tr key={rowIndex}>
                {headers.map((header, headerIndex) => {
                  return (
                    (header !== "contestId" && header !== "userId" && header !== "score") &&
                    <td key={headerIndex} onClick={() => handleClickRow(data[rowIndex].contestId)}>
                      {row[header]}
                    </td>
                  )
                })}

                {isEditable && isDeletable && (
                  <td>
                    <Button
                      icon={<MdModeEdit />}
                      className="Edit ButtonIcon"
                      onClick={(e) => onEdit(row)}
                    />
                    <Button
                      icon={<MdDelete />}
                      className="Delete ButtonIcon"
                      onClick={(e) => onDelete(row)}
                    />
                  </td>
                )}
                {isEditable && !isDeletable && (
                  <td>
                    <Button
                      icon={<MdModeEdit />}
                      className="Edit ButtonIcon"
                      onClick={(e) => onEdit(row)}
                    />
                  </td>
                )}
                {!isEditable && isDeletable && (
                  <td>
                    <Button
                      icon={<MdDelete />}
                      className="Delete ButtonIcon"
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
                        onClick={() => onview(data,rowIndex)}
                      />
                    </div>
                  </td>
                )}
              </tr>
              )
            })}
          </tbody>
        </table>
      </>
    );
  };

  return (
    <div className="TableStyle">
      {DynamicTable(currentData, isEditable, isDeletable, onEdit, onDelete, isViewbtn, RoutingLink)}


      <Pagination
        totalItems={data.length}
        itemsPerPage={itemsPerPage}
        onPageChange={handlePageChange}
      />
    </div>
  );
};

export default ContestinfoTable;
