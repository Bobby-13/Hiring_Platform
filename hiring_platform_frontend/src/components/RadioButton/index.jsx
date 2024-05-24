import "./RadioButton.scss"

const RadioTag = (props) => {
  const {
    name,
    value,
    onChange,
    onClick,
    className,
    lable
  } = props;
  return (
    <div className="common-RadioTag">
      <input
        name={name}
        onChange={onChange}
        value={value}
        className={className}
        type="radio"
        onClick={onClick}    
        />
        <label>{lable}</label>
    
    </div>
  );
};
export default RadioTag;
