package com.divum.hiring_platform.util;

import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.User;
import com.divum.hiring_platform.repository.UserRepository;
import com.divum.hiring_platform.strings.Strings;
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
public class UserExcelUploadService {

    private final ContestRelatedService contestRelatedService;
    private final UserRepository userRepository;

    public boolean isValidExcelFile(MultipartFile file) {
        return Strings.EXCEL_FILE_FORMAT.equals(file.getContentType());
    }

    public List<String> addUser(InputStream inputStream, Contest contest) throws IOException {

        List<User> users = new ArrayList<>();
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
        XSSFSheet sheet = workbook.getSheet(Strings.USER1);
        int rowIndex = 0;
        List<String> errorList = new ArrayList<>();
        for (Row row : sheet) {
            boolean isAlreadyAssignedUser = false;
            if (rowIndex > 101) {
                break;
            }
            if (row.getRowNum() != 0) {

                Iterator<Cell> cellIterator = row.iterator();
                int cellIndex = 0;


                User user = new User();
                while (cellIterator.hasNext()) {

                    Cell cell = cellIterator.next();

                    switch (cellIndex) {

                        case 0:
                            String name = cell.toString();
                            user.setName(name);
                            break;
                        case 1:
                            String email = cell.toString();
                            user.setEmail(email);
                            if (contestRelatedService.isExistingUser(email)) {
                                contestRelatedService.assignUserToTheContest(email, contest, errorList);
                                isAlreadyAssignedUser = true;
                            }
                            break;
                        case 2:
                            String college = cell.toString();
                            user.setCollegeName(college);
                            break;

                        default:
                            break;
                    }
                    cellIndex++;
                }
                if (!isAlreadyAssignedUser) {
                    users.add(user);
                }
                rowIndex++;
            }
        }
        userRepository.saveAll(users);
        return errorList;
    }
}

