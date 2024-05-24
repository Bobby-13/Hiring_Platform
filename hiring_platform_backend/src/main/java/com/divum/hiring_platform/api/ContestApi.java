package com.divum.hiring_platform.api;


import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.dto.UserDto;
import com.divum.hiring_platform.entity.Contest;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/v1/contest")
public interface ContestApi {

    @PostMapping
    ResponseEntity<ResponseDto> createContest(@RequestBody Contest contest) throws MessagingException;

    @PostMapping("/{contestId}")
    ResponseEntity<ResponseDto> assignQuestionToTheContest(@PathVariable String contestId, @RequestParam(value = "REASSIGN", required = false) Boolean reassign) throws MessagingException;

    @PutMapping("/{contestId}")
    ResponseEntity<ResponseDto> updateContest(@PathVariable String contestId, @RequestBody Contest contest);

    @PostMapping("{contestId}/users")
    ResponseEntity<ResponseDto> assignUsers(@PathVariable String contestId, @RequestBody List<UserDto> users) throws MessagingException;

    @DeleteMapping("/{contestId}")
    ResponseEntity<ResponseDto> deleteContest(@PathVariable String contestId);

    @GetMapping
    ResponseEntity<ResponseDto> getAllContest(@RequestParam(value = "required") String required);

    @GetMapping("/{contestId}")
    ResponseEntity<ResponseDto> getContest(@PathVariable String contestId, @RequestParam(value = "required") String required);

    @GetMapping("/user")
    ResponseEntity<ResponseDto> getUsers(
            @RequestParam(value = "roundId", required = false) String roundId,
            @RequestParam(value = "passMark", required = false) Integer passMark
    );

    @GetMapping("/{contestId}/result")
    ResponseEntity<ResponseDto> finalResult(@PathVariable String contestId, @RequestParam(value = "roundId", required = false)String roundId, @RequestParam(value = "finalResult") String finalResult);

    @GetMapping("/admin/home")
    ResponseEntity<ResponseDto> adminHomePage();
}