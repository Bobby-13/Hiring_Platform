package com.divum.hiring_platform.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;


@Entity
@Table(name = "user_details")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String userId;

    @Email
    @Column(unique = true)
    private String email;

    private String name;

    private String password;
    private String collegeName;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "contest_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "contest_id")
    )
    private Set<Contest> contest;

    @OneToOne(mappedBy = "user")
    @JsonIgnore

    private Resume resume;

    private boolean isPassed;


    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", collegeName='" + collegeName + '\'' +
                ", isPassed=" + isPassed +
                '}';
    }
}


