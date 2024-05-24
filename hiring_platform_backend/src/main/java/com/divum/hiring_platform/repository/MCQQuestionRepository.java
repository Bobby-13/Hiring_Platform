package com.divum.hiring_platform.repository;

import com.divum.hiring_platform.dto.McqPaginationDto;
import com.divum.hiring_platform.entity.Category;
import com.divum.hiring_platform.entity.Contest;
import com.divum.hiring_platform.entity.MultipleChoiceQuestion;
import com.divum.hiring_platform.util.enums.Difficulty;
import com.divum.hiring_platform.util.enums.QuestionCategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MCQQuestionRepository extends JpaRepository<MultipleChoiceQuestion, String> {


    @Query("SELECT new com.divum.hiring_platform.dto.McqPaginationDto (m.questionId, m.question, m.category.questionCategory, m.difficulty) FROM MultipleChoiceQuestion m")
    Page<McqPaginationDto> getAllMCQs(Pageable pageable);

    @Query("SELECT new com.divum.hiring_platform.dto.McqPaginationDto(m.questionId, m.question, m.category.questionCategory, m.difficulty) " +
            "FROM MultipleChoiceQuestion m WHERE m.category.questionCategory IN :categories")
    Page<McqPaginationDto> getAllMCQByType(Pageable pageable, @Param("categories") List<QuestionCategory> categories);

    @Query("SELECT new com.divum.hiring_platform.dto.McqPaginationDto(m.questionId, m.question, m.category.questionCategory, m.difficulty) " +
            "FROM MultipleChoiceQuestion m WHERE m.difficulty IN :difficulty")
    Page<McqPaginationDto> getAllMCQByDifficulty(Pageable pageable, List<Difficulty> difficulty);


    @Query("SELECT new com.divum.hiring_platform.dto.McqPaginationDto(m.questionId, m.question, m.category.questionCategory, m.difficulty) " +
            "FROM MultipleChoiceQuestion m WHERE m.difficulty IN :difficulty AND (:category IS NULL OR m.category.questionCategory IN :category)")
    Page<McqPaginationDto> getAllMCQByDifficultyAndType(Pageable pageable, @Param("difficulty") List<Difficulty> difficulty, @Param("category") List<QuestionCategory> category);


    @Query("SELECT q FROM MultipleChoiceQuestion q " +
            "WHERE q.category.categoryId = ?1 " +
            "AND q.difficulty = ?2 " +
            "AND q NOT IN (SELECT rq.contestAndMcq.multipleChoiceQuestion FROM RoundAndMcqQuestion rq WHERE rq.contestAndMcq.contest = ?3) " +
            "ORDER BY RANDOM() LIMIT ?4")
    MultipleChoiceQuestion getRandomQuestion(int categoryId, Difficulty difficulty, Contest contest, int limit);


    @Query("SELECT COUNT(question) FROM MultipleChoiceQuestion question WHERE question.category =?1 AND question.difficulty =?2")
    Integer getQuestionCountDifficultyWise(Category category, Difficulty difficulty);

}
