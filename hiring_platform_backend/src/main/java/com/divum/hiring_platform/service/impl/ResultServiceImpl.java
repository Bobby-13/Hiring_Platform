package com.divum.hiring_platform.service.impl;

import com.divum.hiring_platform.dto.*;
import com.divum.hiring_platform.entity.PartWiseMark;
import com.divum.hiring_platform.entity.*;
import com.divum.hiring_platform.repository.CodingResultRepository;
import com.divum.hiring_platform.repository.ContestRepository;
import com.divum.hiring_platform.repository.InterviewRepository;
import com.divum.hiring_platform.repository.MCQResultRepository;
import com.divum.hiring_platform.repository.service.UserRepositoryService;
import com.divum.hiring_platform.service.ResutService;
import com.divum.hiring_platform.strings.Strings;
import com.divum.hiring_platform.util.enums.Difficulty;
import com.divum.hiring_platform.util.enums.RoundType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ResultServiceImpl implements ResutService {
    private final ContestRepository contestRepository;
    private final CodingResultRepository codingResultRepository;
    private final MCQResultRepository mcqResultRepository;
    private final UserRepositoryService userRepositoryService;
    private final InterviewRepository interviewRepository;
    @Override
    public ResponseEntity<ResponseDto> roundWiseResult(String contestId, String userId) {
        try {
            ResultChartDto resultChartDto=new ResultChartDto();

            User user=userRepositoryService.findUserByUserId(userId);
            Contest contest=contestRepository.findContestByContestId(contestId);
            List<Rounds> rounds=contest.getRounds();

            resultChartDto.setName(user.getName());
            resultChartDto.setCollege(user.getCollegeName());
            resultChartDto.setEmail(user.getEmail());

            List<RoundWisePercentageDto> roundWisePercentageDtoList=new ArrayList<>();
            List<PartWiseForEachRoundDto> partWiseForEachRoundDtoList=new ArrayList<>();

            for (Rounds rounds1:rounds){
                if (rounds1.getRoundType().equals(RoundType.MCQ)){

                    MCQResult mcqResult=mcqResultRepository.findByRoundIdAndUserId(rounds1.getId(),userId);
                    RoundWisePercentageDto roundWisePercentageDto=new RoundWisePercentageDto();
                    roundWisePercentageDto.setRoundType(RoundType.MCQ);
                    roundWisePercentageDto.setRoundNum(rounds1.getRoundNumber());
                    roundWisePercentageDto.setResult(String.valueOf(mcqResult.getTotalPercentage()));
                    roundWisePercentageDtoList.add(roundWisePercentageDto);
                    PartWiseForEachRoundDto partWiseForEachRoundDto=new PartWiseForEachRoundDto();
                    partWiseForEachRoundDto.setRoundNum(rounds1.getRoundNumber());

                    List<PartWisePercentageDto> partWisePercentageDto=new ArrayList<>();
                    List<com.divum.hiring_platform.entity.PartWiseMark> partWiseMarks=mcqResult.getPartWiseMarks();
                    for(PartWiseMark partWiseMark:partWiseMarks){
                        Map<String, Double> difficultyWisePercentage=partWiseMark.getDifficultyWiseMarks();
                        partWisePercentageDto.add(new PartWisePercentageDto(partWiseMark.getPart(),difficultyWisePercentage));
                    }
                    partWiseForEachRoundDto.setPartWisePercentageDto(partWisePercentageDto);
                    partWiseForEachRoundDtoList.add(partWiseForEachRoundDto);
                }
                if (rounds1.getRoundType().equals(RoundType.CODING)){
                    CodingResult codingResult=codingResultRepository.findByRoundIdAndUserId(rounds1.getId(),userId);
                    RoundWisePercentageDto roundWisePercentageDto=new RoundWisePercentageDto();
                    roundWisePercentageDto.setRoundType(RoundType.CODING);
                    roundWisePercentageDto.setRoundNum(rounds1.getRoundNumber());
                    roundWisePercentageDto.setResult(String.valueOf(codingResult.getTotalPercentage()));
                    roundWisePercentageDtoList.add(roundWisePercentageDto);
                    PartWiseForEachRoundDto partWiseForEachRoundDto=new PartWiseForEachRoundDto();
                    partWiseForEachRoundDto.setRoundNum(rounds1.getRoundNumber());

                    List<PartWisePercentageDto> partWisePercentageDto=new ArrayList<>();
                    Map<Difficulty, Integer> difficultyWise = codingResult.getPercentage();
                    Map<String, Double> difficultyWisePercentage = getStringDoubleMap(difficultyWise);
                    partWisePercentageDto.add(new PartWisePercentageDto(Strings.CODING,difficultyWisePercentage));
                    partWiseForEachRoundDto.setPartWisePercentageDto(partWisePercentageDto);
                    partWiseForEachRoundDtoList.add(partWiseForEachRoundDto);
                }
                if(rounds1.getRoundType().equals(RoundType.TECHNICAL_INTERVIEW)){
                    RoundWisePercentageDto roundWisePercentageDto=new RoundWisePercentageDto();
                    roundWisePercentageDto.setRoundType(RoundType.TECHNICAL_INTERVIEW);
                    roundWisePercentageDto.setRoundNum(rounds1.getRoundNumber());

                    Interview interview=interviewRepository.findByRoundsAndUser(rounds1,user);
                    roundWisePercentageDto.setResult(interview.getEmployee().getFirstName()+" "+interview.getEmployee().getLastName());

                    roundWisePercentageDtoList.add(roundWisePercentageDto);
                }
                if(rounds1.getRoundType().equals(RoundType.PERSONAL_INTERVIEW)){
                    RoundWisePercentageDto roundWisePercentageDto=new RoundWisePercentageDto();
                    roundWisePercentageDto.setRoundType(RoundType.PERSONAL_INTERVIEW);
                    roundWisePercentageDto.setRoundNum(rounds1.getRoundNumber());

                    Interview interview=interviewRepository.findByRoundsAndUser(rounds1,user);
                    roundWisePercentageDto.setResult(interview.getEmployee().getFirstName()+" "+interview.getEmployee().getLastName());
                    roundWisePercentageDtoList.add(roundWisePercentageDto);
                }
                resultChartDto.setRoundWiseDto(roundWisePercentageDtoList);
                resultChartDto.setPartWiseDto(partWiseForEachRoundDtoList);
            }
            return ResponseEntity.ok(new ResponseDto(Strings.FINAL_RESULT,resultChartDto));
        }catch (Exception e){
            return ResponseEntity.internalServerError().body(new ResponseDto(e.getMessage(),null));
        }
    }

    public Map<String, Double> getStringDoubleMap(Map<Difficulty,Integer> difficultyWise) {
        Map<String, Double> difficultyWisePercentage = new HashMap<>();
        for (Map.Entry<Difficulty, Integer> entry : difficultyWise.entrySet()) {
            Difficulty difficulty = entry.getKey();
            Integer integerValue = entry.getValue();
            String stringKey = difficulty.toString();
            Double doubleValue = integerValue.doubleValue();
            difficultyWisePercentage.put(stringKey, doubleValue);
        }
        return difficultyWisePercentage;
    }
}