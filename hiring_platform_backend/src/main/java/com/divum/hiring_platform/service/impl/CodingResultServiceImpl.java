package com.divum.hiring_platform.service.impl;

import com.divum.hiring_platform.dto.CodingResultDTO;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.entity.CodingQuestion;
import com.divum.hiring_platform.entity.CodingQuestionObject;
import com.divum.hiring_platform.entity.CodingResult;
import com.divum.hiring_platform.repository.CodingResultRepository;
import com.divum.hiring_platform.repository.service.CodingQuestionRepositoryService;
import com.divum.hiring_platform.service.CodingResultService;
import com.divum.hiring_platform.strings.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CodingResultServiceImpl implements CodingResultService {

    private final CodingResultRepository codingResultRepository;
    private final CodingQuestionRepositoryService codingQuestionRepositoryService;


    @Override
    public ResponseEntity<ResponseDto> fetch(String id) {
        Optional<CodingResult> codingResult = codingResultRepository.findById(id);
        if (codingResult.isEmpty()) {
            throw new ResourceNotFoundException(Strings.DATA_NOT_FOUND);
        }
        List<CodingQuestionObject> codingQuestionObjectList=codingResult.get().getQuestion();
        List<CodingResultDTO> codingResultDTOS=new ArrayList<>();
        for(CodingQuestionObject codingQuestionObject:codingQuestionObjectList) {
            Optional<CodingQuestion> codingQuestion=codingQuestionRepositoryService.findById(codingQuestionObject.getQuestionId());
            CodingResultDTO codingResultDTO = CodingResultDTO.builder()
                    .question(codingQuestion.isPresent()?codingQuestion.get().getQuestion():String.valueOf(codingQuestionObject.getQuestionId()))
                    .code(codingQuestionObject.getCode())
                    .passCount(codingQuestionObject.getPassCount())
                    .testCaseCount(codingQuestionObject.getTestCaseCount())
                    .build();
            codingResultDTOS.add(codingResultDTO);
        }

        ResponseDto responseDto = new ResponseDto();
        responseDto.setMessage(Strings.CODING_RESULTS);
        responseDto.setObject(codingResultDTOS);

        return ResponseEntity.ok(responseDto);
    }
}