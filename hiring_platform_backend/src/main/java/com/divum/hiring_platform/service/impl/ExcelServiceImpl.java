package com.divum.hiring_platform.service.impl;

import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.MultipleChoiceQuestion;
import com.divum.hiring_platform.exception.InvalidDataException;
import com.divum.hiring_platform.repository.MCQQuestionRepository;
import com.divum.hiring_platform.repository.service.ContestRepositoryService;
import com.divum.hiring_platform.service.CodingQnService;
import com.divum.hiring_platform.service.ExcelService;
import com.divum.hiring_platform.strings.Strings;
import com.divum.hiring_platform.util.McqExcelUploadService;
import com.divum.hiring_platform.util.UserExcelUploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExcelServiceImpl implements ExcelService {
    private final MCQQuestionRepository mcqQuestionRepository;
    private final McqExcelUploadService mcqExcelUploadService;
    private final UserExcelUploadService userExcelUploadService;
    private final CodingQnService codingQnService;
    private final ContestRepositoryService contestRepositoryService;


    @Override
    public ResponseEntity<ResponseDto> excelUpload(MultipartFile file, String uploadType, String contestId) throws IOException {
        if (uploadType.equals(Strings.MCQ)) {
            if (mcqExcelUploadService.isValidExcelFile(file)) {
                List<MultipleChoiceQuestion> mcqQuestions = mcqExcelUploadService.getMcqQuestions(file.getInputStream());
                for (MultipleChoiceQuestion mcq : mcqQuestions) {
                    mcq.setQuestionId(UUID.randomUUID().toString());
                    mcqQuestionRepository.save(mcq);
                }
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("questions added", null));

            } else {
                throw new InvalidDataException("Excel not valid");
            }
        } else if (uploadType.equals("USER") && (userExcelUploadService.isValidExcelFile(file))) {
            Optional<Contest> contest;
            try {
                contest = contestRepositoryService.findById(contestId);
                if (contest.isEmpty()) {
                    throw new ResourceNotFoundException("Contest not found");
                }
            } catch (ResourceNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(e.getMessage(), null));
            }

            List<String> errorEmail = userExcelUploadService.addUser(file.getInputStream(), contest.get());
            if (errorEmail != null) {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Users assigned to the contests", null));
            } else {
                return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("The following emails are already assigned to an upcoming / current contest", null));
            }
        } else if (uploadType.equals("CODING_QUESTION")) {
            return codingQnService.createCodingQuestion(null, file);

        }
        return null;
    }
}
