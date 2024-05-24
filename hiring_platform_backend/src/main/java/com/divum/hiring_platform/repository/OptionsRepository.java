package com.divum.hiring_platform.repository;


import com.divum.hiring_platform.entity.Options;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionsRepository extends JpaRepository<Options, Long> {

    @Query(value = "SELECT option FROM option WHERE question_id = ?1 AND is_correct = true", nativeQuery = true)
    List<String> getOption(String questionId);

    @Query(value = "SELECT question_id, option FROM option WHERE is_correct = true", nativeQuery = true)
    List<Object[]> getQuestionIdAndAnswers();


    Options findByOption(String option);
}
