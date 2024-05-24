package com.divum.hiring_platform.util;

import com.divum.hiring_platform.dto.PartResponseDto;
import com.divum.hiring_platform.dto.PartWiseResponseDto;
import com.divum.hiring_platform.dto.SingleResponse;
import com.divum.hiring_platform.entity.MCQResult;
import com.divum.hiring_platform.entity.PartWiseResponse;
import com.divum.hiring_platform.entity.UserResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class McqPartWiseResponseService {
    public PartWiseResponse mcqPartWiseResponse(PartResponseDto partResponseDto) {
        PartWiseResponse partWiseResponse = new PartWiseResponse();
        for (PartWiseResponseDto partWiseResponseDto : partResponseDto.getPartWiseResponseDtoList()) {
            partWiseResponse.setCategory(partWiseResponseDto.getCategory());
            List<UserResponse> userResponses = new ArrayList<>();
            for (SingleResponse singleResponse : partWiseResponseDto.getUserResponse()) {
                UserResponse userResponse = new UserResponse();
                List<String> chosenAnswers = new ArrayList<>(singleResponse.getChosenAnswer());
                userResponse.setChosenAnswer(chosenAnswers);
                userResponse.setQuestionId(singleResponse.getQuestionId());
                userResponse.setIsCorrect(Boolean.FALSE);
                userResponse.setDifficulty(singleResponse.getDifficulty());
                userResponses.add(userResponse);
            }
            partWiseResponse.setUserResponse(userResponses);
        }
        return partWiseResponse;
    }
    public List<PartWiseResponse> updateMcqPartWiseResponse(MCQResult mcqResult) {
        List<PartWiseResponse> partWiseResponse=new ArrayList<>();
        for(PartWiseResponse partWiseResponse1: mcqResult.getSavedMcq()){
            PartWiseResponse partWiseResponse2=new PartWiseResponse();
            partWiseResponse2.setCategory(partWiseResponse1.getCategory());
            List<UserResponse>userResponses=new ArrayList<>();
            for (UserResponse singleResponse : partWiseResponse1.getUserResponse()) {
                UserResponse userResponse = new UserResponse();
                List<String> chosenAnswers = new ArrayList<>(singleResponse.getChosenAnswer());
                userResponse.setChosenAnswer(chosenAnswers);
                userResponse.setQuestionId(singleResponse.getQuestionId());
                userResponse.setIsCorrect(Boolean.FALSE);
                userResponse.setDifficulty(singleResponse.getDifficulty());
                userResponses.add(userResponse);
            }
            partWiseResponse2.setUserResponse(userResponses);
            partWiseResponse.add(partWiseResponse2);
        }
        return partWiseResponse;
    }

}
