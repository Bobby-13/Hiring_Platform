package com.divum.hiring_platform.util;

import com.divum.hiring_platform.entity.Category;
import com.divum.hiring_platform.entity.MultipleChoiceQuestion;
import com.divum.hiring_platform.entity.Options;
import com.divum.hiring_platform.repository.CategoryRepository;
import com.divum.hiring_platform.strings.Strings;
import com.divum.hiring_platform.util.enums.Difficulty;
import com.divum.hiring_platform.util.enums.QuestionCategory;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
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
public class McqExcelUploadService {
    private final CategoryRepository categoryRepository;

    public boolean isValidExcelFile(MultipartFile file) {
        return Strings.EXCEL_FILE_FORMAT.equals(file.getContentType());
    }

    public List<MultipleChoiceQuestion> getMcqQuestions(InputStream inputStream) throws IOException {
        List<MultipleChoiceQuestion> mcqList = new ArrayList<>();
        try (XSSFWorkbook workbook = new XSSFWorkbook(inputStream)) {
            XSSFSheet sheet = workbook.getSheet("McqQuestion");
            if (sheet == null) {
                throw new IllegalArgumentException(Strings.SHEET_NOT_FOUND);
            }
            StringBuilder check = new StringBuilder();

            for (Row row : sheet) {


                if (row.getRowNum() != 0) {
                    check.setLength(0);
                    MultipleChoiceQuestion mcq = new MultipleChoiceQuestion();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        switch (cell.getColumnIndex()) {
                            case 0:
                                mcq.setQuestionId(cell.toString());
                                check.append(mcq.getQuestionId());
                                break;
                            case 1:
                                mcq.setQuestion(cell.toString());
                                check.append(mcq.getQuestion());
                                break;

                            case 2:
                                String categoryStr = getStringCellValue(cell);
                                Category category = categoryRepository.findByQuestionCategory(QuestionCategory.valueOf(categoryStr));
                                mcq.setCategory(category);
                                check.append(mcq.getCategory());
                                break;

                            case 3:
                                String difficultyStr = getStringCellValue(cell);
                                mcq.setDifficulty(Difficulty.valueOf(difficultyStr));
                                check.append(mcq.getDifficulty());
                                break;

                            case 4:
                                List<Options> optionsList = parseOptions(cell, cellIterator, mcq);
                                mcq.setOptions(optionsList);
                                check.append(mcq.getOptions());
                                break;
                            default:
                                break;
                        }
                    }
                    if ((new String(check)).isEmpty()) {
                        break;
                    }
                    mcqList.add(mcq);
                }
            }
        }
        return mcqList;
    }

    private String getStringCellValue(Cell cell) {

        if (cell.getCellType() == CellType.STRING)

            return cell.getStringCellValue();

        if (cell.getCellType() == CellType.NUMERIC)
            return cell.getNumericCellValue() + "";

        return "";
    }

    private List<Options> parseOptions(Cell cell, Iterator<Cell> cellIterator, MultipleChoiceQuestion mcq) {
        List<Options> optionsList = new ArrayList<>();

        String optionValue1 = getStringCellValue(cell);
        Options option1 = new Options();
        option1.setMultipleChoiceQuestion(mcq);
        option1.setOption(optionValue1);
        option1.setCorrect(isCorrectOption(cell));
        optionsList.add(option1);

        while (cellIterator.hasNext()) {

            Cell optionCell = cellIterator.next();
            String optionValue = getStringCellValue(optionCell);
            if (!optionValue.isEmpty()) {
                Options option = new Options();
                option.setMultipleChoiceQuestion(mcq);
                option.setOption(optionValue);
                option.setCorrect(isCorrectOption(optionCell));
                optionsList.add(option);
            }
        }
        return optionsList;
    }

    private boolean isCorrectOption(Cell cell) {
        CellStyle cellStyle = cell.getCellStyle();

        return (cellStyle.getFillForegroundColorColor() + "").equals("org.apache.poi.xssf.usermodel.XSSFColor@1fba2ba4");
    }

}
