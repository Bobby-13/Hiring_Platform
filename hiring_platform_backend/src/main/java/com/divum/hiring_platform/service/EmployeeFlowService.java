package com.divum.hiring_platform.service;

import com.divum.hiring_platform.dto.FeedBackAndResultDto;
import com.divum.hiring_platform.dto.FeedbackAndDescriptionDto;
import com.divum.hiring_platform.dto.InterviewRequestDto;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.util.enums.InterviewRequestType;
import org.springframework.http.ResponseEntity;

public interface EmployeeFlowService {
    ResponseEntity<ResponseDto> addFeedBackAndResult(String interviewId, FeedBackAndResultDto feedBackAndResultDto);

    ResponseEntity<ResponseDto> getAssignedStudents(Long employeeId, String contestId);

    ResponseEntity<ResponseDto> requestReshedule(String interviewId, InterviewRequestType interviewRequestType, InterviewRequestDto interviewRequestDto);

//    ResponseEntity<ResponseDto> getAllInterviews(String employeeId, String contestId);

    ResponseEntity<ResponseDto> getAllContests(String employeeId);

    ResponseEntity<ResponseDto> interviewDetails(String interviewId);

    ResponseEntity<ResponseDto> feedback(String interviewId, FeedbackAndDescriptionDto feedbackAndDescriptionDto);


    ResponseEntity<ResponseDto> contestLog(String employeeId);
}
