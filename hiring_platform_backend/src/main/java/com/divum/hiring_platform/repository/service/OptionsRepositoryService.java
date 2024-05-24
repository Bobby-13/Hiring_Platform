package com.divum.hiring_platform.repository.service;

import com.divum.hiring_platform.entity.Options;

import java.util.List;
import java.util.Optional;

public interface OptionsRepositoryService {

    List<String> getOption(String questionId);

    List<Object[]> getQuestionIdAndAnswers();


    Options findByOption(String option);

    void save(Options options);

    Optional<Options> findById(Long id);
}
