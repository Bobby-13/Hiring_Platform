import React from 'react';
import styles from './index.module.scss';

const Button = ({ text, icon, type, onClickFunction }) => {
  let nameOfClass =
    type === 'danger'
      ? 'button_component_red'
      : type === 'success'
      ? 'button_component_green'
      : type === 'safe'
      ? 'button_component_yellow'
      : type === 'icon'
      ? 'button_component_icon'
      : type === 'success_status'
      ? 'button_component_green_static'
      : 'button_component';
  return (
    <button
      className={`${styles[nameOfClass]} flex-align-center`}
      onClick={onClickFunction}
    >
      {icon && <div>{icon}</div>}
      <div>{text}</div>
    </button>
  );
};

export default Button;
