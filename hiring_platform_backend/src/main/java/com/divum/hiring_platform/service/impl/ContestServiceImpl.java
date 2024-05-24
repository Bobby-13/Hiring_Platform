package com.divum.hiring_platform.service.impl;

import com.divum.hiring_platform.dto.*;
import com.divum.hiring_platform.entity.*;
import com.divum.hiring_platform.exception.InvalidDataException;
import com.divum.hiring_platform.repository.service.*;
import com.divum.hiring_platform.service.ContestService;
import com.divum.hiring_platform.strings.Strings;
import com.divum.hiring_platform.util.ContestRelatedService;
import com.divum.hiring_platform.util.enums.*;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static com.divum.hiring_platform.strings.Strings.*;

@Service
@RequiredArgsConstructor
public class ContestServiceImpl implements ContestService {

    @Value("${email.task.round.first.welcome}")
    private String welcomeEmailTimeGap;

    @Value("${default.password}")
    private String defaultPassword;

    @Value("${email.task.round.first.start}")
    private String firstRoundStartTimeGap;

    @Value("${email.task.round.result}")
    private String resultMailTime;

    private final ContestRepositoryService contestRepositoryService;
    private final UserRepositoryService userRepositoryService;
    private final PasswordEncoder passwordEncoder;
    private final EmailTaskRepositoryService emailTaskRepositoryService;
    private final RoundsRepositoryService roundsRepositoryService;
    private final MCQResultRepositoryService mcqResultRepositoryService;
    private final CodingResultRepositoryService codingResultRepositoryService;
    private final InterviewRepositoryService interviewRepositoryService;
    private final RoundsAndQuestionRepositoryService roundsAndQuestionRepositoryService;
    private final EmployeeAvailabilityRepositoryService employeeAvailabilityRepositoryService;
    private final MCQQuestionRepositoryService mcqQuestionRepositoryService;
    private final ContestRelatedService contestRelatedService;
    private final CodingQuestionRepositoryService codingQuestionRepositoryService;
    private final RoundsAndMcqQuestionRepositoryService roundsAndMcqQuestionRepositoryService;
    private final NotificationRepositoryService notificationRepositoryService;

    @Override
    public ResponseEntity<ResponseDto> createContest(Contest contest) {
        createNewContest(contest);
        contestRepositoryService.save(contest);
        generateEmailTask(contest.getRounds());
        assignQuestion(contest.getContestId(), false);
        var contestId = ContestIdDto.builder()
                .contestId(contest.getContestId())
                .build();
        return ResponseEntity.ok(new ResponseDto(Strings.CONTEST_CREATED_SUCCESSFULLY, contestId));
    }

    @Override
    public ResponseEntity<ResponseDto> updateContest(String contestId, Contest contest) {
        Contest existingContest = contestRelatedService.getContestFromDatabase(contestId);
        existingContest.setName(contest.getName());
        List<Rounds> existingContestRounds = existingContest.getRounds();
        updateRounds(existingContest, contest.getRounds());
        contestRepositoryService.save(existingContest);
        updateEmailTask(contest.getRounds(), existingContestRounds);
        return ResponseEntity.ok().body(new ResponseDto("Contest updated", contest));
    }

    public void updateEmailTask(List<Rounds> updatedRounds, List<Rounds> existingContestRounds) {
        for (Rounds updatedRound : updatedRounds) {
            for (Rounds existingRound : existingContestRounds) {
                if (existingRound.getId().equals(updatedRound.getId())) {
                    updateEmailTasksForRound(updatedRound, existingRound);
                    break;
                }
            }
        }
    }

    private void updateEmailTasksForRound(Rounds updatedRound, Rounds existingRound) {
        List<EmailTask> emailTasks = emailTaskRepositoryService.findAllByRounds(existingRound);
        for (EmailTask emailTask : emailTasks) {
            if (emailTask.getTaskTime().isEqual(existingRound.getStartTime().minusHours(Long.parseLong(welcomeEmailTimeGap)))) {
                emailTask.setTaskTime(updatedRound.getStartTime().minusHours(Long.parseLong(welcomeEmailTimeGap)));
            } else if (emailTask.getTaskTime().isEqual(existingRound.getStartTime().minusMinutes(Long.parseLong(firstRoundStartTimeGap)))) {
                emailTask.setTaskTime(updatedRound.getStartTime().minusMinutes(Long.parseLong(firstRoundStartTimeGap)));
            } else {
                emailTask.setTaskTime(updatedRound.getEndTime().plusMinutes(Long.parseLong(resultMailTime)));
            }
        }
        emailTaskRepositoryService.saveAll(emailTasks);
    }

    @Override
    public ResponseEntity<ResponseDto> assignUser(String contestId, List<UserDto> users) {
        Contest contest = contestRelatedService.getContestFromDatabase(contestId);
        return assignUserUsingObjects(contest, users);
    }


