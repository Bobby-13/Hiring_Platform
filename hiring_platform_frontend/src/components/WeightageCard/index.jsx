import React, { useEffect, useState } from 'react';
import "../../components/WeightageCard/WeightageCard.scss";
import { Weightage } from '../../helpers/RoundList';
import InputTag from '../InputTag';

const WeightageCard = ({ active, title, add, roundNumber,categoryId }) => {
    const [weightages, setWeightages] = useState([]);      
    const [duration, setDuration] = useState(0);
    const sumOfWeightages = Object.values(weightages).filter(value => !isNaN(value)).reduce((acc, curr) => acc + curr, 0);
      const handleWeightageChange = (item, value, title) => {
        console.log(title,"title")
        const parsedValue = value.trim() !== '' ? parseInt(value) : 0;
        const filteredCategory = categoryId.filter(item => item.heading === title);
        console.log(filteredCategory,"filter")
        const id = filteredCategory.length > 0 ? filteredCategory.map(item => item.category.categoryId)[0] : null; 
        console.log(id,"id")
        setWeightages(prevWeightages => ({
            ...prevWeightages,
            ...prevWeightages[title],
            [item]: parsedValue,
            category: {
                categoryId:id,
                category: title==="Technical"?"TECHNICAL_MCQ":title==="Verbal"?"VERBAL_MCQ":title==="Logical"?"LOGICAL_MCQ":title==="Aptitude"?"APTITUDE_MCQ":title==="Pattern"?"PATTERN_CODING":title==="Strings"?"STRINGS_CODING":title==="Mathematics"?"MATHEMATICS_CODING":title==="Data"?"DATA_STRUCTURE_CODING":"ALGORITHMS_CODING"
            }
        }));   
    };

    // console.log(weightages,"weight")
    useEffect(()=>{
       add(roundNumber, weightages, duration,sumOfWeightages,title);
    },[sumOfWeightages])

    return (
        <div className='WeightageCard' style={{ height: !active && '0px', overflow: "hidden", padding: !active && "0px", border: !active && "none" }}>
            <h3>{title}</h3>
            {Weightage.map(item => (
                <div className='Weightage-content' key={item}>
                    <p className='weighttitle'>{item}</p>
                    <div className='incdecbutton'>
                        <InputTag type="text" className='weightagecount'  onChange={(e) => handleWeightageChange(item, e.target.value,title)} />
                    </div>
                </div>
            ))}
            <div className='totalvalues'>
                <p className='weighttitle'>Total</p>
                <p style={{ marginLeft: "3.2rem" , marginTop:"0.5rem" }}>{sumOfWeightages}</p>
            </div>
            <div className='totalvalues'>
                <p className='weighttitle'>Duration</p>
                <InputTag type={"text"} className={"durationinfo"} placeholder={"Enter time duration"}  onChange={(e) => handleWeightageChange("assignedTime", e.target.value,title)} />
            </div>
        </div>
    );
};

export default WeightageCard;
