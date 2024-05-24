package com.divum.hiring_platform.repository.service;

import com.divum.hiring_platform.entity.Notification;

import java.util.List;

public interface NotificationRepositoryService{
    Notification save(Notification notification);

    List<Notification> findAll();

}
