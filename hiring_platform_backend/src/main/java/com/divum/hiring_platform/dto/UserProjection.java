package com.divum.hiring_platform.dto;

import com.divum.hiring_platform.entity.Resume;

public interface UserProjection {
    String getUserId();

    String getEmail();

    String getName();

    String getPassword();

    String getCollegeName();

    Resume getResume();

}
