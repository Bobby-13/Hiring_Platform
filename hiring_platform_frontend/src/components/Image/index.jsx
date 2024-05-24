const Images = (props) => {
  return (
    <img
      src={props.src}
      alt={props.alt}
      className={props.className}
      onClick={props.onClick}
      height={props.height}
      width={props.width}
    >
      {props.text}
    </img>
  );
};

export default Images;
