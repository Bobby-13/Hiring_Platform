package com.divum.hiring_platform.dto;

import com.divum.hiring_platform.util.enums.InterviewType;

import java.time.LocalDateTime;

public interface InterviewProjection {
    String getInterviewId();

    InterviewType getInterviewType();

    String getFeedBack();

    LocalDateTime getInterviewTime();

    String getInterviewUrl();

    String getEventId();
}