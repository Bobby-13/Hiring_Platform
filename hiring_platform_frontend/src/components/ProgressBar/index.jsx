import React, { useState, useEffect } from 'react'
import "../ProgressBar/progressBar.scss"
import { IoMdCheckmark } from "react-icons/io";

const Progressbar = ({ className="", actives, select }) => {
  const count = [1, 2, 3, 4]
  const [active, setActive] = useState(1)

  useEffect(() => {
    setActive(actives);
  }, [actives]);

  return (
      <div className={`holeProgressContainer ${className}`} >
        {count.map((f, index) => (
          <div className='ProgressContainer' key={index}>
            <div className='circleValue' style={{ background: active >= index || index === select ? "#1FA72A" : "lightgray" }} >
              {active >= index ? <IoMdCheckmark style={{ color: "white" }} /> : f}
            </div>
            {count.length > f &&
              <div className='circleLine' style={{ background: active >= index ? "#1FA72A" : "lightgray" }}>
              </div>
            }
          </div>
        ))}
      </div>
  )
}

export default Progressbar
