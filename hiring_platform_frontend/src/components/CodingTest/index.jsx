import React, { useState } from 'react'
import "./CodingTest.scss"
import { Editor } from "@monaco-editor/react";
import Button from "../Button/index"
const CodingTest = ({height,width}) => {
    const languageOptions = ["python", "javascript", "java", "cpp"];
    const [selectLanguage, setSelectLanguage] = useState("python");
    const themes=["vs-light","vs-dark"]
    const [theme,setTheme]=useState("vs-light")
    const [codeValue, setCodeValue] = useState(`Write code here`);
    const [coding,setCoding]=useState([{name:"",code:""}])
    const jsonFormat = JSON.stringify(coding, null, 2);
    const [screen,setScreen]=useState(false)
    const handleChange = (newValue) => {
      setCodeValue(newValue);
       setCoding((data)=>({...data,name:selectLanguage,code:codeValue}))
    };
  
    return (
        
                <div className='Compailer'>
                    <div className='optionsChange'>
                        <div className='language'>
                    Language : <select className='languageSelection' onChange={(event) => setSelectLanguage(event.target.value)}>
                        {languageOptions.map(f => (
                            <option key={f} >{f}</option>
                            ))}
                    </select>
                            </div>
                        <Button type={"button"} text={screen?"🌙 Dark":"☀️ light"} className={"screen"} onClick={()=>setScreen(!screen)} />
                    </div>
                    <Editor
                        height={height}
                        width={width}
                        theme={screen?"vs-light":"vs-dark"}
                        language={selectLanguage}
                        value={codeValue}
                        onChange={handleChange}
                        className="compailerText"
                        options={{
                            contextmenu: false
                        }}
                    />
                    <div className='RunButton'>
                        <Button type={"button"} text={"Run"} className={"compileButtons"} />
                        <Button type={"button"} text={"Submit"} className={"submitButtons"} />
                    </div>
                    
                </div>
    )
}

export default CodingTest
