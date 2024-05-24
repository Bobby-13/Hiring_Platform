package com.divum.hiring_platform.repository.service.impl;

import com.divum.hiring_platform.entity.Notification;
import com.divum.hiring_platform.repository.NotificationRepository;
import com.divum.hiring_platform.repository.service.NotificationRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationRepositoryServiceImpl implements NotificationRepositoryService {
    public final NotificationRepository notificationRepository;
    @Override
    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public List<Notification> findAll() {
        return notificationRepository.findAll();
    }

}
