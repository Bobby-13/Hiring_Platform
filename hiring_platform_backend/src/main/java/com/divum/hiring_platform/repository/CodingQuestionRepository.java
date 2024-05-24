package com.divum.hiring_platform.repository;


import com.divum.hiring_platform.dto.GetCodingQnDto;
import com.divum.hiring_platform.entity.Category;
import com.divum.hiring_platform.entity.CodingQuestion;
import com.divum.hiring_platform.entity.Contest;
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
public interface CodingQuestionRepository extends JpaRepository<CodingQuestion, Long> {

    @Query("SELECT q FROM CodingQuestion q WHERE q.category.categoryId =?1 AND q.difficulty =?2 AND q NOT IN (SELECT rq.contestAndCoding.codingQuestion FROM RoundAndCodingQuestion rq WHERE rq.contestAndCoding.contest =?3) ORDER BY RANDOM() LIMIT ?4")
    CodingQuestion getRandomQuestion(int categoryId, Difficulty difficulty, Contest contest, int easy);

    @Query("SELECT new com.divum.hiring_platform.dto.GetCodingQnDto(cq.questionId, cq.question, cq.category.questionCategory, cq.difficulty) " +
            "FROM CodingQuestion cq " +
            "WHERE (:categories IS NULL OR cq.category.questionCategory IN :categories)" +
            "AND (:difficulties IS NULL OR cq.difficulty IN :difficulties)")
    Page<GetCodingQnDto> findCodingQuestionsWithCategoryAndDifficulty(
            @Param("categories") List<QuestionCategory> categories,
            @Param("difficulties") List<Difficulty> difficulties,
            Pageable pageable);

    @Query("SELECT new com.divum.hiring_platform.dto.GetCodingQnDto(cq.questionId, cq.question, cq.category.questionCategory, cq.difficulty) " +
            "FROM CodingQuestion cq " +
            "LEFT JOIN cq.category cat " +
            "WHERE (:categories IS NULL OR cat.questionCategory IN :categories)")
    Page<GetCodingQnDto> findCodingQuestionsByCategory(
            @Param("categories") List<QuestionCategory> categories,
            Pageable pageable);

    @Query("SELECT new com.divum.hiring_platform.dto.GetCodingQnDto(cq.questionId, cq.question, cq.category.questionCategory, cq.difficulty) " +
            "FROM CodingQuestion cq " +
            "WHERE (:difficulties IS NULL OR cq.difficulty IN :difficulties)")
    Page<GetCodingQnDto> findCodingQuestionsByDifficulty(
            @Param("difficulties") List<Difficulty> difficulties,
            Pageable pageable);

    @Query("SELECT new com.divum.hiring_platform.dto.GetCodingQnDto(cq.questionId, cq.question, cq.category.questionCategory, cq.difficulty) " +
            "FROM CodingQuestion cq")
    Page<GetCodingQnDto> findAllQn(Pageable pageable);

    @Query("SELECT COUNT(question) FROM CodingQuestion question WHERE question.category =?1 AND question.difficulty =?2")
    Integer getQuestionCountDifficultyWise(Category category, Difficulty difficulty);


}
