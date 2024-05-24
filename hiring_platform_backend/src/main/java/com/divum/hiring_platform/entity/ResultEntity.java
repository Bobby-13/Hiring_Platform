package com.divum.hiring_platform.entity;

import com.divum.hiring_platform.util.enums.Result;

public interface ResultEntity {
    String getId();
    String getContestId();
    String getRoundId();
    String getUserId();
    Result getResult();

    float getTotalPercentage();
}