    @Override
    public ResponseEntity<ResponseDto> deleteContest(String contestId) {
        Contest contest = contestRelatedService.getContestFromDatabase(contestId);
        List<User> users = contestRepositoryService.findUsersAssignedToTheContest(contestId);
        for (User user : users) {
            Set<Contest> contests = user.getContest();
            contests.removeIf(c -> c.getContestId().equals(contestId));
        }
        roundsAndMcqQuestionRepositoryService.deleteWithContestId(contestId);
        roundsAndQuestionRepositoryService.deleteWithContestId(contestId);
        employeeAvailabilityRepositoryService.deleteRecord(contestId);
        List<Employee> employees = contestRepositoryService.getEmployeeAssignedToTheContest(contestId);
        for (Employee employee : employees) {
            Set<Contest> contests = employee.getContest();
            contests.removeIf(c -> c.getContestId().equals(contestId));
        }
        contestRepositoryService.save(contest);
        contestRepositoryService.deleteByContestId(contestId);
        return ResponseEntity.ok(new ResponseDto("Contest with ID " + contestId + " deleted successfully.", null));
    }

    @Override
    public ResponseEntity<ResponseDto> getAllContest(String required) {
        if (required.equals("CONTEST")) {
            return getContest();
        } else if (required.equals("CONTESTANTS")) {
            return getContestants();
        } else {
            throw new InvalidDataException("NOT_A_VALID_REQUEST");
        }
    }

    @Override
    public ResponseEntity<ResponseDto> getContest(String contestId, String required) {
        if (required.equals("CONTEST")) {
            Contest contest = contestRelatedService.getContestFromDatabase(contestId);
            return ResponseEntity.ok(new ResponseDto("The contest with the id " + contestId, contest));
        } else if (required.equals("CONTESTANTS")) {
            List<User> users = contestRepositoryService.findUsersAssignedToTheContest(contestId);
            List<Contestants> contestants = new ArrayList<>();
            for (User user : users) {
                contestRelatedService.buildContestants(null, user, contestants);
            }
            return ResponseEntity.ok(new ResponseDto("Participated contestants ", contestants));
        } else {
            throw new InvalidDataException("NOT_A_VALID_REQUEST");
        }
    }

    private @NotNull ResponseEntity<ResponseDto> getContestants() {
        List<User> users = userRepositoryService.findAll();
        List<UserDetails> userDetails = new ArrayList<>();
        for (User user : users) {
            UserDetails userDetail = new UserDetails();
            userDetail.setUserId(user.getUserId());
            userDetail.setName(user.getName());
            userDetail.setEmail(user.getEmail());
            userDetails.add(userDetail);
        }
        return ResponseEntity.ok(new ResponseDto("Contestants", userDetails));
    }

