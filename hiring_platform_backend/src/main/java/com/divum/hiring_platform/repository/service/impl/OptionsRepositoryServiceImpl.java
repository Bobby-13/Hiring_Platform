package com.divum.hiring_platform.repository.service.impl;

import com.divum.hiring_platform.entity.Options;
import com.divum.hiring_platform.repository.OptionsRepository;
import com.divum.hiring_platform.repository.service.OptionsRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OptionsRepositoryServiceImpl implements OptionsRepositoryService {

    private final OptionsRepository optionsRepository;

    @Override
    public List<String> getOption(String questionId) {
        return optionsRepository.getOption(questionId);
    }

    @Override
    public List<Object[]> getQuestionIdAndAnswers() {
        return optionsRepository.getQuestionIdAndAnswers();
    }


    public Options findByOption(String option) {
        return optionsRepository.findByOption(option);
    }

    @Override
    public void save(Options options) {
        optionsRepository.save(options);
    }

    @Override
    public Optional<Options> findById(Long id) {
        return optionsRepository.findById(id);
    }
}
