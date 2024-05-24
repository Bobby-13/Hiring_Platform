import "./Input.scss"

const InputTag = (props) => {
  const {
    placeholder,
    name,
    value,
    type,
    onChange,
    onClick,
    className,
    label,
    checkboxHeading
  } = props;


  return (
    <div className="common-input">
    <label>{label}</label>
      <input
        name={name}
        placeholder={placeholder}
        onChange={onChange}
        value={value}
        className={className}
        type={type}
        onClick={onClick}  
        autoComplete="false"  
      />
      <label style={{marginLeft:"1rem"}}>{checkboxHeading}</label>
    </div>
  );
};

export default InputTag;
