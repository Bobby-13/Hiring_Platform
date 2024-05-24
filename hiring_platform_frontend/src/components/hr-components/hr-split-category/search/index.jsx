import React from "react";
import { FaSearch } from "react-icons/fa";
import styles from "./index.module.scss";

const Search = () => {
  return (
    <div className={`${styles.search_component} flex-align-center`}>
      <input placeholder="search" />
      <div className={`${styles.search_icon} flex-align-center`}>
        <FaSearch />
      </div>
    </div>
  );
};

export default Search;
