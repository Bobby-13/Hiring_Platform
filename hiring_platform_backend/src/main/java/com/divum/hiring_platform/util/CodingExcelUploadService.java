package com.divum.hiring_platform.util;

import com.divum.hiring_platform.entity.*;
import com.divum.hiring_platform.repository.service.CategoryRepositoryService;
import com.divum.hiring_platform.strings.Strings;
import com.divum.hiring_platform.util.enums.CasesType;
import com.divum.hiring_platform.util.enums.CodeLanguage;
import com.divum.hiring_platform.util.enums.Difficulty;
import com.divum.hiring_platform.util.enums.QuestionCategory;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CodingExcelUploadService {

    private final CategoryRepositoryService categoryRepositoryService;

    public boolean isValidExcelFile(MultipartFile file) {
        return Strings.EXCEL_FILE_FORMAT.equals(file.getContentType());
    }
    public List<CodingQuestion> getCodeQuestions(InputStream inputStream) throws IOException {
        List<CodingQuestion> codingQuestions = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            try{
            codingQuestions=readSheet(workbook.getSheetAt(i));
            }catch (NullPointerException e){throw new IOException(e.getMessage()+"sheet");}
        }
        return codingQuestions;
    }

    private List<CodingQuestion> readSheet(XSSFSheet sheet){
        List<CodingQuestion> codingQuestions=new ArrayList<>();
        Iterator<Row> rowIterator = sheet.iterator();
        while (rowIterator.hasNext()) {
            CodingQuestion codingQuestion=new CodingQuestion();
            Row row = rowIterator.next();
            try {
                if (row.getRowNum() != 0) {
                    codingQuestion = readRow(row);
                }
            }catch(Exception e){
                continue;
            }
            if (row.getRowNum() != 0) {
                codingQuestions.add(codingQuestion);
            }
        }
        return codingQuestions;
    }

    private CodingQuestion readRow(Row row){
        CodingQuestion codingQuestion=new CodingQuestion();
        try {
            codingQuestion.setQuestion(String.valueOf(row.getCell(0)));
        }catch (Exception e){
            throw new IllegalArgumentException(Strings.NULL_QUESTION+"qn");
        }
        try {
            codingQuestion.setConstraints(String.valueOf(row.getCell(1)));
        }catch (Exception e){
            throw new IllegalArgumentException(Strings.NULL_CONSTRAINTS+"cns");
        }

        try {
            QuestionCategory categoryString = QuestionCategory.valueOf(row.getCell(2).getStringCellValue());
            Category category = categoryRepositoryService.findByQuestionCategory(categoryString);
            codingQuestion.setCategory(category);
        } catch (Exception e) {
            throw new IllegalArgumentException(Strings.INVALID_CATEGORY+ row.getCell(2).getStringCellValue());
        }

        if (!row.getCell(2).getStringCellValue().endsWith("_CODING"))
            throw new IllegalArgumentException(Strings.INVALID_CODING_CATEGORY);

        try {
            codingQuestion.setDifficulty(Difficulty.valueOf(String.valueOf(row.getCell(3))));
        } catch (Exception e) {
            throw new IllegalArgumentException(Strings.INVALID_DIFFICULTY+ row.getCell(3).getStringCellValue());
        }

        try {
            List<StaticCode> staticCodes = new ArrayList<>();
            List<FunctionCode> functionCodes = new ArrayList<>();
            for (int l = 0; l < 4; l++) {
                StaticCode staticCode = new StaticCode();
                staticCode.setCodeLanguage(CodeLanguage.valueOf(String.valueOf(row.getCell(4 + l * 4))));
                staticCode.setCode(String.valueOf(row.getCell(5 + l * 4)));
                staticCode.setCodingQuestion(codingQuestion);
                staticCodes.add(staticCode);

                FunctionCode functionCode = new FunctionCode();
                functionCode.setCodeLanguage(CodeLanguage.valueOf(String.valueOf(row.getCell(6 + l * 4))));
                functionCode.setCode(String.valueOf(row.getCell(7 + l * 4)));
                functionCode.setCodingQuestion(codingQuestion);
                functionCodes.add(functionCode);
            }
            codingQuestion.setStaticCodes(staticCodes);
            codingQuestion.setFunctionCodes(functionCodes);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage()+"static code function code");
        }

        List<Cases> casesList = new ArrayList<>();
        int numberOfCases = getIntValue(row.getCell(20));
            for (int k = 0; k < numberOfCases; k++) {
                Cases cases = new Cases();
                try {
                    cases.setInput(String.valueOf((row.getCell(21 + k * 3))));
                    cases.setOutput(String.valueOf((row.getCell(22 + k * 3))));
                    cases.setCasesType(CasesType.valueOf(String.valueOf(row.getCell(23 + k * 3))));
                    cases.setCodingQuestion(codingQuestion);
                }catch (NullPointerException e) {
                    continue;
                }catch (Exception e1){
                    throw new IllegalArgumentException(e1.getMessage()+"cases");
                }
                casesList.add(cases);
        }
        codingQuestion.setCasesList(casesList);
        return codingQuestion;
    }

    private int getIntValue(Cell cell) {
        return cell != null ? (int) cell.getNumericCellValue() : 0;
    }
}