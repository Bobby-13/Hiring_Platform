package com.divum.hiring_platform.api;

import com.divum.hiring_platform.dto.FeedbackAndDescriptionDto;
import com.divum.hiring_platform.dto.InterviewRequestDto;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.util.enums.InterviewRequestType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/employee")
public interface EmployeeFlowApi {


    @GetMapping("/{employeeId}/interviews/{contestId}")
    ResponseEntity<ResponseDto> getAssignedStudents(@PathVariable Long employeeId, @PathVariable String contestId);

    @PostMapping("/{interviewId}/interviewRequestType/{interviewRequestType}")
    ResponseEntity<ResponseDto> requestReschedule(@PathVariable String interviewId, @PathVariable InterviewRequestType interviewRequestType, @RequestBody InterviewRequestDto interviewRequestDto);

    @GetMapping("/contests/{employeeId}")
    ResponseEntity<ResponseDto> getAllContests(@PathVariable String employeeId);


    @GetMapping("/interview/{interviewId}")
    ResponseEntity<ResponseDto> interviewDetails(@PathVariable String interviewId);

    @PutMapping("interview/{interviewId}")
    ResponseEntity<ResponseDto> feedback(@PathVariable String interviewId, @RequestBody FeedbackAndDescriptionDto feedbackAndDescriptionDto);

    @GetMapping("/contests/log/{employeeId}")
    ResponseEntity<ResponseDto> contestLog(@PathVariable String employeeId);
}
