import React, { useState, useEffect } from "react";
import "./Pagination.scss";
import Button from "../Button/index";

const Pagination = ({ totalItems, itemsPerPage, onPageChange }) => {
  const [activePage, setActivePage] = useState(1);
  const totalPages = Math.ceil(totalItems / itemsPerPage);

  useEffect(() => {
    setActivePage(1);
  }, [totalItems, itemsPerPage]);

  const handlePageClick = (pageNum) => {
    setActivePage(pageNum);
    onPageChange(pageNum);
  };

  const handlePrevClick = () => {
    const prevPage = activePage - 1;
    if (prevPage >= 1) {
      setActivePage(prevPage);
      onPageChange(prevPage);
    }
  };

  const handleNextClick = () => {
    const nextPage = activePage + 1;
    if (nextPage <= totalPages) {
      setActivePage(nextPage);
      onPageChange(nextPage);
    }
  };

  if (totalPages <= 1 ) {
    return null;
  }
  const pageNumbers = [];
  for (let i = 1; i <= totalPages; i++) {
    pageNumbers.push(
      <Button
        key={i}
        className={`page-num ${activePage === i ? "active" : ""}`}
        onClick={() => handlePageClick(i)}
        text={i}
      />
    );
  }

  return (
    <div className="pagination">
      <Button
        className="page-arrow"
        onClick={handlePrevClick}
        disabled={activePage === 1}
        text="&lsaquo;"
      />
      {pageNumbers}
      <Button
        className="page-arrow"
        onClick={handleNextClick}
        disabled={activePage === totalPages}
        text="&rsaquo;"
      />
    </div>
  );
};

export default Pagination;