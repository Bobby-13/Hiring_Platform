import React, { useState } from "react";
import Filter from "../../../components/Filter";
import Button from "../../../components/Button";
import SearchBox from "../../../components/SearchBox";
import Table from "../../../components/Table";
import { McqData } from "../../../helpers/addQuestionsHelper";
import { Link } from "react-router-dom";
import axios from "axios";
import "../AddQuestion/addQuestion.scss";
const Modal = ({ onClose, children }) => {
  return (
    <div className="modal">
      <div className="modal-content">
        <span className="close" onClick={onClose}>
          &times;
        </span>
        {children}
      </div>
    </div>
  );
};

const McqAddQuestion = () => {
  const [filteredData, setFilteredData] = useState(McqData);
  const [selectedItem, setSelectedItem] = useState("");
  const [isModalOpen, setIsModalOpen] = useState(false);
  const addQuestionLabels = [
    { value: "LOGICAL_MCQ", label: "Logical" },
    { value: "VERBAL_MCQ", label: "Verbal" },
    { value: "APTITUDE_MCQ", label: "Aptitude" },
    { value: "TECHNICAL_MCQ", label: "Technical" },
  ];

  const [pageNumber, setPageNumber] = useState(1);
  const [pageSize, setPageSize] = useState(10);
  const [selectedIndex, setSelectedIndex] = useState("");

  const handlePageChange = (page) => {
    setPageNumber(page);
  };

  const handlePageSizeChange = (size) => {
    setPageSize(size);
  };

 

  const handleEdit = (item, index) => {
    setSelectedItem({ ...item });
    setIsModalOpen(true);
    setSelectedIndex(index);
  };

  const handleUpdate = async () => {
    console.log("Selected item question:", selectedItem.question);

    try {
      const response = await axios.put(
        `http://192.168.1.20:8081/api/v1/questions/mcq/${selectedItem.questionId}`,
        {
          question: selectedItem.question,
        },
        {
          headers:{
            'Authorization':`${sessionStorage.getItem("token")}`
          }
        }
      );
      console.log(response);
      const updatedData = filteredData.map((item) => {
        if (item.questionId === selectedItem.questionId) {
          return { ...item, question: selectedItem.question };
        } else {
          return item;
        }
      });

      setFilteredData(updatedData);
      setIsModalOpen(false);
    } catch (err) {
      console.log(err);
    }
  };
  const handleDelete = async (questionId) => {
    try {
       const questionIdData=questionId.questionId
      await axios.delete(`http://192.168.1.20:8081/api/v1/questions/mcq/${questionIdData}`,
      {
        headers:{
          'Authorization':`${sessionStorage.getItem("token")}`
        }
      });
      
      const updatedData = filteredData.filter(item => item.questionId !== questionId.questionId);
      setFilteredData(updatedData);
    } catch (err) {
      console.log(err);
    }
  };
  
  return (
    <div className="McqContainer">
      <div className="addQuestionTopContainer">
        <div className="addQuestionTopContainerLeft">
          <SearchBox />
          <Filter
            options={addQuestionLabels.map((option) => option.label)}
            pageNumber={pageNumber}
            pageSize={pageSize}
            setFilteredData={(e) => {
              setFilteredData(e);
            }}
          />
        </div>
        <Link to="addQuestionSetup/mcq">
          <Button text="Add New" className="addNewButton" />
        </Link>
      </div>
      <div className="addQuestionTableContainer">
        <Table
          data={filteredData}
          isEditable={true}
          isDeletable={true}
          onEdit={(item, index) => handleEdit(item, index)}
          onDelete={(item, index) => handleDelete(item, index)}
          pageNumber={pageNumber}
          pageSize={pageSize}
          onPageChange={handlePageChange}
          onPageSizeChange={handlePageSizeChange}
        />
      </div>
      {isModalOpen && selectedItem && (
        <Modal onClose={() => setIsModalOpen(false)}>
          <h2>Edit Item</h2>
          <textarea
            value={selectedItem.question}
            onChange={(e) =>
              setSelectedItem({ ...selectedItem, question: e.target.value })
            }
            rows={4}
            cols={50}
          />
          <Button text="Update" onClick={handleUpdate} />
        </Modal>
      )}

    </div>
  );
};

export default McqAddQuestion;
