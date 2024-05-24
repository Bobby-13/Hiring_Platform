import React from 'react';
import styles from './index.module.scss';

const DynamicToggle = ({ switchStates, handleToggle, page, home }) => {
  return (
    <div className={styles?.dynamicToggle}>
      {switchStates?.map((item, index) => {
        return (
          <button
            key={index}
            className={ item === page ? styles?.selected : ''}
            onClick={() => {
              handleToggle(item, index);
            }}
          >
            {
             item
            }
          </button>
        );
      })}
    </div>
  );
};

export default DynamicToggle;