    private @NotNull ResponseEntity<ResponseDto> getContest() {
        List<Contest> contests = contestRepositoryService.findAll();
        List<EssentialContestDetails> contestDetails = new ArrayList<>();
        for (Contest contest : contests) {
            EssentialContestDetails details = new EssentialContestDetails();
            details.setContestId(contest.getContestId());
            details.setName(contest.getName());
            details.setContestStatus(contest.getContestStatus());

            List<Rounds> sortedRounds = contest.getRounds()
                    .stream()
                    .sorted(Comparator.comparing(Rounds::getRoundNumber))
                    .toList();
            details.setStartDate(sortedRounds.get(0).getStartTime().format(DateTimeFormatter.ofPattern("dd/MM/yy")));
            if (contest.getContestStatus() == ContestStatus.COMPLETED) {
                details.setEndDate(sortedRounds.get(contest.getRounds().size() - 1).getEndTime().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
            }
            contestDetails.add(details);
        }
        return ResponseEntity.ok(new ResponseDto("Contest log", contestDetails));
    }

    @Override
    public ResponseEntity<ResponseDto> getUsers(String roundId, Integer passmark) {
        Optional<Rounds> rounds = roundsRepositoryService.findById(roundId);
        if (rounds.isEmpty()) {
            throw new ResourceNotFoundException(ROUND_NOT_FOUND + " " + roundId);
        }
        Rounds round = rounds.get();
        if (passmark != null) {
            round.setPassPercentage(passmark);
            roundsRepositoryService.save(round);
        }
        RoundFilterContestantDTO filterContestantDTO;
        if (round.getRoundType().equals(RoundType.MCQ)) {
            filterContestantDTO = contestRelatedService.getUpdatedMcqResult(roundId, round);
        } else if (round.getRoundType().equals(RoundType.CODING)) {
            filterContestantDTO = contestRelatedService.getUpdatedCodingResult(roundId, round);
        } else {
            throw new InvalidDataException("The round must be either Mcq or Coding");
        }
        return ResponseEntity.ok(new ResponseDto("Listed contestants for the round", filterContestantDTO));
    }

    @Override
    public ResponseEntity<ResponseDto> assignQuestion(String contestId, Boolean reassign) {
        Contest contest = contestRelatedService.getContestFromDatabase(contestId);
        List<MultipleChoiceQuestion> multipleChoiceQuestions = roundsAndMcqQuestionRepositoryService.getQuestionByContest(contest);
        List<CodingQuestion> codingQuestions = roundsAndQuestionRepositoryService.getQuestionByContest(contest);
        McqAndCodingList list = new McqAndCodingList();
        list.setCodingQuestions(codingQuestions);
        list.setMultipleChoiceQuestions(multipleChoiceQuestions);
        if (reassign != null && reassign) {
            roundsAndMcqQuestionRepositoryService.deleteWithContestId(contestId);
            roundsAndQuestionRepositoryService.deleteWithContestId(contestId);
        }
        if ((!multipleChoiceQuestions.isEmpty() || !codingQuestions.isEmpty()) && Boolean.FALSE.equals(reassign)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDto("Question are already assigned", list));
        }
        if (reassign == null || reassign == Boolean.TRUE) {
            if (reassign != null) {
                assignQuestion(contest);
            }
            ContestAndQuestion contestAndQuestion = new ContestAndQuestion();
            contestAndQuestion.setContestId(contestId);
            if (roundsAndQuestionRepositoryService.isAssigned(contestId)) {
                contestAndQuestion.setCodingQuestions(roundsAndQuestionRepositoryService.getQuestionByContest(contest));
            }
            if (roundsAndMcqQuestionRepositoryService.isAssigned(contestId)) {
                contestAndQuestion.setMultipleChoiceQuestions(roundsAndMcqQuestionRepositoryService.getQuestionByContest(contest));
            }
            return ResponseEntity.ok(new ResponseDto("The question has been assigned to the contest", contestAndQuestion));
        }
        assignQuestion(contest);
        return ResponseEntity.ok(new ResponseDto("The question has been assigned to the contest", null));
    }

    @Override
    public ResponseEntity<ResponseDto> finalResult(String contestId, String roundId, String isFinalResult) {
        Contest contest = contestRelatedService.getContestFromDatabase(contestId);
        List<Rounds> rounds = contest.getRounds();
        if (isFinalResult.equals("true")) {
            return getFinalResult(contest);
        } else if (isFinalResult.equals("false")) {
            if (roundId == null) {
                return getPrimaryRoundResultDetails(rounds, contest);
            } else {
                return getRoundWiseResult(roundId);
            }
        } else {
            throw new InvalidDataException(NOT_A_VALID_REQUEST);
        }
    }
    private ResponseEntity<ResponseDto> getFinalResult(Contest contest) {
        List<User> users = contestRepositoryService.findPassedStudents(contest);
        List<FinalResultUser> finalResultUsers = new ArrayList<>();
        for (User user : users) {
            FinalResultUser resultUser = new FinalResultUser();
            resultUser.setContestant(user.getName());
            resultUser.setUserId(user.getUserId());
            resultUser.setCollege(user.getCollegeName());
            resultUser.setEmail(user.getEmail());
            finalResultUsers.add(resultUser);
        }
        return ResponseEntity.ok(new ResponseDto("Final result list", finalResultUsers));
    }

    private ResponseEntity<ResponseDto> getRoundWiseResult(String roundId) {
        Rounds round = roundsRepositoryService.findById(roundId).
                orElseThrow(() -> new ResourceNotFoundException(ROUND_NOT_FOUND));
        int count;
        RoundResult result = new RoundResult();
        result.setPassPercentage(round.getPassPercentage());
        result.setInterviewResults(null);
        List<PartWiseMarkAllocation> markAllocations = new ArrayList<>();
        List<InterviewResultDetails> interviewResults = new ArrayList<>();
        switch (round.getRoundType()) {
            case MCQ -> {
                result.setRoundType("MCQ");
                count = mcqResultRepositoryService.countByRoundId(roundId);
                getTestTypeRoundDetails(result, count, round, markAllocations);
            }
            case CODING -> {
                result.setRoundType("Coding");
                count = codingResultRepositoryService.countByRoundId(roundId);
                getTestTypeRoundDetails(result, count, round, markAllocations);
            }
            case TECHNICAL_INTERVIEW ->
                    getInterviewTypeRoundDetails(RoundType.TECHNICAL_INTERVIEW, roundId, result, round, interviewResults);
            case PERSONAL_INTERVIEW ->
                    getInterviewTypeRoundDetails(RoundType.PERSONAL_INTERVIEW, roundId, result, round, interviewResults);
        }
        if (round.getEndTime() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto("The round is not ended", null));
        }
        LocalDateTime startTime = round.getStartTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM,dd,yy-hh:mm");
        String startingTime = startTime.format(formatter);
        String endingTime = round.getEndTime().format(formatter);

