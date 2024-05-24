package com.divum.hiring_platform.service;


import com.divum.hiring_platform.dto.*;
import com.divum.hiring_platform.entity.Category;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Service
public interface RoundService {


    ResponseEntity<ResponseDto> getContestantAndEmployeeDetails(String roundId, Integer passMark);

    ResponseEntity<ResponseDto> getRoundsForHRAssign(String request);

    ResponseEntity<ResponseDto> sendEmailToTheEmployeesAboutTheInterview(String roundId, EmployeeIds employeeIds);

    ResponseEntity<ResponseDto> generateInterviewSchedule(String roundId, InterviewScheduleGenerateDTO scheduleGenerateDTO) throws IOException, ParseException;

    ResponseEntity<ResponseDto> updateTheInterviewTiming(String roundId, List<InterviewScheduleResponseDTO> interviewScheduleResponseDTO) throws ParseException;

    ResponseEntity<ResponseDto> getRequest(Long requestId, String choice);

    ResponseEntity<ResponseDto> updateInterview(Long requestId, String decision, EmployeeAndTime employeeAndTime) throws MessagingException, ParseException;

    ResponseEntity<ResponseDto> completeTheInterviewRound(String roundId);

    ResponseEntity<ResponseDto> getAllCategory(String type);

    ResponseEntity<ResponseDto> getLiveRoundsForFilter();

    ResponseEntity<ResponseDto> createCategory(Category category);

}
