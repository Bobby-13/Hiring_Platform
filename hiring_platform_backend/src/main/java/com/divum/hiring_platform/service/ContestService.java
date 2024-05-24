package com.divum.hiring_platform.service;


import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.dto.UserDto;
import com.divum.hiring_platform.entity.Contest;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ContestService {
    
    ResponseEntity<ResponseDto> createContest(Contest contest);

    ResponseEntity<ResponseDto> updateContest(String contestId, Contest contest);

    ResponseEntity<ResponseDto> assignUser(String contestId, List<UserDto> users) throws MessagingException;

    ResponseEntity<ResponseDto> deleteContest(String contestId);


    ResponseEntity<ResponseDto> getAllContest(String required);

    ResponseEntity<ResponseDto> getContest(String contestId, String required);

    ResponseEntity<ResponseDto> getUsers(String roundId, Integer passmark);

    ResponseEntity<ResponseDto> assignQuestion(String contestId, Boolean reassign) throws MessagingException;

    ResponseEntity<ResponseDto> finalResult(String contestId, String roundId, String finalResult);

    ResponseEntity<ResponseDto> adminHomePage();

}
