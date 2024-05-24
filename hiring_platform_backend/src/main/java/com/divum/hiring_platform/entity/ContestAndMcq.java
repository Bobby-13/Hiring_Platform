package com.divum.hiring_platform.entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class ContestAndMcq implements Serializable {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contest_id")
    private Contest contest;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_id")
    private MultipleChoiceQuestion multipleChoiceQuestion;
}
