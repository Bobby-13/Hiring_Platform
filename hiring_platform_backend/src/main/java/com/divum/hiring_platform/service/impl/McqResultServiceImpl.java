
package com.divum.hiring_platform.service.impl;

import com.divum.hiring_platform.dto.PartResponseDto;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.entity.*;
import com.divum.hiring_platform.repository.MCQResultRepository;
import com.divum.hiring_platform.repository.service.MCQResultRepositoryService;
import com.divum.hiring_platform.repository.service.RoundsAndMcqQuestionRepositoryService;
import com.divum.hiring_platform.repository.service.RoundsRepositoryService;
import com.divum.hiring_platform.service.McqResultService;
import com.divum.hiring_platform.util.McqPartWiseResponseService;
import com.divum.hiring_platform.util.enums.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class McqResultServiceImpl implements McqResultService {

    private final MCQResultRepository mcqResultRepository;
    private final MCQResultRepositoryService mcqResultRepositoryService;
    private final McqPartWiseResponseService mcqPartWiseResponseService;
    private final RoundsRepositoryService roundsRepositoryService;
    private final RoundsAndMcqQuestionRepositoryService roundsAndMcqQuestionRepositoryService;

    public void update(MCQResult result) {
        Rounds round = roundsRepositoryService.findById(result.getRoundId())
                .orElseThrow(() -> new ResourceNotFoundException("Round not found with id " + result.getRoundId()));
        Map<String, Map<String, Integer>> totalQuestionCountDifficultyWise = new HashMap<>();
        Map<String, List<String>> mcqWithCorrectAnswer = getCorrectAnswers(round, totalQuestionCountDifficultyWise);
        List<PartWiseMark> partWiseMarks = new ArrayList<>();
        double totalPercentage = 0;
        int count = 0;
        for (PartWiseResponse partWiseResponse : result.getSavedMcq()) {
            count++;
            double currentPercentage = 0;
            PartWiseMark partWiseMark = evaluatePartWiseMark(partWiseResponse, mcqWithCorrectAnswer);
            currentPercentage = calculatePartWisePercentage(partWiseMark, totalQuestionCountDifficultyWise);
            partWiseMarks.add(partWiseMark);
            totalPercentage += currentPercentage;
        }
        if(count == 0) {
            totalPercentage = 1;
            count = 1;
        }
        totalPercentage = totalPercentage / count;
        result.setPartWiseMarks(partWiseMarks);
        result.setTotalPercentage((float) totalPercentage);
        result.setResult(totalPercentage >= round.getPassPercentage() ? Result.PASS : Result.FAIL);
    }

    private Map<String, List<String>> getCorrectAnswers(Rounds round, Map<String, Map<String, Integer>> totalQuestionCountDifficultyWise) {
        Map<String, List<String>> mcqWithCorrectAnswer = new HashMap<>();
        List<MultipleChoiceQuestion> questions = roundsAndMcqQuestionRepositoryService.findByRoundId(round.getId());

        for (MultipleChoiceQuestion question : questions) {
            Map<String, Integer> difficultyAndCount = totalQuestionCountDifficultyWise.getOrDefault(String.valueOf(question.getCategory().getQuestionCategory()), new HashMap<>());
            difficultyAndCount.put(String.valueOf(question.getDifficulty()), difficultyAndCount.getOrDefault(String.valueOf(question.getDifficulty()), 0) + 1);
            totalQuestionCountDifficultyWise.put(String.valueOf(question.getCategory().getQuestionCategory()), difficultyAndCount);
            List<String> correctOptions = question.getOptions().stream()
                    .filter(Options::isCorrect)
                    .map(Options::getOption)
                    .toList();
            mcqWithCorrectAnswer.put(question.getQuestionId(), correctOptions);
        }
        return mcqWithCorrectAnswer;
    }

    private PartWiseMark evaluatePartWiseMark(PartWiseResponse partWiseResponse, Map<String, List<String>> mcqWithCorrectAnswer) {
        PartWiseMark partWiseMark = new PartWiseMark();
        partWiseMark.setPart(partWiseResponse.getCategory());
        int correctAnsCount = 0;
        Map<String, Integer> questionCount = new HashMap<>();
        Map<String, Double> correctQuestionCount = new HashMap<>();
        for (UserResponse response : partWiseResponse.getUserResponse()) {
            String difficulty = response.getDifficulty();
            questionCount.put(difficulty, questionCount.getOrDefault(difficulty, 0) + 1);
            List<String> chosenAnswers = mcqWithCorrectAnswer.get(response.getQuestionId());
            boolean isCorrect = checkIfAnswersAreCorrect(chosenAnswers, response.getChosenAnswer());
            if (isCorrect) {
                correctAnsCount++;
                correctQuestionCount.put(difficulty, correctQuestionCount.getOrDefault(difficulty, 0.0) + 1);
                response.setIsCorrect(true);
            }
        }
        for (String difficulty : questionCount.keySet()) {
            correctQuestionCount.put(difficulty, (correctQuestionCount.getOrDefault(difficulty, 0.0) / questionCount.get(difficulty)) * 100);
        }
        partWiseMark.setCorrectAnswerCount(correctAnsCount);
        partWiseMark.setDifficultyWiseMarks(correctQuestionCount);
        return partWiseMark;
    }

    private boolean checkIfAnswersAreCorrect(List<String> correctAnswers, List<String> chosenAnswers) {
        return correctAnswers.size() == chosenAnswers.size() && new HashSet<>(correctAnswers).containsAll(chosenAnswers);
    }

    private double calculatePartWisePercentage(PartWiseMark partWiseMark, Map<String, Map<String, Integer>> totalQuestionCountDifficultyWise) {
        Map<String, Integer> difficultyAndMark = totalQuestionCountDifficultyWise.getOrDefault(partWiseMark.getPart(), new HashMap<>());

        double easy = (partWiseMark.getDifficultyWiseMarks().getOrDefault("EASY", 0.00) * difficultyAndMark.getOrDefault("EASY", 1)) / 100;
        double medium = (partWiseMark.getDifficultyWiseMarks().getOrDefault("HARD", 0.00) * difficultyAndMark.getOrDefault("MEDIUM", 1)) / 100;
        double hard = (partWiseMark.getDifficultyWiseMarks().getOrDefault("HARD", 0.00) * difficultyAndMark.getOrDefault("HARD", 1)) / 100;

        Integer totalEasy = difficultyAndMark.getOrDefault("EASY", 0);
        Integer totalMedium = difficultyAndMark.getOrDefault("MEDIUM", 0);
        Integer totalHard = difficultyAndMark.getOrDefault("HARD", 0);

        double totalPercentage = (easy * 0.33 + medium * 0.5 + hard ) / (totalEasy + totalMedium + totalHard);
        return (totalPercentage* (totalEasy + totalHard + totalMedium) / (easy + medium + hard)) * 100;
    }

    @Override
    public ResponseEntity<ResponseDto> partWiseResponseMcq(String userId, String roundId, PartResponseDto partResponseDto, String status) {
        Optional<MCQResult> mcqResult = mcqResultRepositoryService.findByUserIdAndRoundId(userId, roundId);
        if (mcqResult.isPresent()) {
            MCQResult result = mcqResult.get();
            PartWiseResponse partWiseResponses = mcqPartWiseResponseService.mcqPartWiseResponse(partResponseDto);
            for(PartWiseResponse response : result.getSavedMcq()) {
                if(partWiseResponses.getCategory().equals(response.getCategory())) {
                    response.setUserResponse(partWiseResponses.getUserResponse());
                    return ResponseEntity.ok(new ResponseDto("Answers updated", null));
                }
            }
            List<PartWiseResponse> partWiseResponse = mcqPartWiseResponseService.updateMcqPartWiseResponse(mcqResult.get());
            partWiseResponse.add(partWiseResponses);
            mcqResult.get().setSavedMcq(partWiseResponse);
            update(mcqResult.get());
            mcqResultRepository.save(result);
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("updated", result.getId()));
        } else {
            Optional<Rounds> round = roundsRepositoryService.findById(roundId);
            if (round.isEmpty()) throw new ResourceNotFoundException("Rounds Not Found");

            MCQResult mcqResult1 = new MCQResult();
            mcqResult1.setUserId(userId);
            mcqResult1.setContestId(round.get().getContest().getContestId());
            mcqResult1.setRoundId(roundId);
            PartWiseResponse partWiseResponses = mcqPartWiseResponseService.mcqPartWiseResponse(partResponseDto);
            List<PartWiseResponse> partWiseResponse = new ArrayList<>();
            partWiseResponse.add(partWiseResponses);
            mcqResult1.setSavedMcq(partWiseResponse);
            mcqResult1.setTotalPercentage(-1);
            if (LocalDateTime.now().isBefore(round.get().getEndTime())) {
                update(mcqResult1);
                mcqResultRepository.save(mcqResult1);
            }
            else {
                return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(new ResponseDto("Round has ended", null));
            }
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseDto("mcq user response saved", mcqResult1.getId()));
        }

    }

}