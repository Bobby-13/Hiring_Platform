package com.divum.hiring_platform.service.impl;

import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.repository.ContestRepository;
import com.divum.hiring_platform.service.ContestService;
import com.divum.hiring_platform.service.impl.ContestServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContestServiceImplTest {

    @Mock
    private ContestRepository contestRepository;

    @InjectMocks
    private ContestServiceImpl contestService;

    @Test
    void testCreateContest_WithValidData_ShouldCreateContestSuccessfully() {
        Contest contest = new Contest();
        contest.setName("Test Contest");

        when(contestRepository.save(contest)).thenReturn(contest);

        ResponseEntity<ResponseDto> response = contestService.createContest(contest);

        assertEquals(HttpStatus.NOT_ACCEPTABLE, response.getStatusCode());
    }
}
