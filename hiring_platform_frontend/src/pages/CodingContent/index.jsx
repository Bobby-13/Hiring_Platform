import React, { useEffect,useState } from 'react';
import { Rounds2 } from '../../helpers/RoundList';
import '../CodingContent/CodingContest.scss';
import InputTag from '../../components/InputTag';
import WeightageCard from '../../components/WeightageCard';
import { Percentageinfo } from '../../components/WeightageCard/PercentagetimeInfo';
import Button from '../../components/Button';
import axios from 'axios';

const CodingContent = ({setTotalCountDisplay, totalCountDisplay,setCategoryTitle,getinfovalue,setPopup}) => {
    const [codeactive, setCodeactive] = useState({});
  // const [addata,setAddata]=useState()
    
  const [codinground, setCodinground] = useState([]);
  const[Codingpercentage,setCodingpercentage]=useState([]);

    const toggleCodeactive = (type, checked) => {
        setCodeactive(prevState => ({
            ...prevState,
            [type]: checked,
        }));
    };

    
    const [Checkboxcoding,setCheckboxcoding]=useState()
    
    useEffect(() => {
      const fetchData = async () => {
        try {
              const response = await axios.get(`http://localhost:8081/api/v1/contest/category?type=${"CODING"}`);
              setCheckboxcoding(response.data.object);
              const headings = response.data.object.map(item => item.heading);
              const codeActiveUpdate = {};
              headings.forEach(heading => {
                codeActiveUpdate[heading] = false;
              });
              setCodeactive(prevState => ({
                ...prevState,
                ...codeActiveUpdate
              }));
          } catch (error) {
              console.error("Error fetching data:", error);
          }
        };
        
        fetchData();
      }, []);
      
      const handleCodeCheckboxChange = (event) => {
          const { checked, value } = event.target;
          toggleCodeactive(value, checked);
      };
  


    const addRoundData = (roundType, partData, assignedTime, sumOfWeightages, title) => {
        setTotalCountDisplay(sumOfWeightages)
        setCategoryTitle(title)
        console.log(partData?.category?.categoryId)
        setCodinground(previous => {
            const newArray = [...previous];
            newArray[partData?.category?.categoryId] = partData;
            return newArray;
        });
    };

    const Codingpercentages =(percentagevalue)=>{
       setCodingpercentage(percentagevalue);
      }
      
    console.log(Checkboxcoding)
      const updatevalue=()=>{
        getinfovalue(codinground,"CODING",Codingpercentage)
        setPopup(false) 
      }
    
    return (
        <div>
            <div className="CodingContent">
                {Checkboxcoding?.map((item, index) => (
                    <InputTag
                        type="checkbox"
                        Process={item.heading}
                        key={index}
                        value={item.heading}
                        onChange={handleCodeCheckboxChange}
                        checkboxHeading={item.heading}
                    />
                ))}
            </div>
            <div style={{ display: "flex" }}>
                {Object.entries(codeactive).map(([key, value]) => (
                    <WeightageCard
                        key={key}
                        active={value}
                        title={key}
                        add={addRoundData}
                        roundNumber={2} 
                        categoryId={Checkboxcoding}
                        />
                      ))}
            </div>
            <Percentageinfo percentage={Codingpercentages} />
            <Button
          text={"Submit"}
          className={"CreateContestSubmitbutton"}
          onClick={()=>{updatevalue()}}
        />
        </div>
    );
};

export default CodingContent;
