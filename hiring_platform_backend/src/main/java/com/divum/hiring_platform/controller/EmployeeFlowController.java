package com.divum.hiring_platform.controller;

import com.divum.hiring_platform.api.EmployeeFlowApi;
import com.divum.hiring_platform.dto.FeedbackAndDescriptionDto;
import com.divum.hiring_platform.dto.InterviewRequestDto;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.service.EmployeeFlowService;
import com.divum.hiring_platform.util.enums.InterviewRequestType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EmployeeFlowController implements EmployeeFlowApi {

    private final EmployeeFlowService employeeFlowService;


    @Override
    public ResponseEntity<ResponseDto> getAssignedStudents(Long employeeId, String contestId) {
        return employeeFlowService.getAssignedStudents(employeeId, contestId);
    }

    @Override
    public ResponseEntity<ResponseDto> requestReschedule(String interviewId, InterviewRequestType interviewRequestType, InterviewRequestDto interviewRequestDto) {
        return employeeFlowService.requestReshedule(interviewId, interviewRequestType, interviewRequestDto);
    }


//    @Override
//    public ResponseEntity<ResponseDto> getAllInterviews(String employeeId, String contestId) {
//        return employeeFlowService.getAllInterviews(employeeId, contestId);
//    }


    @Override
    public ResponseEntity<ResponseDto> getAllContests(String employeeId) {
        return employeeFlowService.getAllContests(employeeId);
    }
    @Override
    public ResponseEntity<ResponseDto> interviewDetails(String interviewId) {
        return employeeFlowService.interviewDetails(interviewId);
    }

    @Override
    public ResponseEntity<ResponseDto> feedback(String interviewId, FeedbackAndDescriptionDto feedbackAndDescriptionDto) {
        return employeeFlowService.feedback(interviewId, feedbackAndDescriptionDto);
    }

    @Override
    public ResponseEntity<ResponseDto> contestLog(String employeeId) {
        return employeeFlowService.contestLog(employeeId);
    }


}