        result.setRoundRange(startingTime + " -- " + endingTime);
        result.setRoundNumber(String.valueOf(round.getRoundNumber()));
        return ResponseEntity.ok(new ResponseDto("Round wise result", result));
    }

    private void getInterviewTypeRoundDetails(RoundType roundType, String roundId, RoundResult result, Rounds round, List<InterviewResultDetails> interviewResults) {
        int count;
        result.setRoundType(roundType == RoundType.PERSONAL_INTERVIEW ? "Personal Interview" : "Technical Interview");
        count = interviewRepositoryService.countByRoundsId(roundId);
        result.setParticipantsCount(count);
        result.setPassPercentage(null);
        result.setPartWiseMarkAllocations(null);
        getInterviewResult(round, interviewResults);
        result.setInterviewResults(interviewResults);
    }

    private void getTestTypeRoundDetails(RoundResult result, int count, Rounds round, List<PartWiseMarkAllocation> markAllocations) {
        result.setParticipantsCount(count);
        result.setPassCount(round.getPassCount());
        getPartWiserMark(round, markAllocations, result);
        result.setParticipantsCount(round.getParticipantsCounts());
        result.setPassPercentage(round.getPassPercentage());
        result.setPartWiseMarkAllocations(markAllocations);
    }

    private void getInterviewResult(Rounds round, List<InterviewResultDetails> interviewResults) {
        List<Interview> interviews = interviewRepositoryService.findInterviewsByRoundsId(round.getId());
        for (Interview interview : interviews) {
            InterviewResultDetails interviewResult = new InterviewResultDetails();
            interviewResult.setContestant(interview.getUser().getName());
            interviewResult.setEmployee(interview.getEmployee().getFirstName() + " " + interview.getEmployee().getLastName());
            interviewResult.setCollageName(interview.getUser().getCollegeName());
            interviewResult.setEmail(interview.getUser().getEmail());
            interviewResults.add(interviewResult);
        }
    }

    private void getPartWiserMark(Rounds round, List<PartWiseMarkAllocation> markAllocations, RoundResult result) {
        int totalNumberOfQuestion = 0;
        int totalTimeAllocated = 0;
        for (Part part : round.getParts()) {
            totalNumberOfQuestion += part.getEasy() + part.getMedium() + part.getHard();
            PartWiseMarkAllocation markAllocation = new PartWiseMarkAllocation();
            totalTimeAllocated += part.getAssignedTime();
            markAllocation.setAllocatedTime(part.getAssignedTime());
            markAllocation.setPart(part.getCategory().getQuestionCategory().toString().split("_")[0]);
            List<DifficultyAndCount> difficultyAndCountList = new ArrayList<>();
            getDifficultyWiseMarkAndTime(difficultyAndCountList, part.getEasy(), "Easy");
            getDifficultyWiseMarkAndTime(difficultyAndCountList, part.getMedium(), "Medium");
            getDifficultyWiseMarkAndTime(difficultyAndCountList, part.getHard(), "Hard");
            markAllocation.setDifficultyAndCountList(difficultyAndCountList);
            markAllocations.add(markAllocation);
        }
        result.setTotalNumberOfQuestions(totalNumberOfQuestion);
        result.setTotalAllocatedTime(totalTimeAllocated);
    }

    private void getDifficultyWiseMarkAndTime(List<DifficultyAndCount> difficultyAndCountList, int count, String difficulty) {
        DifficultyAndCount difficultyAndCount = new DifficultyAndCount(difficulty, count);
        difficultyAndCountList.add(difficultyAndCount);
    }

    private ResponseEntity<ResponseDto> getPrimaryRoundResultDetails(List<Rounds> rounds, Contest contest) {
        ContestResult result = new ContestResult();

        Rounds firstRound = rounds.stream().filter(r -> r.getRoundNumber() == 1).findFirst().orElse(null);
        Rounds lastRound = rounds.stream().filter(r -> r.getRoundNumber() == rounds.size() - 1).findFirst().orElse(null);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM,dd,yyyy");
        if (firstRound == null || lastRound == null) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(new ResponseDto("Error occurred while fetching rounds", null));
        }
        String formattedStartDate = firstRound.getStartTime().format(formatter);
        String formattedEndDate = lastRound.getEndTime().format(formatter);
        result.setContestDate(formattedStartDate + " - " + formattedEndDate);
        result.setContestName(contest.getName());
        Long count = userRepositoryService.countUsersByContest(contest);
        result.setParticipantCount(count.intValue());
        Integer roundCount = 0;
        List<RoundList> roundLists = new ArrayList<>();
        rounds.sort(Comparator.comparingInt(Rounds::getRoundNumber));
        for (Rounds round : contest.getRounds()) {
            roundCount++;
            RoundList roundList = new RoundList();
            roundList.setRound(ROUND + round.getRoundNumber());
            String roundType = round.getRoundType() == RoundType.MCQ || round.getRoundType() == RoundType.CODING ? "Test" : "Interview";
            roundList.setRoundType(roundType);
            roundList.setRoundId(round.getId());
            roundLists.add(roundList);
        }
        if (contest.getContestStatus().equals(ContestStatus.COMPLETED)) {
            RoundList roundList = new RoundList();
            roundList.setRound("Final Result");
            roundList.setRoundId(contest.getContestId());
            roundLists.add(roundList);
        }
        result.setTotalRoundCount(roundCount);
        result.setRoundLists(roundLists);
        return ResponseEntity.ok(new ResponseDto("Contest result", result));
    }


    public ResponseEntity<ResponseDto> assignUserUsingObjects(Contest contest, List<UserDto> userDTOs) {
        List<String> errorList = new ArrayList<>();
        for (UserDto userDto : userDTOs) {
            if (contestRelatedService.isExistingUser(userDto.getEmail())) {
                contestRelatedService.assignUserToTheContest(userDto.getEmail(), contest, errorList);
            } else {
                User newUser = createUserFromDto(userDto, contest, passwordEncoder);
                userRepositoryService.save(newUser);
            }
        }
        if (!errorList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("The users with the following email id are already in a live or upcoming contest", errorList));
        }
        return ResponseEntity.ok(new ResponseDto("Users are assigned to the contest", null));
    }

    private User createUserFromDto(UserDto userDto, Contest contest, PasswordEncoder passwordEncoder) {
        User newUser = new User();
        newUser.setEmail(userDto.getEmail());
        newUser.setName(userDto.getName());
        newUser.setCollegeName(userDto.getCollege());
        newUser.setPassword(passwordEncoder.encode(defaultPassword));
        newUser.setPassed(false);
        Set<Contest> contestList = new HashSet<>();
        contestList.add(contest);
        newUser.setContest(contestList);
        return newUser;
    }

    public void updateRounds(Contest existingContest, List<Rounds> updatedRounds) {
        List<Rounds> existingRounds = existingContest.getRounds();
        List<Rounds> finalUpdatedRounds = new ArrayList<>();
        Rounds previousRound = null;
        for (Rounds updatedRoundDTO : updatedRounds) {
            Rounds existingRound = findOrCreateRound(updatedRoundDTO, existingRounds);
            if (previousRound != null && isSuitableRoundType(updatedRoundDTO.getRoundType())) {
                LocalDateTime previousRoundEndTime = previousRound.getEndTime();
                LocalDateTime currentRoundStartTime = updatedRoundDTO.getStartTime();
                if (currentRoundStartTime != null && previousRoundEndTime != null &&
                        previousRoundEndTime.plusHours(1).isAfter(currentRoundStartTime)) {
                    throw new InvalidDataException("Failed to update round: There is not enough time between the end of the round " + previousRound.getRoundNumber() + " and the start of the round" + updatedRoundDTO.getRoundNumber());
                }
            }
            updateRound(existingRound, updatedRoundDTO);
            existingRound.setContest(existingContest);
            finalUpdatedRounds.add(existingRound);
            previousRound = existingRound;
        }
        existingContest.setRounds(finalUpdatedRounds);
    }

    public void createNewContest(Contest contest) {
        contest.setContestStatus(ContestStatus.UPCOMING);
        List<Rounds> rounds = contest.getRounds();
        rounds.sort(Comparator.comparingInt(Rounds::getRoundNumber));
        Rounds previousRound = null;
        for (Rounds round : rounds) {
            round.setContest(contest);
            if (!isValidRoundType(round)) {
                throw new InvalidDataException(NOT_A_VALID_REQUEST);
            }
            checkTiming(round);
            if (previousRound != null && (previousRound.getRoundType() == RoundType.MCQ || previousRound.getRoundType() == RoundType.CODING)) {
                validateRoundTiming(previousRound, round);
            }
            previousRound = round;
        }
    }

    private void checkTiming(Rounds round) {
        if ((round.getRoundType() == RoundType.CODING || round.getRoundType() == RoundType.MCQ)) {
            if (round.getStartTime().isBefore(LocalDateTime.now()) || round.getEndTime().isBefore(LocalDateTime.now())) {
                throw new InvalidDataException(ROUND + round.getRoundNumber() + " Start and End time must be a future time");
            }
            if (calculateRoundDuration(round)) {
                throw new InvalidDataException(ROUND + round.getRoundNumber() + " duration exceeds the end time");
            }
        }
    }

    private boolean isValidRoundType(Rounds round) {
        return round.getRoundType() == RoundType.MCQ || round.getRoundType() == RoundType.CODING
                || round.getRoundType() == RoundType.PERSONAL_INTERVIEW || round.getRoundType() == RoundType.TECHNICAL_INTERVIEW;
    }

    private boolean calculateRoundDuration(Rounds round) {
        if (isTestRoundType(round)) {
            int roundDuration = 0;
            for (Part part : round.getParts()) {
                checkPartWithRound(round, part);
                roundDuration += part.getAssignedTime();
                part.setRounds(round);
            }
            return !round.getStartTime().plusMinutes(roundDuration).isBefore(round.getEndTime());
        } else {
            return false;
        }
    }

    private void validateRoundTiming(Rounds previousRound, Rounds round) {
        if ((previousRound != null) && isTestRoundType(round) && (previousRound.getEndTime().isAfter(round.getStartTime().minusHours(1)))) {
            throw new InvalidDataException("There is not enough time between round " + previousRound.getRoundNumber() + " and " + round.getRoundNumber());
        }
    }

    private boolean isTestRoundType(Rounds round) {
        return round.getRoundType() == RoundType.CODING || round.getRoundType() == RoundType.MCQ;
    }

    private boolean isSuitableRoundType(RoundType roundType) {
        return (roundType == RoundType.MCQ) || (roundType == RoundType.CODING);
    }

    private void checkPartWithRound(Rounds round, Part part) {
        QuestionCategory partCategory = part.getCategory().getQuestionCategory();
        boolean isValidCategory = (round.getRoundType() == RoundType.MCQ && isValidMCQCategory(partCategory)) ||
                (round.getRoundType() == RoundType.CODING && isValidCodingCategory(partCategory));
        if (!isValidCategory) {
            throw new InvalidDataException(partCategory + " is not suitable for " + round.getRoundType() + " round");
        }
    }

    private boolean isValidMCQCategory(QuestionCategory category) {
        return category == QuestionCategory.APTITUDE_MCQ || category == QuestionCategory.LOGICAL_MCQ ||
                category == QuestionCategory.VERBAL_MCQ || category == QuestionCategory.TECHNICAL_MCQ;
    }

    private boolean isValidCodingCategory(QuestionCategory category) {
        return category == QuestionCategory.ALGORITHMS_CODING || category == QuestionCategory.PATTERN_CODING ||
                category == QuestionCategory.STRINGS_CODING || category == QuestionCategory.DATA_STRUCTURE_CODING ||
                category == QuestionCategory.MATHEMATICS_CODING;
    }

    private Rounds findOrCreateRound(Rounds updatedRound, List<Rounds> existingRound) {
        if (updatedRound.getId() != null) {
            return existingRound.stream()
                    .filter(rounds -> rounds.getId().equals(updatedRound.getId()))
                    .findFirst()
                    .orElse(new Rounds());
        } else {
            return new Rounds();
        }
    }

    private void updateRound(Rounds round, Rounds updatedRound) {
        round.setStartTime(updatedRound.getStartTime());
        round.setEndTime(updatedRound.getEndTime());
        round.setPassPercentage(updatedRound.getPassPercentage());
        round.setContest(round.getContest());
        round.setRoundType(updatedRound.getRoundType());

        if (updatedRound.getParts() != null) {
            List<Part> existingParts = round.getParts();
            List<Part> finalUpdatedParts = new ArrayList<>();
            for (Part part : updatedRound.getParts()) {
                checkPartWithRound(round, part);
                Part fetchedPart = findOrCreatePart(existingParts, part);
                updatePart(fetchedPart, part);
                fetchedPart.setRounds(round);
                finalUpdatedParts.add(fetchedPart);
            }
            round.setParts(finalUpdatedParts);
        }

        if (calculateRoundDuration(round)) {
            throw new InvalidDataException("The end time of the round " + round.getRoundNumber() + " exceeds the end time");
        }
    }

    private Part findOrCreatePart(List<Part> existingParts, Part part) {
        if (existingParts == null) {
            return new Part();
        }
        return existingParts.stream()
                .filter(p -> p.getId().equals(part.getId()))
                .findFirst()
                .orElse(new Part());
    }

    private void updatePart(Part part, Part updated) {
        part.setEasy(updated.getEasy());
        part.setMedium(updated.getMedium());
        part.setHard(updated.getHard());
        part.setAssignedTime(updated.getAssignedTime());
        part.setCategory(updated.getCategory());
    }

    public void generateEmailTask(List<Rounds> rounds) {
        rounds.sort(Comparator.comparingInt(Rounds::getRoundNumber));
        List<EmailTask> emailTasks = new ArrayList<>();
        for (int i = 0; i < rounds.size(); i++) {
            Rounds round = rounds.get(i);
            RoundType roundType = round.getRoundType();
            if (roundType != RoundType.PERSONAL_INTERVIEW && roundType != RoundType.TECHNICAL_INTERVIEW) {
                if (round.getRoundNumber() == 1) {
                    createEmailTask(round, round.getStartTime().minusHours(Long.parseLong(welcomeEmailTimeGap)), emailTasks);
                    createEmailTask(round, round.getStartTime().minusMinutes(Long.parseLong(firstRoundStartTimeGap)), emailTasks);
                }
                if (i != rounds.size() - 1) {
                    RoundType nextRoundType = rounds.get(i + 1) != null ? rounds.get(i + 1).getRoundType() : null;
                    if ((nextRoundType == RoundType.PERSONAL_INTERVIEW || nextRoundType == RoundType.TECHNICAL_INTERVIEW)) {
                        continue;
                    }
                    createEmailTask(round, round.getEndTime().plusMinutes(Long.parseLong(resultMailTime)), emailTasks);
                }
            }
        }
        emailTaskRepositoryService.saveAll(emailTasks);
    }

    public void createEmailTask(Rounds round, LocalDateTime taskTime, List<EmailTask> emailTasks) {
        EmailTask emailTask = new EmailTask();
        emailTask.setRounds(round);
        emailTask.setTaskStatus(TaskStatus.PENDING);
        emailTask.setTaskTime(taskTime);
        emailTasks.add(emailTask);
    }

    public void assignQuestion(Contest contest) {
        int questionCount = 0;
        int actualQuestionCount = 0;
        for (Rounds rounds : contest.getRounds()) {
            int number = 0;
            if (rounds.getRoundType() == RoundType.MCQ) {
                number = assignMcqQuestions(rounds, contest);
                if (number > 0) {
                    questionCount += number;
                }
                actualQuestionCount = roundsAndMcqQuestionRepositoryService.getQuestionCount(contest);
            } else if (rounds.getRoundType() == RoundType.CODING) {
                number = assignCodingQuestions(rounds, contest);
                if (number > 0) {
                    questionCount += number;
                }
                actualQuestionCount = roundsAndQuestionRepositoryService.getQuestionCount(contest);
            }
            if (actualQuestionCount != questionCount || number == -1) {
                roundsAndMcqQuestionRepositoryService.deleteWithContestId(contest.getContestId());
                roundsAndQuestionRepositoryService.deleteWithContestId(contest.getContestId());
                throw new ResourceNotFoundException("Not enough question for the round " + rounds.getRoundNumber());
            }
        }
    }

    private int assignMcqQuestions(Rounds rounds, Contest contest) {
        int questionCount = 0;
        if (rounds.getParts().isEmpty()) {
            throw new InvalidDataException("No parts available in the round " + rounds.getRoundNumber() + " to assign questions. Please ensure that the round has at least one part configured");
        }
        for (int i = 0; i < rounds.getParts().size(); i++) {
            Part part = rounds.getParts().get(i);
            int number = assignMcqQuestionsToPart(part, contest);
            if (number > 0) {
                questionCount += number;
            } else {
                return -1;
            }
        }
        return questionCount;
    }

    private int assignCodingQuestions(Rounds rounds, Contest contest) {
        int questionCount = 0;
        if (rounds.getParts().isEmpty()) {
            throw new InvalidDataException("No parts available in the round " + rounds.getRoundNumber() + " to assign questions. Please ensure that the round has at least one part configured");
        }
        for (Part part : rounds.getParts()) {
            int number = assignCodingQuestion(part, contest);
            if (number > 0) {
                questionCount += number;
            } else {
                return number;
            }
        }
        return questionCount;
    }

    private int assignMcqQuestionsToPart(Part part, Contest contest) {
        List<MultipleChoiceQuestion> questions = new ArrayList<>();
        Map<Difficulty, Integer> difficultyIntegerMap = getDifficultyIntegerMap(part);
        int categoryId = part.getCategory().getCategoryId();

        for (Map.Entry<Difficulty, Integer> entry : difficultyIntegerMap.entrySet()) {
            Difficulty difficulty = entry.getKey();
            int requiredCount = entry.getValue();

            while (requiredCount > 0) {
                MultipleChoiceQuestion question = fetchQuestion(categoryId, difficulty, contest);
                if (question == null) {
                    return -1;
                }
                if (!questions.contains(question)) {
                    RoundAndMcqQuestion roundAndMcqQuestion = new RoundAndMcqQuestion();
                    ContestAndMcq contestAndMcq = new ContestAndMcq();
                    contestAndMcq.setContest(contest);
                    contestAndMcq.setMultipleChoiceQuestion(question);
                    roundAndMcqQuestion.setContestAndMcq(contestAndMcq);
                    roundAndMcqQuestion.setRounds(part.getRounds());
                    roundsAndMcqQuestionRepositoryService.save(roundAndMcqQuestion);
                    questions.add(question);
                    requiredCount--;
                }
            }
        }

        return questions.size();
    }

    private static Map<Difficulty, Integer> getDifficultyIntegerMap(Part part) {
        Map<Difficulty, Integer> difficultyIntegerMap = new EnumMap<>(Difficulty.class);
        difficultyIntegerMap.put(Difficulty.EASY, part.getEasy());
        difficultyIntegerMap.put(Difficulty.MEDIUM, part.getMedium());
        difficultyIntegerMap.put(Difficulty.HARD, part.getHard());
        return difficultyIntegerMap;
    }

    private MultipleChoiceQuestion fetchQuestion(int categoryId, Difficulty difficulty, Contest contest) {
        return mcqQuestionRepositoryService.getRandomQuestion(categoryId, difficulty, contest, 1);
    }

    public int assignCodingQuestion(Part part, Contest contest) {
        List<CodingQuestion> questions = new ArrayList<>();
        Map<Difficulty, Integer> difficultyIntegerMap = getDifficultyIntegerMap(part);
        int categoryId = part.getCategory().getCategoryId();

        for (Map.Entry<Difficulty, Integer> entry : difficultyIntegerMap.entrySet()) {
            Difficulty difficulty = entry.getKey();
            int requiredCount = entry.getValue();

            while (requiredCount > 0) {
                CodingQuestion question = fetchCodingQuestion(categoryId, difficulty, contest);
                if (question == null) {
                    return -1;
                }
                if (!questions.contains(question)) {
                    RoundAndCodingQuestion codingQuestion = new RoundAndCodingQuestion();
                    ContestAndCoding contestAndCoding = new ContestAndCoding();
                    contestAndCoding.setCodingQuestion(question);
                    contestAndCoding.setContest(contest);
                    codingQuestion.setRounds(part.getRounds());
                    codingQuestion.setContestAndCoding(contestAndCoding);
                    roundsAndQuestionRepositoryService.save(codingQuestion);
                    questions.add(question);
                    requiredCount--;
                }
            }
        }
        return questions.size();
    }

    private CodingQuestion fetchCodingQuestion(int categoryId, Difficulty difficulty, Contest contest) {
        return codingQuestionRepositoryService.getRandomQuestion(categoryId, difficulty, contest, 1);
    }

    @Override
    public ResponseEntity<ResponseDto> adminHomePage() {
        Map<String,Integer> contestCount=new HashMap<>();
        contestCount.put(ContestStatus.CURRENT.name(),contestRepositoryService.countContestByContestStatus(ContestStatus.CURRENT));
        contestCount.put(ContestStatus.COMPLETED.name(),contestRepositoryService.countContestByContestStatus(ContestStatus.COMPLETED));
        contestCount.put(ContestStatus.UPCOMING.name(),contestRepositoryService.countContestByContestStatus(ContestStatus.UPCOMING));

        //graph
        List<Contest> contestList=contestRepositoryService.findContestsByContestStatus(ContestStatus.COMPLETED);
        Map<Integer, Integer> contestCountByYear = new HashMap<>();
        for (Contest contest : contestList) {
            int year = contest.getRounds().get(0).getStartTime().getYear();

            if (contestCountByYear.containsKey(year)) {
                contestCountByYear.put(year, contestCountByYear.get(year) + 1);
            } else {
                contestCountByYear.put(year, 1);
            }
        }

        //notific
        List<Notification> notifications=notificationRepositoryService.findAll();

        //pie chart
        int assigned=0,notAssigned=0;
        List<Contest> contests=contestRepositoryService.findContestsByContestStatus(ContestStatus.CURRENT);
        for(Contest contest:contests){
            List<Rounds> roundsList = roundsRepositoryService.findByContestAndInRoundType(contest,List.of(RoundType.TECHNICAL_INTERVIEW,RoundType.PERSONAL_INTERVIEW));
            for(Rounds rounds:roundsList){
                boolean isRoundIdAssigned = interviewRepositoryService.existsByRoundId(rounds);
                if (isRoundIdAssigned) {
                    assigned++;
                } else {
                    notAssigned++;
                }
            }
        }
        int total=assigned+notAssigned;
        Map<String,Integer> HR_Assigned=new HashMap<>();
        if(total!=0)
        {
            HR_Assigned.put("ASSIGNED",assigned * 100 / total);
            HR_Assigned.put("NOT ASSIGNED",notAssigned * 100 / total);
        }else {
            HR_Assigned.put("ASSIGNED",0);
            HR_Assigned.put("NOT ASSIGNED",0);
        }

        AdminHomePageResponseDto responseDto=new AdminHomePageResponseDto();
        responseDto.setContestCount(contestCount);
        responseDto.setNotifications(notifications);
        responseDto.setContestCountByYear(contestCountByYear);
        responseDto.setAssignedHR(HR_Assigned);
        return ResponseEntity.ok(new ResponseDto("",responseDto));
    }
}
