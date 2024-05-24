import React from 'react';
import styles from './index.module.scss';
import { camelToSpacePascal } from '../../../utils/splitHR';

const MultiValueTable = ({ data }) => {
  const keys = Object.keys(data[0]);
  return (
    <table className={`${styles.table_component} flex-column`}>
      <thead>
        <tr className='flex-justify-between'>
          {keys.map((key) => (
            <td key={key}>{camelToSpacePascal(key)}</td>
          ))}
        </tr>
      </thead>
      <tbody>
        {data.map((values, index) => (
          <tr key={index}>
            {keys.map((key) => (
              <td key={key}>{values[key]}</td>
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  );
};

export default MultiValueTable;
