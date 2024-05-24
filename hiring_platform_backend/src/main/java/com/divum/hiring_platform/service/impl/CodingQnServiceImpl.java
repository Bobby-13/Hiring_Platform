package com.divum.hiring_platform.service.impl;

import com.divum.hiring_platform.dto.CreateCodingQnDto;
import com.divum.hiring_platform.dto.GetCodingQnDto;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.entity.*;
import com.divum.hiring_platform.repository.service.CategoryRepositoryService;
import com.divum.hiring_platform.repository.service.CodingQuestionRepositoryService;
import com.divum.hiring_platform.service.CodingQnService;
import com.divum.hiring_platform.strings.Strings;
import com.divum.hiring_platform.util.CodingExcelUploadService;
import com.divum.hiring_platform.util.enums.Difficulty;
import com.divum.hiring_platform.util.enums.QuestionCategory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CodingQnServiceImpl implements CodingQnService {
    private final ModelMapper modelMapper;
    private final CodingExcelUploadService excelCoding;
    private final CategoryRepositoryService categoryRepositoryService;
    private final CodingQuestionRepositoryService codingQuestionRepositoryService;

    @Override
    public ResponseEntity<ResponseDto> createCodingQuestion(CreateCodingQnDto createCodingQnDto, MultipartFile multipartFile) {
        if(createCodingQnDto!=null){
            try{
                CodingQuestion codingQuestion=modelMapper.map(createCodingQnDto,CodingQuestion.class);
                Category category = categoryRepositoryService.findByQuestionCategory(QuestionCategory.valueOf(createCodingQnDto.getCategory()));
                codingQuestion.setCategory(category);
                setCodingQnIdLists(codingQuestion);
                codingQuestionRepositoryService.save(codingQuestion);
                return ResponseEntity.ok(new ResponseDto(Strings.CREATE_CODING_QUESTION_SUCCESS,codingQuestion));
            }catch (Exception e){
                return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage(),null));
            }
        }
        if (!multipartFile.isEmpty()) {
            if (excelCoding.isValidExcelFile(multipartFile)) {
                try {
                    List<CodingQuestion> codeQuestions = excelCoding.getCodeQuestions(multipartFile.getInputStream());
                    codingQuestionRepositoryService.saveAll(codeQuestions);
                    return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(Strings.CREATE_CODING_QUESTIONS_SUCCESS, null));
                } catch (Exception e) {
                    return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage(), null));
                }
            } else {
                return ResponseEntity.badRequest().body(new ResponseDto(Strings.FILE_NOT_VALID, null));
            }
        }
        return ResponseEntity.badRequest().body(new ResponseDto(Strings.EMPTY_INPUT, null));
    }

    @Override
    public ResponseEntity<ResponseDto> getCodingQuestion(Long id) {
        try {
            Optional<CodingQuestion> codingQuestion = codingQuestionRepositoryService.findById(id);
            if (codingQuestion.isPresent()) {
                return ResponseEntity.ok(new ResponseDto(Strings.FETCH_BY_CODING_QUESTION_ID_SUCCESS, codingQuestion.get()));
            }
            return ResponseEntity.badRequest().body(new ResponseDto(Strings.NOT_FOUND_CODING_QUESTION_ID+ id, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage(), null));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> updateCodingQuestion(Long id, CreateCodingQnDto createCodingQnDto) {
        try {
            if (createCodingQnDto != null && codingQuestionRepositoryService.findById(id).isPresent()) {
                CodingQuestion codingQuestion = modelMapper.map(createCodingQnDto, CodingQuestion.class);
                codingQuestion.setQuestionId(id);
                Category category = categoryRepositoryService.findByQuestionCategory(QuestionCategory.valueOf(createCodingQnDto.getCategory()));
                codingQuestion.setCategory(category);
                setCodingQnIdLists(codingQuestion);
                codingQuestionRepositoryService.save(codingQuestion);
                return ResponseEntity.ok(new ResponseDto(Strings.UPDATE_CODING_QUESTION_SUCCESS, null));
            }
            return ResponseEntity.badRequest().body(new ResponseDto(Strings.NOT_FOUND_CODING_QUESTION_ID, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage(), null));
        }
    }

    private void setCodingQnIdLists(CodingQuestion codingQuestion) {
        List<CodingImageUrl> imageUrls = codingQuestion.getImageUrl();
        for (CodingImageUrl codingImageUrl : imageUrls) {
            codingImageUrl.setCodingQuestion(codingQuestion);
        }
        List<Cases> casesList = codingQuestion.getCasesList();
        for (Cases cases : casesList) {
            cases.setCodingQuestion(codingQuestion);
        }
        List<StaticCode> staticCodes = codingQuestion.getStaticCodes();
        for (StaticCode staticCode : staticCodes) {
            staticCode.setCodingQuestion(codingQuestion);
        }
        List<FunctionCode> functionCodes = codingQuestion.getFunctionCodes();
        for (FunctionCode functionCode : functionCodes) {
            functionCode.setCodingQuestion(codingQuestion);
        }
    }

    @Override
    public ResponseEntity<ResponseDto> deleteCodingQuestion(Long id) {
        try {
            if (codingQuestionRepositoryService.findById(id).isPresent()) {
                codingQuestionRepositoryService.deleteById(id);
                return ResponseEntity.ok(new ResponseDto(Strings.DELETE_CODING_QUESTION_SUCCESS, null));
            }
            return ResponseEntity.badRequest().body(new ResponseDto(Strings.NOT_FOUND_CODING_QUESTION_ID, null));
        } catch (Exception e) {
            return ResponseEntity.ok(new ResponseDto(e.getMessage(), null));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> deleteAllCodingQuestion() {
        try {
            codingQuestionRepositoryService.deleteAll();
            return ResponseEntity.ok(new ResponseDto(Strings.DELETE_CODING_QUESTION_SUCCESS, null));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage(), null));
        }
    }

    @Override
    public ResponseEntity<ResponseDto> getCodingQuestions(int page, int size, List<QuestionCategory> category, List<Difficulty> difficulty) {
        try {
            Pageable pageRequest = PageRequest.of(page, size);
            Page<GetCodingQnDto> codingQuestionsPage;

            if (category != null && difficulty != null) {
                codingQuestionsPage = codingQuestionRepositoryService.findCodingQuestionsWithCategoryAndDifficulty(category, difficulty, pageRequest);
            } else if (category != null) {
                codingQuestionsPage = codingQuestionRepositoryService.findCodingQuestionsByCategory(category, pageRequest);
            } else if (difficulty != null) {
                codingQuestionsPage = codingQuestionRepositoryService.findCodingQuestionsByDifficulty(difficulty, pageRequest);
            } else {
                codingQuestionsPage = codingQuestionRepositoryService.findAllQn(pageRequest);
            }

            List<GetCodingQnDto> codingQuestions = codingQuestionsPage.getContent();

            Map<String, Object> response = new HashMap<>();
            response.put(Strings.QUESTIONS_FOR_THIS_PAGE, codingQuestions);
            response.put(Strings.NUM_OF_PAGES, codingQuestionsPage.getTotalElements());

            return ResponseEntity.ok(new ResponseDto("", response));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage(), null));
        }
    }
}