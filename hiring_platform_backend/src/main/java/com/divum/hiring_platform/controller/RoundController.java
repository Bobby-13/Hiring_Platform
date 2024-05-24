package com.divum.hiring_platform.controller;

import com.divum.hiring_platform.api.RoundApi;
import com.divum.hiring_platform.dto.*;
import com.divum.hiring_platform.entity.Category;
import com.divum.hiring_platform.service.RoundService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RoundController implements RoundApi {

    private final RoundService roundService;


    @Override
    public ResponseEntity<ResponseDto> getContestantAndEmployeeDetails(String roundId, Integer passMark) {
        return roundService.getContestantAndEmployeeDetails(roundId, passMark);
    }

    @Override
    public ResponseEntity<ResponseDto> getRoundsForHRAssign(String request) {
        return roundService.getRoundsForHRAssign(request);
    }

    @Override
    public ResponseEntity<ResponseDto> sendEmailToTheEmployeesAboutTheInterview(String roundId, EmployeeIds employeeIds) {
        return roundService.sendEmailToTheEmployeesAboutTheInterview(roundId, employeeIds);
    }

    @Override
    public ResponseEntity<ResponseDto> generateInterviewSchedule(String roundId, InterviewScheduleGenerateDTO scheduleGenerateDTO) throws IOException, ParseException {
        return roundService.generateInterviewSchedule(roundId, scheduleGenerateDTO);
    }

    @Override
    public ResponseEntity<ResponseDto> updateTheInterviewTiming(String roundId, List<InterviewScheduleResponseDTO> interviewScheduleResponseDTO) throws ParseException {
        return roundService.updateTheInterviewTiming(roundId, interviewScheduleResponseDTO);
    }

    @Override
    public ResponseEntity<ResponseDto> getRequest(Long requestId,String choice) {
        return roundService.getRequest(requestId, choice);
    }

    @Override
    public ResponseEntity<ResponseDto> updateInterview(Long requestId, String decision, EmployeeAndTime employeeAndTime) throws MessagingException, ParseException {
        return roundService.updateInterview(requestId, decision, employeeAndTime);
    }

    @Override
    public ResponseEntity<ResponseDto> completeTheInterviewRound(String roundId) {
        return roundService.completeTheInterviewRound(roundId);
    }
    @Override
    public ResponseEntity<ResponseDto> getLiveRounds() {
        return roundService.getLiveRoundsForFilter();
    }
    @Override
    public ResponseEntity<ResponseDto> getAllCategory(String type) {
        return roundService.getAllCategory(type);
    }

    @Override
    public ResponseEntity<ResponseDto> createCategory(Category category) {
        return roundService.createCategory(category);
    }


}
