import React from "react";
import "/home/divum/hiring_platform/hiring-platform-frontend/src/components/ResumeUpload.scss";
import { useDropzone } from 'react-dropzone';
import UploadFileIcon from '@mui/icons-material/UploadFile';

function ResumeUpload() {
    const currentYear = new Date().getFullYear();
    const years = Array.from({ length: 20 }, (_, index) => currentYear - index);

    const { getRootProps, getInputProps } = useDropzone({ accept: 'application/pdf' }); // Adjust the accepted file type accordingly

    return (
        <div className="maincontainer">
            RESUME UPLOAD
            <div className="name-container">
                DEPARTMENT:
                <div>
                    <input type="text"  className="input" />
                </div>
            </div>
            <div className="year-container">
                <p>YEAR:</p>
                <div>
                    <select className="year-dropdown">
                        {years.map((year) => (
                            <option key={year} value={year}>
                                {year}
                            </option>
                        ))}
                    </select>
                </div>
            </div>
            <div className="resume-container">
                <p>UPLOAD RESUME:</p>
                <div {...getRootProps()} className="upload-container">
                    <input {...getInputProps()} />
                    <UploadFileIcon className="upload-icon"/>
                    <p>Drag & drop or click here to select a PDF file</p>
                </div>
            </div>
            <button className="submit">SUBMIT</button>
        </div>
    );
}

export default ResumeUpload;
