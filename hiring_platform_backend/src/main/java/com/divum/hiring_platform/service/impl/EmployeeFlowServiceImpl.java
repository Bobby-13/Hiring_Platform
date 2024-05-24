package com.divum.hiring_platform.service.impl;

import com.divum.hiring_platform.dto.*;
import com.divum.hiring_platform.entity.*;
import com.divum.hiring_platform.repository.service.*;
import com.divum.hiring_platform.service.EmployeeFlowService;
import com.divum.hiring_platform.strings.Strings;
import com.divum.hiring_platform.util.enums.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class EmployeeFlowServiceImpl implements EmployeeFlowService {
    private final InterviewRepositoryService interviewRepositoryService;
    private final ContestRepositoryService contestRepositoryService;
    private final EmployeeRepositoryService employeeRepositoryService;
    private final RoundsRepositoryService roundsRepositoryService;
    private final ContestServiceImpl contestService;
    private final MCQResultRepositoryService mcqResultRepositoryService;
    private final CodingResultRepositoryService codingResultRepositoryService;
    private final InterviewRequestRepositoryService interviewRequestRepositoryService;
    private final NotificationRepositoryService notificationRepositoryService;
    private final UserRepositoryService userRepositoryService;

    @Override
    public ResponseEntity<ResponseDto> addFeedBackAndResult(String interviewId,FeedBackAndResultDto feedBackAndResultDto) {
        Optional<Interview> interview=interviewRepositoryService.findById(interviewId);
        if(interview.isPresent()) {
            Interview interview1 = interview.get();
            interview1.setFeedBack(feedBackAndResultDto.getFeedback());
            interview1.setInterviewResult(InterviewResult.valueOf(feedBackAndResultDto.getInterviewResult()));
            interviewRepositoryService.save(interview1);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(Strings.CREATE_FEEDBACK_SUCCESS, interview1));
        }else{
            return null;
        }
    }

    @Override
    public ResponseEntity<ResponseDto> getAssignedStudents(Long employeeId, String contestId) {
        Optional<Contest> contests = contestRepositoryService.findById(contestId);
        if(contests.isEmpty())
            throw new NullPointerException(Strings.CONTEST_NOT_FOUND);
        Contest contest = contests.get();
        Employee employee = employeeRepositoryService.findEmployeeByEmployeeId(employeeId);
        if(employee==null)
            throw new NullPointerException(Strings.EMPLOYEE_NOT_FOUND);
        RoundType roundType = null;
        if (employee.getEmployeeType().equals(EmployeeType.PERSONAL_HR)) {
            roundType = RoundType.PERSONAL_INTERVIEW;
        }
        if (employee.getEmployeeType().equals(EmployeeType.TECHNICAL_HR)) {
            roundType = RoundType.TECHNICAL_INTERVIEW;
        }
        List<Rounds> rounds = roundsRepositoryService.findByContestAndRoundType(contest, roundType);
        List<AssignedInterviewsDto> assignedInterviewsDtos = new ArrayList<>();
        if(contest.getContestStatus().equals(ContestStatus.CURRENT)){
            for (Rounds rounds1 : rounds) {
                List<Interview> interviews = interviewRepositoryService.findAllByEmployeeAndRounds(employee, rounds1);
                List<AssignedInterviewsDto> assignedInterviewsDtos1=getAssignedInterviews(interviews,contest,rounds1);
                assignedInterviewsDtos.addAll(assignedInterviewsDtos1);
            }
            return ResponseEntity.ok(new ResponseDto(Strings.ASSIGNED_INTERVIEWS, assignedInterviewsDtos));
        }
        else if(contest.getContestStatus().equals(ContestStatus.COMPLETED)){
            for (Rounds rounds1 : rounds) {
                List<Interview> interviews = interviewRepositoryService.findAllByEmployeeAndRounds(employee, rounds1);
                List<AssignedInterviewsDto> assignedInterviewsDtos1=getAssignedInterviewsforCompletedContest(interviews);
                assignedInterviewsDtos.addAll(assignedInterviewsDtos1);
            }
            return ResponseEntity.ok(new ResponseDto(Strings.ASSIGNED_INTERVIEWS, assignedInterviewsDtos));
        }
        return ResponseEntity.badRequest().body(new ResponseDto(Strings.INVALID_CONTEST,null));
    }
    public List<AssignedInterviewsDto> getAssignedInterviewsforCompletedContest(List<Interview> interviews) {
        List<AssignedInterviewsDto> assignedInterviewsDtos=new ArrayList<>();
        for (Interview interview : interviews) {
            AssignedInterviewsDto assignedInterviewsDto = new AssignedInterviewsDto();
            assignedInterviewsDto.setUserName(interview.getUser().getName());
            assignedInterviewsDto.setUserEmail(interview.getUser().getEmail());
            assignedInterviewsDto.setInterviewTime(interview.getInterviewTime());
            assignedInterviewsDto.setFeedBack(interview.getFeedBack());
            assignedInterviewsDto.setStatus(interview.getInterviewResult().toString());
            assignedInterviewsDtos.add(assignedInterviewsDto);
        }
        return assignedInterviewsDtos;
    }

    public List<AssignedInterviewsDto> getAssignedInterviews(List<Interview> interviews,Contest contest,Rounds rounds) {
        List<AssignedInterviewsDto> assignedInterviewsDtos=new ArrayList<>();
        for (Interview interview : interviews) {
            AssignedInterviewsDto assignedInterviewsDto = new AssignedInterviewsDto();
            assignedInterviewsDto.setUserName(interview.getUser().getName());
            assignedInterviewsDto.setUserEmail(interview.getUser().getEmail());
            assignedInterviewsDto.setInterviewTime(interview.getInterviewTime());
            if (interview.getFeedBack()!=null) {
                assignedInterviewsDto.setResume(interview.getUser().getResume().getResumeUrl());
                Map<String,String> previousRoundResult=getPreviousRoundResult(contest, rounds.getRoundNumber() - 1, interview.getUser().getUserId());
                assignedInterviewsDto.setPrevoiusRoundResult(previousRoundResult);
                assignedInterviewsDto.setMeetingLink(interview.getInterviewUrl());
                assignedInterviewsDto.setInterviewId(interview.getInterviewId());
                assignedInterviewsDto.setStatus(Strings.ASSIGNED);
            } else {
                assignedInterviewsDto.setFeedBack(interview.getFeedBack());
                assignedInterviewsDto.setStatus(Strings.COMPLETED);
            }
            assignedInterviewsDtos.add(assignedInterviewsDto);
        }
        return assignedInterviewsDtos;
    }

    public Map<String,String> getPreviousRoundResult(Contest contest,int roundsNum,String userId) {
        Map<String,String> previousRoundResult=new HashMap<>();
        if(roundsNum<=1){
            previousRoundResult.put(Strings.ROUND_NOT_FOUND,Strings.ROUND_NOT_FOUND);
            return previousRoundResult;
        }
        Rounds rounds=roundsRepositoryService.findByContestAndRoundNumber(contest,roundsNum);
        if(rounds==null){
            previousRoundResult.put(Strings.ROUND_NOT_FOUND,Strings.ROUND_NOT_FOUND);
            return previousRoundResult;
        }
        switch (rounds.getRoundType()){
            case MCQ :
                previousRoundResult.put(Strings.MCQ,mcqResultRepositoryService.getMcqResultId(rounds.getId(), userId));
                return previousRoundResult;
            case CODING :
                try {
                    previousRoundResult.put(Strings.CODING,codingResultRepositoryService.findByRoundIdAndUserId(rounds.getId(), userId).getId());
                    return previousRoundResult;
                }catch (Exception e){
                    previousRoundResult.put(Strings.CODING_RESULT_NOT_FOUND,Strings.CODING_RESULT_NOT_FOUND);
                    return previousRoundResult;
                }
            case PERSONAL_INTERVIEW, TECHNICAL_INTERVIEW :
                try {
                    previousRoundResult.put(Strings.INTERVIEW,interviewRepositoryService.getInterviewFeedback(rounds.getId(), userId));
                    return previousRoundResult;
                }catch (Exception e){
                    previousRoundResult.put(Strings.INTERVIEW_RESULT_NOT_FOUND,Strings.INTERVIEW_RESULT_NOT_FOUND);
                    return previousRoundResult;
                }
            default:return null;
        }
    }


    @Override
    public ResponseEntity<ResponseDto> requestReshedule(String interviewId, InterviewRequestType interviewRequestType, InterviewRequestDto interviewRequestDto) {
        Interview interview=interviewRepositoryService.findById(interviewId).orElse(null);
        if (interview!=null){
            InterviewRequest interviewRequest=new InterviewRequest();
            Notification notification=new Notification();
            if (interviewRequestType.equals(InterviewRequestType.CANCEL)){
                interviewRequest.setReason(interviewRequestDto.getReason());
                interviewRequest.setInterview(interview);
                interviewRequest.setInterviewRequestType(interviewRequestType);
                interviewRequest.setInterviewRequestStatus(InterviewRequestStatus.APPLIED);
                try {
                    interviewRequestRepositoryService.save(interviewRequest);
                }catch (Exception e){
                    return ResponseEntity.badRequest().body(new ResponseDto(Strings.CANNOT_SEND_INTERVIEW_REQUST_MORE_THAN_ONE_TIME,null));
                }
                notification.setMessage(Strings.INTERVIEW_REQUEST_APPLIED);
                notification.setCreatedAt(LocalDateTime.now());
                notification.setJobType(JobType.INTERVIEW_REQUEST);
                Notification savedNotification = notificationRepositoryService.save(notification);
                notification.setJobId(savedNotification.getJobId());
                return ResponseEntity.ok(new ResponseDto(Strings.CANCEL_REQUEST_SENT,null));
            }
            else if (interviewRequestType.equals(InterviewRequestType.RESCHEDULE)){
                interviewRequest.setReason(interviewRequestDto.getReason());
                interviewRequest.setInterview(interview);
                interviewRequest.setInterviewRequestType(interviewRequestType);
                interviewRequest.setInterviewRequestStatus(InterviewRequestStatus.APPLIED);
                interviewRequest.setPreferredTime(interviewRequestDto.getPreferredTime());
                try {
                    interviewRequestRepositoryService.save(interviewRequest);
                }catch (Exception e){
                    return ResponseEntity.badRequest().body(new ResponseDto(Strings.CANNOT_SEND_INTERVIEW_REQUST_MORE_THAN_ONE_TIME,null));
                }
                notification.setMessage(Strings.INTERVIEW_REQUEST_APPLIED);
                notification.setCreatedAt(LocalDateTime.now());
                notification.setJobType(JobType.INTERVIEW_REQUEST);
                notificationRepositoryService.save(notification);
                return ResponseEntity.ok(new ResponseDto(Strings.RESCHEDULE_REQUEST_SENT,null));
            }
            else{
                return ResponseEntity.badRequest().body(new ResponseDto(Strings.INVALID_REQUEST,interviewRequestType));
            }
        }
        return ResponseEntity.badRequest().body(new ResponseDto(Strings.NOT_FOUND_INTERVIEW_ID,null));
    }


    @Override
    public ResponseEntity<ResponseDto> getAllContests(String employeeId) {
        List<Contest> contests;
        try {
            contests = contestRepositoryService.findContestByEmployeeId(employeeId).orElseThrow(() -> new ResourceNotFoundException(Strings.EMPLOYEE_NOT_FOUND));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(e.getMessage(), null));
        }

        List<ContestsForEmployeeDto> contestsForEmployeeDtoS = new ArrayList<>();
        for (Contest contest : contests) {
            ContestsForEmployeeDto contestsForEmployeeDto = new ContestsForEmployeeDto();
            contestsForEmployeeDto.setContestId(contest.getContestId());
            contestsForEmployeeDto.setContestStatus(contest.getContestStatus());
            contestsForEmployeeDto.setName(contest.getName());
            for (Rounds rounds : contest.getRounds()) {
                if (rounds.getRoundNumber() == 1) {
                    contestsForEmployeeDto.setStartTime(String.valueOf(rounds.getStartTime()));
                }
            }
            contestsForEmployeeDtoS.add(contestsForEmployeeDto);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(Strings.ALL_CONTEST_FOR_EMPLOYEE, contestsForEmployeeDtoS));
    }

    @Override
    public ResponseEntity<ResponseDto> interviewDetails(String interviewId) {
        Interview interview;
        try {
            interview = interviewRepositoryService.findById(interviewId).orElseThrow(() -> new ResourceNotFoundException(Strings.NOT_FOUND_INTERVIEW_ID));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(Strings.NOT_FOUND_INTERVIEW_ID + interviewId, null));
        }
        InterviewDto interviewDto = new InterviewDto();
        interviewDto.setName(interview.getUser().getName());
        interviewDto.setInterviewTime(interview.getInterviewTime());
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(Strings.INTERVIEW1, interviewDto));

    }

    @Override
    public ResponseEntity<ResponseDto> feedback(String interviewId, FeedbackAndDescriptionDto feedbackAndDescriptionDto) {
        Interview interview;
        try {
            interview = interviewRepositoryService.findById(interviewId).orElseThrow(() -> new ResourceNotFoundException(Strings.NOT_FOUND_INTERVIEW_ID));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(Strings.NOT_FOUND_INTERVIEW_ID + interviewId, null));
        }

        interview.setInterviewResult(InterviewResult.valueOf(feedbackAndDescriptionDto.getFeedback()));
        if (interview.getInterviewResult().equals(InterviewResult.SELECTED) || interview.getInterviewResult().equals(InterviewResult.CAN_BE_CONSIDERATE)) {
            User user = interview.getUser();
            if (user != null) {
                user.setPassed(true);
                userRepositoryService.save(user);
            }
        }
        interview.setFeedBack(feedbackAndDescriptionDto.getDescription());
        interviewRepositoryService.save(interview);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto(Strings.INTERVIEW_UPDATE_SUCCESS, null));
    }


    @Override
    public ResponseEntity<ResponseDto> contestLog(String employeeId) {
        Optional<List<Contest>> contests = contestRepositoryService.findContestByEmployeeId(employeeId);

        if (contests.isEmpty())
            throw new ResourceNotFoundException("Employee not found");

        List<Contest> contests1 = new ArrayList<>();
        for (Contest contest : contests.get()) {
            if (contest.getContestStatus().toString().equals("COMPLETED"))
                contests1.add(contest);
        }
        List<ContestsForEmployeeDto> contestsForEmployeeDtos = new ArrayList<>();
        for (Contest contest : contests1) {
            ContestsForEmployeeDto contestsForEmployeeDto = new ContestsForEmployeeDto();
            contestsForEmployeeDto.setName(contest.getName());
            contestsForEmployeeDto.setContestId(contest.getContestId());
            for (Rounds rounds : contest.getRounds()) {
                if (rounds.getRoundNumber() == 1) {
                    String[] time = rounds.getStartTime().toString().split("T");
                    StringBuilder emp = new StringBuilder();
                    for (String x : time) {
                        emp.append(x);
                        emp.append(" ");
                    }
                    contestsForEmployeeDto.setStartTime(emp.toString());
                }
            }
            contestsForEmployeeDto.setContestStatus(ContestStatus.COMPLETED);
            contestsForEmployeeDtos.add(contestsForEmployeeDto);
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("all contests for this employee which is completed", contestsForEmployeeDtos));
    }


}
