import "/home/divum/Desktop/hiring platform/hiring-platform-frontend/src/stylesheets/index.scss";

function Contestcard({ heading, content }) {
  return (
    <div className="Contestcard">
      <p style={{ fontSize: "1.5rem" }}>
        {heading} {content}
      </p>
    </div>
  );
}

export default Contestcard;
