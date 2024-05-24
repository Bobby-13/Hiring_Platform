package com.divum.hiring_platform.repository;

import com.divum.hiring_platform.entity.InterviewRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterviewRequestRepository extends JpaRepository<InterviewRequest, Long> {

    @Query("SELECT req FROM InterviewRequest req WHERE req.interview.rounds.id =?1")
    List<InterviewRequest> findCurrentInterviewRequest(String id);

}
