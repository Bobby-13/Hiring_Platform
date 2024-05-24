package com.divum.hiring_platform.controller;


import com.divum.hiring_platform.api.ContestApi;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.dto.UserDto;
import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.service.ContestService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ContestController implements ContestApi {

    private final ContestService contestService;

    @Override
    public ResponseEntity<ResponseDto> createContest(Contest contest) {
        return contestService.createContest(contest);
    }

    @Override
    public ResponseEntity<ResponseDto> assignQuestionToTheContest(String contestId, Boolean reassign) throws MessagingException {
        return contestService.assignQuestion(contestId, reassign);
    }

    @Override
    public ResponseEntity<ResponseDto> updateContest(String contestId, Contest contest) {
        return contestService.updateContest(contestId, contest);
    }

    @Override
    public ResponseEntity<ResponseDto> assignUsers(String contestId, List<UserDto> users) throws MessagingException {
        return contestService.assignUser(contestId, users);
    }

    @Override
    public ResponseEntity<ResponseDto> deleteContest(String contestId) {
        return contestService.deleteContest(contestId);
    }

    @Override
    public ResponseEntity<ResponseDto> getAllContest(String required) {
        return contestService.getAllContest(required);
    }

    @Override
    public ResponseEntity<ResponseDto> getContest(String contestId, String required) {
        return contestService.getContest(contestId, required);
    }


    @Override
    public ResponseEntity<ResponseDto> getUsers(String roundId, Integer passmark) {
        return contestService.getUsers(roundId, passmark);
    }

    @Override
    public ResponseEntity<ResponseDto> finalResult(String contestId, String roundId, String finalResult) {
        return contestService.finalResult(contestId, roundId, finalResult);
    }

    @Override
    public ResponseEntity<ResponseDto> adminHomePage() {
        return contestService.adminHomePage();
    }


}
