import React from 'react'
import "../Previewpage/Preview.scss"
import CodingTest from '../../../../components/CodingTest'
const Preview = () => {
  return (
    <div>
      <h1>Preview </h1>
      <div className='holepreview'>
        <div className='quesion' >
       <h3>Question Type : ALGORITHMS_CODING</h3>
       {/* <h2>questions No : 1   </h2>  */}
       <p>Given a number n, print n-th Fibonacci Number.The Fibonacci numbers are the numbers in the following integer sequence: 0, 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89, 144</p>
       <div className='codingsample'>
        <h3>sample testcase 1</h3>
        <p>Input : 9.0</p>
        <p>output : 34.0</p>
       </div>
        </div>
       <div className='codingcompiler'>
       <CodingTest height={"25vh"} width={"40rem"}/>
       </div>
      </div>
    </div>
  )
}

export default Preview
