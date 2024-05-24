package com.divum.hiring_platform.util;

import com.divum.hiring_platform.dto.UserFinalResultDto;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WriteExcelService {
    public ByteArrayInputStream writeExcel(List<UserFinalResultDto> userFinalResultDtoList) throws IOException {
        try(
            Workbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream out = new ByteArrayOutputStream()){
            Sheet sheet = workbook.createSheet("FinalResult");
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("No");
            headerRow.createCell(1).setCellValue("User Name");
            headerRow.createCell(2).setCellValue("Email");
            int i = 3;
            int index1=1;
            for (int j = 0; j < getMcqResultSize(userFinalResultDtoList); j++) {
                headerRow.createCell(i++).setCellValue("MCQ-"+ index1++);
            }
            index1=1;
            for (int j = 0; j < getCodingResultSize(userFinalResultDtoList); j++) {
                headerRow.createCell(i++).setCellValue("CODING-"+ index1++);
            }

            int iterator = 1;
            int index=1;
            for (UserFinalResultDto userFinalResultDto : userFinalResultDtoList) {
                Row dataRow = sheet.createRow(iterator++);
                dataRow.createCell(0).setCellValue(index++);
                dataRow.createCell(1).setCellValue(userFinalResultDto.getUserName());
                dataRow.createCell(2).setCellValue(userFinalResultDto.getEmail());
                int j=3;
                    for (Map.Entry<String,String>e:userFinalResultDto.getMcqMark().entrySet()) {
                        dataRow.createCell(j++).setCellValue(e.getValue());
                    }

                for (Map.Entry<String,String>e:userFinalResultDto.getCodingMark().entrySet()) {
                    dataRow.createCell(j++).setCellValue(e.getValue());
                }



            }
            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());


        }


    }

    private int getCodingResultSize(List<UserFinalResultDto> userFinalResultDtoList) {
        int length=0;
        for(UserFinalResultDto userFinalResultDto:userFinalResultDtoList){
            if(userFinalResultDto.getCodingMark().size()>length)
                length=userFinalResultDto.getCodingMark().size();
        }
        return length;
    }

    private int getMcqResultSize(List<UserFinalResultDto> userFinalResultDtoList) {
        int length=0;
        for(UserFinalResultDto userFinalResultDto:userFinalResultDtoList){
            if(userFinalResultDto.getMcqMark().size()>length)
                length=userFinalResultDto.getMcqMark().size();
        }
        return length;
    }


}
