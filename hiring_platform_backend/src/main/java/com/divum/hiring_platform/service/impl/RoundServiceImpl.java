package com.divum.hiring_platform.service.impl;

import com.divum.hiring_platform.dto.*;
import com.divum.hiring_platform.entity.*;
import com.divum.hiring_platform.exception.InvalidDataException;
import com.divum.hiring_platform.repository.MCQResultRepository;
import com.divum.hiring_platform.repository.service.*;
import com.divum.hiring_platform.service.RoundService;
import com.divum.hiring_platform.util.ContestRelatedService;
import com.divum.hiring_platform.util.EmailSender;
import com.divum.hiring_platform.util.GoogleMeetService;
import com.divum.hiring_platform.util.enums.*;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RoundServiceImpl implements RoundService {

    private final EmailSender emailSender;
    private final InterviewRepositoryService interviewRepositoryService;
    private final RoundsRepositoryService roundsRepositoryService;
    private final MCQResultRepository mCQResultRepository;
    private final UserRepositoryService userRepositoryService;
    private final EmployeeRepositoryService employeeRepositoryService;
    private final CategoryRepositoryService categoryRepositoryService;
    private final MCQQuestionRepositoryService mcqQuestionRepositoryService;
    private final CodingQuestionRepositoryService codingQuestionRepositoryService;
    private final EmployeeAvailabilityRepositoryService employeeAvailabilityRepositoryService;
    private final InterviewRequestRepositoryService interviewRequestRepositoryService;
    private final ContestRepositoryService contestRepositoryService;
    private final ContestRelatedService contestRelatedService;
    private final CodingResultRepositoryService codingResultRepositoryService;
    private final GoogleMeetService googleMeetService;

    private static final String RESCHEDULE = "RESCHEDULE";
    private static final String DD_MM_YY = "dd-MM-yy";
    private static final String CODING = "CODING";
    private static final String HH_MM = "HH:mm";
    private static final String H_MMA = "h:mma";
    private static final String ROUND_NOT_FOUND = "Round not found";
    private static final String NOT_A_VALID_REQUEST = "Not a valid request";
    private static final String PERSONAL = "Personal";
    private static final String TECHNICAL = "Technical";
    private static final String CONTEST_NOT_FOUND = "Contest not found";

    @Value("${spring.mail.username}")
    private String adminMail;


    @Override
    public ResponseEntity<ResponseDto> getContestantAndEmployeeDetails(String roundId, Integer passMark) {
        Rounds rounds = roundsRepositoryService.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException(CONTEST_NOT_FOUND));
        if (rounds.getStartTime() == null) {
            Rounds previousRound = contestRelatedService.getPreviousRound(rounds.getContest(), rounds);
            if (previousRound != null) {
                if (previousRound.getEndTime().isAfter(LocalDateTime.now())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDto("The previous round is not completed", null));
                }
                if (passMark != null) {
                    previousRound.setPassPercentage(passMark);
                    roundsRepositoryService.save(previousRound);
                    emailSender.updateResult(previousRound, new ArrayList<>());
                }
                return getDetailsToConductInterview(rounds, previousRound);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("There are no previous round for this interview", null));
            }
        } else {
            return getLiveInterviewRound(roundId);
        }
    }

    @Override
    public ResponseEntity<ResponseDto> getRoundsForHRAssign(String request) {
        List<Contest> contests = contestRepositoryService.findContestsByContestStatus(ContestStatus.CURRENT);
        if (contests == null) {
            return ResponseEntity.ok(new ResponseDto("0 live contest found", null));
        }
        if (request.equals("INTERVIEW")) {
            return getInterviewDetails(contests);
        } else if (request.equals(RESCHEDULE)) {
            return getRescheduleRequest(contests);
        } else {
            throw new InvalidDataException(NOT_A_VALID_REQUEST);
        }
    }

    @Override
    public ResponseEntity<ResponseDto> sendEmailToTheEmployeesAboutTheInterview(String roundId, EmployeeIds employeeIds) {
        Rounds rounds = roundsRepositoryService.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException(ROUND_NOT_FOUND));
        try {
            List<Employee> employees = new ArrayList<>();
            Contest contest = rounds.getContest();
            for (Long employeeId : employeeIds.getIds()) {
                employees.add(employeeRepositoryService.findEmployeeByEmployeeId(employeeId));
            }
            List<EmployeeAvailability> employeeAvailabilities = new ArrayList<>();
            for (Employee employee : employees) {
                EmployeeAvailability employeeAvailability = new EmployeeAvailability();
                employeeAvailability.setEmployeeAndContest(new EmployeeAndContest(employee, contest));
                employeeAvailability.setResponse(EmployeeResponse.PENDING);
                employeeAvailabilities.add(employeeAvailability);
            }
            emailSender.notifyAboutOneOnOne(employeeAvailabilities);
            employeeAvailabilityRepositoryService.saveAll(employeeAvailabilities);
            return ResponseEntity.ok(new ResponseDto("Email sent to the employees ", null));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto(e.getMessage(), null));
        }
    }


    @Override
    public ResponseEntity<ResponseDto> generateInterviewSchedule(String roundId, InterviewScheduleGenerateDTO scheduleGenerateDTO) throws IOException, ParseException {
        Rounds rounds = roundsRepositoryService.findById(roundId)
                .orElseThrow(() -> new ResourceNotFoundException(ROUND_NOT_FOUND));
        rounds.setStartTime(scheduleGenerateDTO.getStartTime());
        Contest contest = rounds.getContest();
        EmployeeType employeeType;
        if (RoundType.PERSONAL_INTERVIEW == rounds.getRoundType()) {
            employeeType = EmployeeType.PERSONAL_HR;
        } else {
            employeeType = EmployeeType.TECHNICAL_HR;
        }
        List<Employee> employees = new ArrayList<>();
        for (Long employeeId : scheduleGenerateDTO.getEmployeeId()) {
            Employee employee = employeeAvailabilityRepositoryService.findEmployeesWhoIsAvailable(employeeId, contest, employeeType);
            employees.add(employee);
        }
        List<User> users = contestRepositoryService.findPassedStudents(contest);
        List<Interview> interviews = generateInterviews(rounds.getStartTime(), scheduleGenerateDTO.getDuration(), users, employees, rounds);
        interviewRepositoryService.saveAll(interviews);
        List<InterviewScheduleResponseDTO> response = buildInterviewResponse(interviews);
        return ResponseEntity.ok(new ResponseDto("Interview assigned", response));
    }

    @Override
    public ResponseEntity<ResponseDto> updateTheInterviewTiming(String roundId, List<InterviewScheduleResponseDTO> interviewScheduleResponseDTO) throws ParseException {
        List<InterviewScheduleResponseDTO> updated = new ArrayList<>();
        for (InterviewScheduleResponseDTO response : interviewScheduleResponseDTO) {
            Interview interview = null;
            interview = interviewRepositoryService.findById(response.getInterviewId())
                    .orElseThrow(() -> new ResourceNotFoundException("Interview with id " + response.getInterviewId() + " found"));
            assert interview != null;
            String interviewDateStr = response.getInterviewDate();
            String interviewTimeStr = response.getInterviewTime();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DD_MM_YY);
            LocalDate interviewDate = LocalDate.parse(interviewDateStr, dateFormatter);

            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(HH_MM);
            LocalTime interviewTime = LocalTime.parse(interviewTimeStr, timeFormatter);
            LocalDateTime interviewDateTime = interviewDate.atTime(interviewTime);
            interview.setInterviewTime(interviewDateTime);
            updateGoogleCalenderEvent(interview);
            interviewRepositoryService.save(interview);

            response.setInterviewTime(interview.getInterviewTime().format(DateTimeFormatter.ofPattern(DD_MM_YY)));
            response.setInterviewDate(interview.getInterviewTime().format(DateTimeFormatter.ofPattern(HH_MM)));
            updated.add(response);
            sendMail(interview);
        }
        return ResponseEntity.ok(new ResponseDto("Interview timings updated", updated));
    }

    private void updateGoogleCalenderEvent(Interview interview) throws ParseException {
        SimpleDateFormat inputFormat = getGoogleTimeParser();
        GoogleEventRequestDto request = new GoogleEventRequestDto(
                "Divum",
                inputFormat.parse(String.valueOf(interview.getInterviewTime())),
                inputFormat.parse(String.valueOf(interview.getInterviewTime().plusMinutes(30))),
                "Interview",
                Arrays.asList(interview.getUser().getEmail(), adminMail),
                interview.getEmployee().getEmail()
        );
        googleMeetService.updateEvent(interview.getEventId(), "boopathig158@gmail.com" , request);
    }

    @Override
    public ResponseEntity<ResponseDto> getRequest(Long requestId, String choice) {
        InterviewRequest rescheduleRequest;
        try {
            rescheduleRequest = interviewRequestRepositoryService.findById(requestId)
                    .orElseThrow(() -> new ResourceNotFoundException("No request found with id " + requestId));
        } catch (ResourceNotFoundException e) {
            return resourceNotFound(e.getMessage());
        }
        IndividualInterviewRequest request = new IndividualInterviewRequest();
        ContestDetails contestDetails = new ContestDetails();
        Rounds rounds = rescheduleRequest.getInterview().getRounds();
        contestDetails.setContestName(rounds.getContest().getName());
        contestDetails.setRoundNumber(rounds.getRoundNumber());
        contestDetails.setInterviewDate(rescheduleRequest.getInterview().getInterviewTime().format(DateTimeFormatter.ofPattern(DD_MM_YY)));
        contestDetails.setInterviewTime(rescheduleRequest.getInterview().getInterviewTime().format(DateTimeFormatter.ofPattern(H_MMA)));
        contestDetails.setRoundType(contestRelatedService.getInterviewType(rounds.getRoundType()));
        request.setContestDetails(contestDetails);

        if (choice.equals(RESCHEDULE)) {
            EmployeeDetails employeeDetails = new EmployeeDetails();
            String name = rescheduleRequest.getInterview().getEmployee().getFirstName() + " " + rescheduleRequest.getInterview().getEmployee().getLastName();
            employeeDetails.setName(name);
            employeeDetails.setPreferredTime(rescheduleRequest.getPreferredTime().format(DateTimeFormatter.ofPattern(H_MMA)));
            employeeDetails.setPreferredDate(rescheduleRequest.getPreferredTime().format(DateTimeFormatter.ofPattern(DD_MM_YY)));
            request.setEmployeeDetails(employeeDetails);

            List<Interview> interviews = interviewRepositoryService.findInterviewsByEmployee(rescheduleRequest.getInterview().getEmployee());
            List<ScheduleDetails> scheduleDetails = new ArrayList<>();
            for (Interview interview : interviews) {
                ScheduleDetails scheduleDetail = new ScheduleDetails();
                scheduleDetail.setIntervieweeName(interview.getUser().getName());
                scheduleDetail.setDate(interview.getInterviewTime().format(DateTimeFormatter.ofPattern(DD_MM_YY)));
                scheduleDetail.setTime(interview.getInterviewTime().format(DateTimeFormatter.ofPattern(H_MMA)));
                scheduleDetails.add(scheduleDetail);
            }
            request.setScheduleDetails(scheduleDetails);
        } else if (choice.equals("REASSIGN")) {
            List<InterviewAssignEmployee> employees = getEmployeeForInterview(rounds);
            request.setEmployees(employees);
        }
        return ResponseEntity.ok(new ResponseDto("interview request details", request));
    }


    @Override
    public ResponseEntity<ResponseDto> updateInterview(Long requestId, String decision, EmployeeAndTime employeeAndTime) throws MessagingException, ParseException {
        InterviewRequest interviewRequest;
        try {
            interviewRequest = interviewRequestRepositoryService.findById(requestId)
                    .orElseThrow(() -> new ResourceNotFoundException("Request not found"));
        } catch (ResourceNotFoundException e) {
            return resourceNotFound(e.getMessage());
        }
        Interview interview = interviewRequest.getInterview();
        Employee requestedEmployee = interview.getEmployee();
        switch (decision) {
            case RESCHEDULE -> {
                interview.setInterviewTime(employeeAndTime.getInterviewTime());
                interviewRequest.setInterviewRequestStatus(InterviewRequestStatus.RESCHEDULED);
                emailSender.sendEmailToTheContestantAndEmployeeAboutTheReschedule(interview);
            }
            case "REASSIGN" -> {
                Employee employee = employeeRepositoryService.findEmployeeByEmployeeId(employeeAndTime.getEmployeeId());
                interview.setInterviewTime(employeeAndTime.getInterviewTime());
                interview.setEmployee(employee);
                interviewRequest.setInterviewRequestStatus(InterviewRequestStatus.REASSIGNED);
                emailSender.sendEmailToEmployeeAboutReassign(interview, requestedEmployee);
            }
            case "REJECTED" -> {
                interviewRequest.setInterviewRequestStatus(InterviewRequestStatus.REJECTED);
                emailSender.sendEmailToEmployeeAboutRejection(interviewRequest.getInterview());
            }
            default -> {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(NOT_A_VALID_REQUEST, null));
            }
        }
        updateGoogleCalenderEvent(interview);
        interviewRequestRepositoryService.save(interviewRequest);
        interviewRepositoryService.save(interview);
        return ResponseEntity.ok(new ResponseDto("Interview updated", null));
    }


    @Override
    public ResponseEntity<ResponseDto> completeTheInterviewRound(String roundId) {
        Rounds round;
        try {
            round = roundsRepositoryService.findById(roundId)
                    .orElseThrow(() -> new ResourceNotFoundException(ROUND_NOT_FOUND));
        } catch (ResourceNotFoundException e) {
            return resourceNotFound(e.getMessage());
        }
        round.setEndTime(LocalDateTime.now());
        roundsRepositoryService.save(round);
        try {
            emailSender.sendEmailAboutInterviewResult(round);
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto(e.getMessage(), null));
        }
        return ResponseEntity.ok(new ResponseDto("Interview round completed", null));
    }




    private ResponseEntity<ResponseDto> getLiveInterviewRound(String roundId) {
        List<Interview> interviews = interviewRepositoryService.findInterviewsByRoundsId(roundId);
        try {
            if (interviews == null) {
                throw new ResourceNotFoundException("No interview with this round");
            }
        } catch (ResourceNotFoundException e) {
            return resourceNotFound(e.getMessage());
        }
        List<InterviewScheduleResponseDTO> response = buildInterviewResponse(interviews);
        return ResponseEntity.ok(new ResponseDto("Scheduled Interview list", response));
    }

    private ResponseEntity<ResponseDto> getRescheduleRequest(List<Contest> contests) {
        List<Rounds> rounds = new ArrayList<>();
        for (Contest contest : contests) {
            rounds.addAll(contest.getRounds());
        }
        rounds.removeIf(round -> round.getRoundType() == RoundType.MCQ || round.getRoundType() == RoundType.CODING);
        List<InterviewRequest> requests = new ArrayList<>();
        for (Rounds round : rounds) {
            requests.addAll(interviewRequestRepositoryService.findCurrentInterviewRequest(round.getId()));
        }
        List<InterviewRequestLog> requestLogs = new ArrayList<>();

        for (InterviewRequest request : requests) {
            InterviewRequestLog requestLog = new InterviewRequestLog();
            Rounds interviewRound = request.getInterview().getRounds();
            String roundType = contestRelatedService.getInterviewType(interviewRound.getRoundType());
            String round = interviewRound.getRoundNumber() + " - " + roundType;
            requestLog.setRound(round);
            requestLog.setIntervieweeName(request.getInterview().getUser().getName());
            requestLog.setId(request.getId());
            requestLog.setContestName(interviewRound.getContest().getName());
            requestLog.setEmployeeName(request.getInterview().getEmployee().getFirstName() + " " + request.getInterview().getEmployee().getLastName());
            requestLog.setRequestType(String.valueOf(request.getInterviewRequestType()));
            requestLog.setStatus(String.valueOf(request.getInterviewRequestStatus()));
            requestLog.setReason(request.getReason());
            requestLog.setAssignedTime(request.getInterview().getInterviewTime().format(DateTimeFormatter.ofPattern("dd/MM/yy - HH:mm")));
            requestLog.setPreferredTime(request.getPreferredTime().format(DateTimeFormatter.ofPattern("dd/MM/yy - HH:mm")));
            requestLogs.add(requestLog);
        }

        return ResponseEntity.ok(new ResponseDto("Reschedule requests", requestLogs));
    }

    private ResponseEntity<ResponseDto> getInterviewDetails(List<Contest> contests) {
        List<HrAssignDTO> assignDTOList = new ArrayList<>();
        for (Contest contest : contests) {
            List<Rounds> rounds = contest.getRounds();
            LocalDateTime contestStartTime = findContestStartTime(rounds);
            for (Rounds round : rounds) {
                if (isInterviewRoundWithoutEndTime(round)) {
                    HrAssignDTO assignDTO = createHrAssignDTO(contest, round, contestStartTime);
                    assignDTOList.add(assignDTO);
                }
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("current/upcoming interview rounds", assignDTOList));
    }

    private LocalDateTime findContestStartTime(List<Rounds> rounds) {
        for (Rounds round : rounds) {
            if (round.getRoundNumber() == 1) {
                return round.getStartTime();
            }
        }
        return null;
    }

    private boolean isInterviewRoundWithoutEndTime(Rounds round) {
        return (round.getRoundType() == RoundType.PERSONAL_INTERVIEW || round.getRoundType() == RoundType.TECHNICAL_INTERVIEW)
                && round.getEndTime() == null;
    }

    private HrAssignDTO createHrAssignDTO(Contest contest, Rounds round, LocalDateTime contestStartTime) {
        HrAssignDTO assignDTO = new HrAssignDTO();
        assignDTO.setContestName(contest.getName());
        assignDTO.setStatus(round.getStartTime() == null ? "Pending" : "Assigned");
        assignDTO.setRoundNumber(round.getRoundNumber());
        assert contestStartTime != null;
        assignDTO.setRoundType(contestRelatedService.getInterviewType(round.getRoundType()));
        assignDTO.setContestDate(contestStartTime.format(DateTimeFormatter.ofPattern("dd MMM, hh:mm a")));
        assignDTO.setRoundId(round.getId());
        return assignDTO;
    }


    private ResponseEntity<ResponseDto> sendMail(Interview interview) {
        try {
            sendEmailAboutTheInterview(interview.getRounds().getId());
            return null;
        } catch (Exception e) {
            return resourceNotFound(e.getMessage());
        }
    }


    private void sendEmailAboutTheInterview(String roundId) {
        List<Interview> interviews = interviewRepositoryService.findInterviewsByRoundsId(roundId);
        Map<Employee, Map<User, LocalDateTime>> employeeInterviewMap = new HashMap<>();

        for (Interview interview : interviews) {
            Employee employee = interview.getEmployee();
            User user = interview.getUser();
            LocalDateTime interviewTime = interview.getInterviewTime();
            employeeInterviewMap.computeIfAbsent(employee, k -> new HashMap<>());
            employeeInterviewMap.get(employee).put(user, interviewTime);
        }

        List<EmployeeInterviewScheduleMail> mails = new ArrayList<>();
        for (Map.Entry<Employee, Map<User, LocalDateTime>> entry : employeeInterviewMap.entrySet()) {
            Employee employee = entry.getKey();
            Map<User, LocalDateTime> userInterviewMap = entry.getValue();
            EmployeeInterviewScheduleMail mail = new EmployeeInterviewScheduleMail(employee, userInterviewMap);
            mails.add(mail);
        }
        emailSender.sendEmailAboutTheInterview(mails);
    }


    private List<InterviewScheduleResponseDTO> buildInterviewResponse(List<Interview> interviews) {
        List<InterviewScheduleResponseDTO> responseDTOList = new ArrayList<>();
        for (Interview interview : interviews) {
            InterviewScheduleResponseDTO response = new InterviewScheduleResponseDTO();
            response.setInterviewId(interview.getInterviewId());
            String name = interview.getEmployee().getFirstName() + " " + interview.getEmployee().getLastName();
            response.setInterviewer(name);
            response.setInterviewDate(interview.getInterviewTime().format(DateTimeFormatter.ofPattern(DD_MM_YY)));
            response.setInterviewTime(interview.getInterviewTime().format(DateTimeFormatter.ofPattern(HH_MM)));
            response.setInterviewee(interview.getUser().getName());
            response.setCollageName(interview.getUser().getCollegeName());
            responseDTOList.add(response);
        }
        return responseDTOList;
    }

    public List<Interview> generateInterviews(LocalDateTime startTime, int durationMinutes, List<User> users, List<Employee> employees, Rounds round) throws IOException, ParseException {
        List<Interview> interviews = new ArrayList<>();
        if (users.isEmpty() || employees.isEmpty()) {
            return interviews;
        }
        int totalUsers = users.size();
        int totalEmployees = employees.size();
        int usersPerEmployee = totalUsers / totalEmployees;

        int remainingUsers = totalUsers % totalEmployees;

        int[] usersAssignedPerEmployee = new int[totalEmployees];
        Arrays.fill(usersAssignedPerEmployee, usersPerEmployee);

        for (int i = 0; i < remainingUsers; i++) {
            usersAssignedPerEmployee[i]++;
        }

        LocalDateTime interviewStartTime = startTime;
        Employee previousEmployee = null;
        for (int i = 0; i < totalEmployees; i++) {
            Employee employee = employees.get(i);
            if (previousEmployee != null && previousEmployee != employee) {
                interviewStartTime = startTime;
            }
            Iterator<User> userIterator = users.iterator();
            for (int j = 0; j < usersAssignedPerEmployee[i] && userIterator.hasNext(); j++) {
                User user = userIterator.next();
                userIterator.remove();
                Interview interview = new Interview();
                interview.setInterviewTime(interviewStartTime);
                interview.setUser(user);
                interview.setEmployee(employee);
                interview.setRounds(round);
                interviews.add(interview);
                interviewStartTime = interviewStartTime.plusMinutes(durationMinutes);
                generateGoogleEvent(interview, durationMinutes, employee);
                previousEmployee = employee;
            }

        }
        return interviews;
    }

    private void generateGoogleEvent(Interview interview, int durationMinutes, Employee employee) throws IOException, ParseException {
        GoogleEventRequestDto googleEventRequestDto = new GoogleEventRequestDto();
        googleEventRequestDto.setAttendees(
                Arrays.asList(
                        interview.getUser().getEmail(),
                        adminMail
                )
        );
        googleEventRequestDto.setSummary("Divum");
        SimpleDateFormat inputFormat = getGoogleTimeParser();
        googleEventRequestDto.setStartTime(inputFormat.parse(String.valueOf(interview.getInterviewTime())));
        googleEventRequestDto.setEndTime(inputFormat.parse(String.valueOf(interview.getInterviewTime().plusMinutes(durationMinutes))));
        googleEventRequestDto.setDescription("Interview");
        googleEventRequestDto.setOrganizerEmail(employee.getEmail());
        GoogleEventResponseDto response = googleMeetService.googleMeetCreation(googleEventRequestDto);
        interview.setEventId(response.getEventId());
        interview.setInterviewUrl(response.getGmeetLink());
    }

    private SimpleDateFormat getGoogleTimeParser() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
    }

    private ResponseEntity<ResponseDto> getDetailsToConductInterview(Rounds currentRound, Rounds previousRound) {
        try {
            Contest contest = currentRound.getContest();
            InterviewAssignDTO assignDTO = new InterviewAssignDTO();
            assignDTO.setId(currentRound.getId());
            assignDTO.setContestName(contest.getName());
            assignDTO.setRoundsNumber(currentRound.getRoundNumber());
            if (currentRound.getRoundType() == RoundType.PERSONAL_INTERVIEW) {
                assignDTO.setRound(PERSONAL);
            } else if (currentRound.getRoundType() == RoundType.TECHNICAL_INTERVIEW) {
                assignDTO.setRound(TECHNICAL);
            } else throw new InvalidDataException(NOT_A_VALID_REQUEST);
            assert previousRound != null;
            if (previousRound.getRoundType() == RoundType.MCQ || previousRound.getRoundType() == RoundType.CODING) {
                getPreviousRoundResult(previousRound, assignDTO);
            } else {
                getPreviousInterviewRoundResult(previousRound, assignDTO);
            }
            List<InterviewAssignEmployee> employees = getEmployeeForInterview(currentRound);
            assignDTO.setEmployees(employees);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("Previous rounds passed contestants and employee details", assignDTO));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
        }
    }

    private void getPreviousInterviewRoundResult(Rounds previousRound, InterviewAssignDTO assignDTO) {
        List<Interview> interviews = interviewRepositoryService.findInterviewsByRoundsId(previousRound.getId());
        List<Contestants> contestants = new ArrayList<>();
        for (Interview interview : interviews) {
            User user = interview.getUser();
            if (interview.getInterviewResult() == InterviewResult.SELECTED || interview.getInterviewResult() == InterviewResult.CAN_BE_CONSIDERATE) {
                Contestants contestant = new Contestants();
                contestant.setName(user.getName());
                contestant.setEmail(user.getEmail());
                contestant.setFeedBack(interview.getFeedBack());
                contestants.add(contestant);
                user.setPassed(true);
            } else {
                user.setPassed(false);
            }
            userRepositoryService.save(user);
        }
        assignDTO.setContestants(contestants);
    }

    private boolean employeeIsAlreadyAssigned(Employee employee, Contest contest) {
        Set<Contest> contests = contestRepositoryService.getContestAssignedForEmployee(String.valueOf(employee.getEmployeeId()));
        return contests.contains(contest);
    }


    private List<InterviewAssignEmployee> getEmployeeForInterview(Rounds currentRound) {
        EmployeeType employeeType = currentRound.getRoundType() == RoundType.TECHNICAL_INTERVIEW ? EmployeeType.TECHNICAL_HR : EmployeeType.PERSONAL_HR;
        List<Employee> employees = employeeRepositoryService.findEmployeesByEmployeeType(employeeType);
        employees.removeIf(employee -> employee.getRole() == Role.ADMIN);
        List<EmployeeAvailability> employeeAndContests = employeeAvailabilityRepositoryService.findEmployeeAvailabilitiesByContest(currentRound.getContest().getContestId());
        Map<Employee, EmployeeResponse> employeeAndDecision = new HashMap<>();
        List<InterviewAssignEmployee> interviewAssignEmployees = new ArrayList<>();
        for (EmployeeAvailability employeeAvailability : employeeAndContests) {
            employeeAndDecision.put(employeeAvailability.getEmployeeAndContest().getEmployee(), employeeAvailability.getResponse());
        }
        for (Employee employee : employees) {
            if (employeeIsAlreadyAssigned(employee, currentRound.getContest())) {
                continue;
            }
            InterviewAssignEmployee assignEmployee = new InterviewAssignEmployee();
            assignEmployee.setId(employee.getEmployeeId());
            assignEmployee.setName(employee.getFirstName() + " " + employee.getLastName());
            assignEmployee.setEmail(employee.getEmail());
            if (employeeAndDecision.containsKey(employee)) {
                assignEmployee.setStatus(String.valueOf(employeeAndDecision.get(employee)));
            } else {
                assignEmployee.setStatus("Not assigned");
            }
            interviewAssignEmployees.add(assignEmployee);
        }
        return interviewAssignEmployees;
    }
    private void getPreviousRoundResult(Rounds previousRound, InterviewAssignDTO assignDTO) {
        List<Contestants> contestants = new ArrayList<>();
        if (previousRound.getRoundType() == RoundType.MCQ) {
            List<MCQResult> mcqResults = mCQResultRepository.findMCQResultsByRoundIdAndResult(previousRound.getId(), Result.PASS);
            contestants = contestRelatedService.updatedResultInfo(mcqResults);
        } else {
            List<CodingResult> results = codingResultRepositoryService.findCodingResultsByRoundId(previousRound.getId());
            contestants = contestRelatedService.updatedCodingResult(results, previousRound.getPassPercentage());
        }
        assignDTO.setContestants(contestants);
    }

    public ResponseEntity<ResponseDto> resourceNotFound(String message) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(message, null));
    }

    @Override
    public ResponseEntity<ResponseDto> getAllCategory(String type) {
        List<CategoryDetails> categoryDetails = new ArrayList<>();
        List<Category> categories = categoryRepositoryService.findAll();
        if (type.equals("MCQ")) {
            categories.removeIf(c -> c.getQuestionCategory().toString().contains(CODING));
        } else if (type.equals(CODING)) {
            categories.removeIf(c -> c.getQuestionCategory().toString().contains("MCQ"));
        }
        for (Category category : categories) {
            CategoryDetails categoryDetails1 = new CategoryDetails();
            Map<String, Integer> categoryAndCount = new HashMap<>();
            setCount(categoryAndCount, category, type);
            categoryDetails1.setCategoryAndCount(categoryAndCount);
            String[] headings = category.getQuestionCategory().toString().split("_");
            String heading = headings[0].substring(0, 1).toUpperCase() + headings[0].substring(1).toLowerCase();
            categoryDetails1.setHeading(heading);
            categoryDetails1.setCategory(category);
            categoryDetails.add(categoryDetails1);
        }
        return ResponseEntity.ok(new ResponseDto("The available categories", categoryDetails));
    }


    private void setCount(Map<String, Integer> categoryAndCount, Category category, String type) {
        Integer easy = 0;
        Integer medium = 0;
        Integer hard = 0;
        if (type.equals("MCQ")) {
            easy = mcqQuestionRepositoryService.getQuestionCountDifficultyWise(category, Difficulty.EASY);
            medium = mcqQuestionRepositoryService.getQuestionCountDifficultyWise(category, Difficulty.MEDIUM);
            hard = mcqQuestionRepositoryService.getQuestionCountDifficultyWise(category, Difficulty.HARD);
        } else if (type.equals(CODING)) {
            easy = codingQuestionRepositoryService.getQuestionCountDifficultyWise(category, Difficulty.EASY);
            medium = codingQuestionRepositoryService.getQuestionCountDifficultyWise(category, Difficulty.MEDIUM);
            hard = codingQuestionRepositoryService.getQuestionCountDifficultyWise(category, Difficulty.HARD);
        }
        categoryAndCount.put("Easy", easy);
        categoryAndCount.put("Medium", medium);
        categoryAndCount.put("Hard", hard);
    }


    @Override
    public ResponseEntity<ResponseDto> getLiveRoundsForFilter() {
        List<RecentlyCompletedRoundDTO> recentlyCompletedRounds = new ArrayList<>();
        LocalDateTime currentTime = LocalDateTime.now();
        List<Contest> contestList = contestRepositoryService.findContestsByContestStatus(ContestStatus.CURRENT);
        for (Contest contest : contestList) {
            Rounds recentlyCompletedRound = findRecentlyCompletedRound(contest, currentTime);
            if (recentlyCompletedRound != null) {
                RecentlyCompletedRoundDTO dto = createRecentlyCompletedRoundDTO(contest, recentlyCompletedRound);
                recentlyCompletedRounds.add(dto);
            }
        }
        return ResponseEntity.ok(new ResponseDto("Recently completed rounds are ", recentlyCompletedRounds));
    }

    @Override
    public ResponseEntity<ResponseDto> createCategory(Category category) {
        try {
            categoryRepositoryService.save(category);
            return ResponseEntity.ok(new ResponseDto("Category added successfully", category));
        } catch (Exception e) {
            throw new InvalidDataException(e.getMessage());
        }
    }


    private RecentlyCompletedRoundDTO createRecentlyCompletedRoundDTO(Contest contest, Rounds round) {
        RecentlyCompletedRoundDTO dto = new RecentlyCompletedRoundDTO();
        dto.setRoundId(round.getId());
        dto.setContestName(contest.getName());
        dto.setRoundNumber(round.getRoundNumber());
        dto.setRoundCompletedTime(round.getEndTime().format(DateTimeFormatter.ofPattern("dd MMM, hh:mm a")));
        dto.setStatus(round.getEndTime().isBefore(LocalDateTime.now().minusMinutes(30)) ? "Published" : "Pending");
        return dto;
    }


    private @Nullable Rounds findRecentlyCompletedRound(@NotNull Contest contest, LocalDateTime currentTime) {
        List<Rounds> rounds = contest.getRounds();
        rounds.sort(Comparator.comparingInt(Rounds::getRoundNumber));
        for (int i = 0; i < rounds.size(); i++) {
            Rounds round = rounds.get(i);
            if (round.getRoundType() == RoundType.CODING || round.getRoundType() == RoundType.MCQ) {
                RoundType nextRoundType = rounds.get(i + 1) != null ? rounds.get(i + 1).getRoundType() : null;
                if (nextRoundType == RoundType.PERSONAL_INTERVIEW || nextRoundType == RoundType.TECHNICAL_INTERVIEW) {
                    continue;
                }
                if (isRecentlyCompleted(round, currentTime)) {
                    return round;
                }
            }
        }
        return null;
    }
    private boolean isRecentlyCompleted(Rounds round, LocalDateTime currentTime) {
        return round.getRoundType() == RoundType.CODING || round.getRoundType() == RoundType.MCQ &&
                round.getEndTime().isBefore(currentTime) && round.getEndTime().isBefore(currentTime.plusMinutes(30));
    }

}

