package com.divum.hiring_platform.util;

import com.divum.hiring_platform.entity.EmailTask;
import com.divum.hiring_platform.repository.service.EmailTaskRepositoryService;
import com.divum.hiring_platform.util.enums.TaskStatus;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class Scheduler {

    private final EmailTaskRepositoryService emailTaskRepository;
    private final EmailSender emailSender;

    @Scheduled(fixedRateString = "${scheduler.fixed-rate}")
    public void schedule() throws MessagingException {
        List<EmailTask> emailTasks = emailTaskRepository.findEmailTasksByTaskTimeAfterAndTaskTimeBefore(LocalDateTime.now(), LocalDateTime.now().plusMinutes(5));
        for (EmailTask emailTask : emailTasks) {
            String emailException = null;
            if (emailTask.getTaskStatus() == TaskStatus.PENDING) {
                try {
                    emailSender.sendEmailToTheContestantAboutTheRound(emailTask);
                    emailTask.setTaskStatus(TaskStatus.SUCCESS);
                } catch (Exception e) {
                    emailTask.setTaskStatus(TaskStatus.RETRY);
                }
            }
            if (emailTask.getTaskStatus() == TaskStatus.RETRY) {
                try {
                    emailSender.sendEmailToTheContestantAboutTheRound(emailTask);
                } catch (Exception e) {
                    emailException = e.getMessage();
                    emailTask.setTaskStatus(TaskStatus.FAILED);
                }
            }
            if (emailTask.getTaskStatus() == TaskStatus.FAILED) {
                emailSender.sendEmailToTheAdminAboutTheError(emailTask, emailException);
            }
            emailTaskRepository.save(emailTask);
        }
    }

}
