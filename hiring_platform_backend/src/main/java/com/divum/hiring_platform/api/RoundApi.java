package com.divum.hiring_platform.api;


import com.divum.hiring_platform.dto.*;
import com.divum.hiring_platform.entity.Category;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@RequestMapping("/api/v1/contest")
public interface RoundApi {

    @GetMapping("/round/{roundId}/interview")
    ResponseEntity<ResponseDto> getContestantAndEmployeeDetails(@PathVariable String roundId, @RequestParam(value = "passMark", required = false) Integer passMark);
    @GetMapping("/round/interview/live")
    ResponseEntity<ResponseDto> getRoundsForHRAssign(@RequestParam(value = "request") String request);
    @PostMapping("/round/{roundId}/interview")
    ResponseEntity<ResponseDto> sendEmailToTheEmployeesAboutTheInterview(@PathVariable String roundId, @RequestBody EmployeeIds employeeIds);
    @PostMapping("/round/{roundId}/interview/schedule")
    ResponseEntity<ResponseDto> generateInterviewSchedule(@PathVariable String roundId, @RequestBody InterviewScheduleGenerateDTO scheduleGenerateDTO) throws IOException, ParseException;
    @PutMapping("/round/{roundId}/interview/schedule")
    ResponseEntity<ResponseDto> updateTheInterviewTiming(@PathVariable String roundId, @RequestBody(required = false) List<InterviewScheduleResponseDTO> interviewScheduleResponseDTO) throws ParseException;
    @GetMapping("/round/interview/reschedule/{requestId}")
    ResponseEntity<ResponseDto> getRequest(@PathVariable Long requestId, @RequestParam(value = "choice") String choice);
    @PutMapping("/round/interview/reschedule/{requestId}")
    ResponseEntity<ResponseDto> updateInterview(@PathVariable Long requestId, @RequestParam(value = "decision") String decision, @RequestBody EmployeeAndTime employeeAndTime) throws MessagingException, ParseException;
    @PostMapping("/round/{roundId}/interview/end")
    ResponseEntity<ResponseDto> completeTheInterviewRound(@PathVariable String roundId);
    @GetMapping("/live-rounds")
    ResponseEntity<ResponseDto> getLiveRounds();
    @GetMapping("/category/{type}")
    ResponseEntity<ResponseDto> getAllCategory(@PathVariable String type);
    @PostMapping("/category")
    ResponseEntity<ResponseDto> createCategory(@RequestBody Category category);
}
