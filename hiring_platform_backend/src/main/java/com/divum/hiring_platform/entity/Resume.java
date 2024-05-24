package com.divum.hiring_platform.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data

@Entity
@Table(name = "user_educational_details")
public class Resume {

    @Id
    @OneToOne
    @JsonIgnore
    private User user;

    private String department;
    private String resumeUrl;
    private int yearOfGraduation;

    @Override
    public String toString() {
        return "Resume{" +
                "user=" + user.getUserId() +
                ", department='" + department + '\'' +
                ", resumeUrl='" + resumeUrl + '\'' +
                ", yearOfGraduation=" + yearOfGraduation +
                '}';
    }
}
