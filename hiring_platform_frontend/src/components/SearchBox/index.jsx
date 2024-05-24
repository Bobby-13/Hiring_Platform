import React, { useEffect, useRef, useState } from "react";
import { FaSearch } from "react-icons/fa";
import Button from "../Button";
import "./SearchBox.scss";

const SearchBox = () => {
  const [searchTerm, setSearchTerm] = useState("");
  const [suggestions, setSuggestions] = useState([]);
  const suggestionsRef = useRef(null);

  useEffect(() => {
    function handleClickOutside(event) {
      if (
        suggestionsRef.current &&
        !suggestionsRef.current.contains(event.target)
      ) {
        setSuggestions([]);
      }
    }
    document.addEventListener("mousedown", handleClickOutside);
    return () => {
      document.removeEventListener("mousedown", handleClickOutside);
    };
  }, []);

  const handleChange = (event) => {
    const { value } = event.target;
    setSearchTerm(value);
    fetchSuggestions(value);
  };
  const handleSearchClick = (suggestion) => {
    setSearchTerm(suggestion);
    setSuggestions([]);
  };
  const handleSearchIconClick = () => {
    console.log("Search value is -->", searchTerm);
  };

  const fetchSuggestions = (term) => {
    setTimeout(() => {
      const dummySuggestions = [
        `${term} suggestion 1`,
        `${term} suggestion 2`,
        `${term} suggestion 3`,
      ];
      setSuggestions(dummySuggestions);
    }, 500);
  };

  return (
    <div className="searchBox">
      <input
        type="text"
        placeholder="Search"
        className="searchBar"
        value={searchTerm}
        onChange={handleChange}
      />
      <Button
        className="searchIcon"
        icon={<FaSearch />}
        onClick={handleSearchIconClick}
      />
      <ul ref={suggestionsRef} className="suggestionsContainer">
        {suggestions.map((suggestion, index) => (
          <li key={index} onClick={() => handleSearchClick(suggestion)}>
            {suggestion}
          </li>
        ))}
      </ul>
    </div>
  );
};

export default SearchBox;
