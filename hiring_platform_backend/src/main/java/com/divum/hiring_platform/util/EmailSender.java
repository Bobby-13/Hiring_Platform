package com.divum.hiring_platform.util;

import com.divum.hiring_platform.dto.EmailStructure;
import com.divum.hiring_platform.dto.EmployeeInterviewScheduleMail;
import com.divum.hiring_platform.entity.*;
import com.divum.hiring_platform.repository.service.*;
import com.divum.hiring_platform.strings.Strings;
import com.divum.hiring_platform.util.enums.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@RequiredArgsConstructor
public class EmailSender {

    @Value("${email.sender}")
    private String senderEmail;
    @Value("${email.link.employee-availability}")
    private String employeeAvailabilityLink;
    @Value("${email.template.welcome-email}")
    private String welcomeMail;
    @Value("${email.template.first-round-mail}")
    private String roundMail;
    @Value("${email.template.result-mail}")
    private String resultMail;
    @Value("${default.password}")
    private String defaultPassword;
    @Value("${email.template.response-mail}")
    private String employeeResponseMail;
    @Value("${email.template.employee-interview-notification}")
    private String employeeInterviewNotification;
    @Value("${email.template.interview-reschedule-contestant}")
    private String interviewRescheduleNotificationToContestant;
    @Value("${email.link.password-reset}")
    private String passwordResetBaseLink;
    @Value("${email.template.interview-result}")
    private String interviewResultTemplate;
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    private final TemplateEngine templateEngine;
    private final JavaMailSender javaMailSender;
    private final JwtUtil jwtUtil;
    private final ContestRepositoryService contestRepository;
    private final UserRepositoryService userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MCQResultRepositoryService mcqResultRepositoryService;
    private final CodingResultRepositoryService codingResultRepositoryService;
    private final RoundsRepositoryService roundsRepository;
    private final InterviewRepositoryService interviewRepository;


    public void sendEmailToTheContestantAboutTheRound(EmailTask emailTask) throws MessagingException {
        Rounds round = emailTask.getRounds();
        Contest contest = round.getContest();
        List<User> users = new ArrayList<>();
        boolean isWelcomeEmail = false;
        boolean isFirstRoundMail = false;
        boolean hasPasswordChanged = false;
        if(round.getStartTime().isEqual(emailTask.getTaskTime().plusHours(12))) {
            isWelcomeEmail = true;
            users = contestRepository.findUsersAssignedToTheContest(contest.getContestId());
        } else if (round.getStartTime().isEqual(emailTask.getTaskTime().plusMinutes(30))){
            isFirstRoundMail = true;
            users = contestRepository.findUsersAssignedToTheContest(contest.getContestId());
        } else {
            updateResult(round, users);
        }
        EmailStructure emailStructure = new EmailStructure();
        emailStructure.setSender(senderEmail);
        EmailStructure adminResultNotification = new EmailStructure();
        adminResultNotification.setSender(senderEmail);
        adminResultNotification.setReceiver(senderEmail);
        for(User user : users) {
            Context context;
            String text;
            if(isWelcomeEmail) {
                context = getContextForWelcomeMail(user.getName(), user.getEmail(), defaultPassword, contest.getRounds().get(0).getStartTime());
                text = templateEngine.process(welcomeMail, context);
                emailStructure.setSubject(Strings.ASSESSMENT_DETAILS);
            } else if (isFirstRoundMail){
                hasPasswordChanged = !passwordEncoder.matches(defaultPassword, user.getPassword());
                context = getContextForRoundEmail(hasPasswordChanged, round);
                text  = templateEngine.process(roundMail, context);
                emailStructure.setSubject(Strings.ASSESSMENT_DETAILS);
                user.setPassed(true);
            } else {
                Rounds nextRound = Objects.requireNonNull(getNextRound(round));
                context = getContextForResultEmail(round, nextRound, user);
                text = templateEngine.process(resultMail, context);
                emailStructure.setSubject(Strings.ROUND_RESULT);
            }
            emailStructure.setReceiver(user.getEmail());
            emailStructure.setText(text);
            sendEmail(emailStructure);
        }
        if(isWelcomeEmail) {
            contest.setContestStatus(ContestStatus.CURRENT);
        }
        userRepository.saveAll(users);
        contestRepository.save(contest);
    }
    public void updateResult(Rounds round, List<User> users) {
        int passCount = 0;
        if(round.getRoundType().equals(RoundType.MCQ)) {
            List<MCQResult> results = mcqResultRepositoryService.findMCQResultsByRoundId(round.getId());
            for(MCQResult result : results) {
                Optional<User> optionalUser = userRepository.findById(result.getUserId());
                if(optionalUser.isEmpty()) {
                    throw new ResourceNotFoundException("User not found");
                }
                User user = optionalUser.get();
                if(round.getPassPercentage() <= result.getTotalPercentage()) {
                    result.setResult(Result.PASS);
                    user.setPassed(true);
                    passCount++;
                } else {
                    result.setResult(Result.FAIL);
                    user.setPassed(false);
                }
                users.add(user);
                round.setParticipantsCounts(results.size());
            }
            round.setPassCount(passCount);
            mcqResultRepositoryService.saveAll(results);
        } else if (round.getRoundType().equals(RoundType.CODING)) {
            List<CodingResult> results = codingResultRepositoryService.findCodingResultsByRoundId(round.getId());
            for(CodingResult result : results) {
                Optional<User> optionalUser = userRepository.findById(result.getUserId());
                if(optionalUser.isEmpty()) {
                    throw new ResourceNotFoundException("User not found");
                }
                User user = optionalUser.get();
                if(round.getPassPercentage() <= result.getTotalPercentage()) {
                    result.setResult(Result.PASS);
                    user.setPassed(true);
                    passCount++;
                } else {
                    result.setResult(Result.FAIL);
                    user.setPassed(false);
                }
                users.add(user);
            }
            round.setParticipantsCounts(results.size());
            round.setPassCount(passCount);
            codingResultRepositoryService.saveAll(results);
        }
        roundsRepository.save(round);
        userRepository.saveAll(users);
    }

    public Rounds getNextRound(Rounds currentRound) {
        int nextRoundNumber = 1 + currentRound.getRoundNumber();
        for(Rounds round: currentRound.getContest().getRounds()) {
            if(round.getRoundNumber() == nextRoundNumber) return round;
        }
        return null;
    }

    public Context getContextForResultEmail(Rounds round, Rounds nextRound, User user) {
        boolean isNextRoundIsInterview = nextRound != null && (nextRound.getRoundType() == RoundType.PERSONAL_INTERVIEW || nextRound.getRoundType() == RoundType.TECHNICAL_INTERVIEW);
        Context context = new Context();
        context.setVariable(Strings.NAME, user.getName());
        context.setVariable(Strings.IS_PASSED, user.isPassed());
        if(!isNextRoundIsInterview && nextRound != null) {
            context.setVariable(Strings.START_DATE, dateFormatter(nextRound.getStartTime()));
            context.setVariable(Strings.START_TIME, timeFormatter(nextRound.getStartTime()));
            context.setVariable(Strings.END_DATE, timeFormatter(nextRound.getEndTime()));
            context.setVariable(Strings.END_TIME, timeFormatter(nextRound.getEndTime()));
        }
        context.setVariable(Strings.IS_NEXT_ROUND_IS_INTERVIEW, isNextRoundIsInterview);
        String categories = getCategoryString(round.getParts());
        context.setVariable(Strings.CATEGORIES, categories);
        return  context;
    }
    public String getCategoryString(List<Part> parts) {
        StringBuilder categoryBuilder = new StringBuilder();
        for(Part part : parts) {
            categoryBuilder.append(part.getCategory().getQuestionCategory().toString()).append(", ");
            if(!categoryBuilder.isEmpty()) {
                categoryBuilder.setLength(categoryBuilder.length() - 2);
            }
        }
        return categoryBuilder.toString();
    }


    public Context getContextForRoundEmail(boolean hasPasswordChanged, Rounds round) {
        Context context = new Context();
        String startDate = dateFormatter(round.getStartTime());
        String startTime = timeFormatter(round.getStartTime());

        String endDate = dateFormatter(round.getEndTime());
        String endTime = timeFormatter(round.getEndTime());
        context.setVariable(Strings.START_DATE, startDate);
        context.setVariable(Strings.START_TIME, startTime);
        context.setVariable(Strings.END_DATE, endDate);
        context.setVariable(Strings.END_TIME, endTime);
        context.setVariable(Strings.HAS_CHANGED_PASSWORD, hasPasswordChanged);
        String categories = getCategoryString(round.getParts());
        context.setVariable(Strings.CATEGORIES, categories);
        return context;
    }

    public Context getContextForWelcomeMail(String name, @jakarta.validation.constraints.Email String email, String password, LocalDateTime dateTime) {
        Context context = new Context();
        context.setVariable(Strings.START_TIME,dateTime);
        context.setVariable(Strings.NAME, name);
        context.setVariable(Strings.USERNAME, email);
        context.setVariable(Strings.DEFAULT_PASSWORD, password);
        return context;
    }
    public String dateFormatter(LocalDateTime localDateTime) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(Strings.DATE_FORMAT4);
        return localDateTime.format(dateFormatter);
    }

    public String timeFormatter(LocalDateTime localDateTime) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Strings.TIME_FORMAT);
        return localDateTime.format(timeFormatter);
    }

    public MimeMessage getMimeMessage() {
        return javaMailSender.createMimeMessage();
    }
    public void sendEmail(EmailStructure emailStructure) throws MessagingException {
        MimeMessage message = getMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, Strings.UTF_8_ENCODING);
        helper.setPriority(1);
        helper.setTo(emailStructure.getReceiver());
        helper.setSubject(emailStructure.getSubject());
        helper.setFrom(emailStructure.getSender());
        helper.setText(emailStructure.getText(), true);
        javaMailSender.send(message);
    }
    public void sendEmailToTheAdminAboutTheError(EmailTask emailTask, String emailException) throws MessagingException {
        EmailStructure emailStructure = new EmailStructure();
        emailStructure.setReceiver(senderEmail);
        emailStructure.setSender(senderEmail);
        emailStructure.setSubject(Strings.NOTIFICATION_MAIL_FAILED);
        emailStructure.setText("Error occurred while sending mail to the contestants for the contest " + emailTask.getRounds().getContest().getName() + " for round " + emailTask.getRounds().getRoundNumber() + "\n The error is " + emailException);
        sendEmail(emailStructure);
        emailTask.setTaskStatus(TaskStatus.ADMIN_NOTIFIED);
    }

    public void notifyAboutOneOnOne(List<EmployeeAvailability> availabilities) throws MessagingException {
        for(EmployeeAvailability availability : availabilities) {
            EmailStructure emailStructure = new EmailStructure();
            emailStructure.setReceiver(availability.getEmployeeAndContest().getEmployee().getEmail());
            emailStructure.setSubject("Availability Inquiry: One-on-One Round with Contestants");
            emailStructure.setSender(senderEmail);
            Context context = getContestForEmployeeOneOnOneRequest(availability);
            String text = templateEngine.process(employeeResponseMail, context);
            emailStructure.setText(text);
            sendEmail(emailStructure);
        }
    }

    public Context getContestForEmployeeOneOnOneRequest(EmployeeAvailability availability) {
        Context context = new Context();
        String name = availability.getEmployeeAndContest().getEmployee().getFirstName() + " " + availability.getEmployeeAndContest().getEmployee().getLastName();
        context.setVariable("employeeName", name);
        String acceptToken = jwtUtil.createEmployeeAvailabilityToken(availability, "ACCEPT");
        String rejectToken = jwtUtil.createEmployeeAvailabilityToken(availability, "REJECT");
        context.setVariable("acceptLink", employeeAvailabilityLink + Strings.TOKEN_PARAMETER + acceptToken);
        context.setVariable("rejectLink", employeeAvailabilityLink + Strings.TOKEN_PARAMETER + rejectToken);
        return context;
    }

    public void sendConfirmationMail(EmployeeAvailability employeeAvailability) throws MessagingException {
        EmailStructure emailStructure = new EmailStructure();
        emailStructure.setSender(senderEmail);
        String name = employeeAvailability.getEmployeeAndContest().getEmployee().getFirstName() + " " + employeeAvailability.getEmployeeAndContest().getEmployee().getLastName();
        emailStructure.setReceiver(employeeAvailability.getEmployeeAndContest().getEmployee().getEmail());
        if(employeeAvailability.getResponse() == EmployeeResponse.AVAILABLE){
            emailStructure.setSubject("Confirmation of Your Response");
            emailStructure.setText("Dear " + name + ",\n\nYour response to the one-on-one interview availability has been confirmed.\n\nContest Name: "+ employeeAvailability.getEmployeeAndContest().getContest().getName() + "\nDecision: Accepted\n\nThank you for your confirmation.\n\nBest regards,\nDIVUM");
        } else {
            emailStructure.setSender("Confirmation of Your Response");
            emailStructure.setSubject("Dear " + name + ",\n\nYour response to the one-on-one interview availability has been confirmed.\n\nContest Name: " + employeeAvailability.getEmployeeAndContest().getContest().getName() + "\nDecision: Rejected\n\nThank you for your confirmation.\n\nBest regards,\nDIVUM");
        }
        sendEmail(emailStructure);
    }


    public void sendEmailAboutTheInterview(List<EmployeeInterviewScheduleMail> mails) {
        for (EmployeeInterviewScheduleMail mail : mails) {
            executorService.submit(() -> {
                EmailStructure employeeMail = new EmailStructure();
                employeeMail.setSender(senderEmail);
                employeeMail.setSubject(Strings.INTERVIEW_SCHEDULE_NOTIFICATION);
                Context context = getEmployeeNotificationContext(mail);
                String text = templateEngine.process(employeeInterviewNotification, context);
                employeeMail.setText(text);
                employeeMail.setReceiver(mail.getEmployee().getEmail());
                try {
                    sendEmail(employeeMail);
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
    public Context getEmployeeNotificationContext(EmployeeInterviewScheduleMail mail) {
        Context context = new Context();
        String name = mail.getEmployee().getFirstName() + " " + mail.getEmployee().getLastName();
        context.setVariable("name", name);
        context.setVariable(Strings.NAME, name);

        List<Map<String, String>> intervieweeDetailsList = new ArrayList<>();
        for (Map.Entry<User, LocalDateTime> entry : mail.getUsersAndInterviewTime().entrySet()) {
            Map<String, String> intervieweeDetails = new HashMap<>();
            User user = entry.getKey();
            LocalDateTime interviewTime = entry.getValue();
            String intervieweeName = user.getName();
            String formattedInterviewTime = interviewTime.format(DateTimeFormatter.ofPattern(Strings.DATE_TIME_FORMAT));
            intervieweeDetails.put(Strings.NAME, intervieweeName);
            intervieweeDetails.put(Strings.INTERVIEW_TIME, formattedInterviewTime);
            intervieweeDetailsList.add(intervieweeDetails);
        }
        Comparator<Map<String, String>> dateTimeComparator = (map1, map2) -> {
            LocalDateTime dateTime1 = LocalDateTime.parse(map1.get(Strings.INTERVIEW_TIME), DateTimeFormatter.ofPattern(Strings.DATE_TIME_FORMAT));
            LocalDateTime dateTime2 = LocalDateTime.parse(map2.get(Strings.INTERVIEW_TIME), DateTimeFormatter.ofPattern(Strings.DATE_TIME_FORMAT));
            return dateTime1.compareTo(dateTime2);
        };
        intervieweeDetailsList.sort(dateTimeComparator);
        context.setVariable("intervieweeDetailsList", intervieweeDetailsList);

        return context;
    }

    public void sendEmailToTheContestantAndEmployeeAboutTheReschedule(Interview interview) throws MessagingException {
        EmailStructure contestantEmail = new EmailStructure();
        EmailStructure employeeEmail = new EmailStructure();

        contestantEmail.setSender(senderEmail);
        employeeEmail.setSender(senderEmail);

        contestantEmail.setSubject("Interview Rescheduled");
        employeeEmail.setSubject("Interview Rescheduled");

        contestantEmail.setReceiver(interview.getUser().getEmail());
        employeeEmail.setReceiver(interview.getEmployee().getEmail());

        Context contestantContext = new Context();
        contestantContext.setVariable(Strings.INTERVIEW_TIME, interview.getInterviewTime().format(DateTimeFormatter.ofPattern(Strings.DATE_TIME_FORMAT2)));
        contestantContext.setVariable("recipientType", "contestant");
        String contestantText = templateEngine.process(interviewRescheduleNotificationToContestant, contestantContext);
        Context employeeContext = new Context();
        employeeContext.setVariable(Strings.INTERVIEW_TIME, interview.getInterviewTime().format(DateTimeFormatter.ofPattern(Strings.DATE_TIME_FORMAT2)));
        employeeContext.setVariable("recipientType", "employee");
        String employeeText = templateEngine.process(interviewRescheduleNotificationToContestant, employeeContext);

        contestantEmail.setText(contestantText);
        employeeEmail.setText(employeeText);

        sendEmail(employeeEmail);
        sendEmail(contestantEmail);
    }

    public void sendEmailToTheEmployeeToResetThePassword(Employee employee, String token) throws MessagingException {
        EmailStructure emailStructure = new EmailStructure();
        emailStructure.setSubject("Password reset");
        emailStructure.setSender(senderEmail);
        emailStructure.setReceiver(employee.getEmail());
        emailStructure.setText("You can reset the password using below link \n " + passwordResetBaseLink + Strings.TOKEN_PARAMETER + token);
        sendEmail(emailStructure);
    }

    public void sendEmailToTheEmployeeAboutPasswordChange(Employee employee) throws MessagingException {
        EmailStructure emailStructure = new EmailStructure();
        emailStructure.setReceiver(employee.getEmail());
        emailStructure.setSubject(Strings.PASSWORD_RESET_EMAIL);
        emailStructure.setSender(senderEmail);
        emailStructure.setText(Strings.PASSWORD_CHANGE_SUCCESS);
        sendEmail(emailStructure);
    }

    public void sendEmailToEmployeeAboutRejection(Interview interview) throws MessagingException {
        EmailStructure emailStructure = new EmailStructure();
        emailStructure.setReceiver(interview.getEmployee().getEmail());
        emailStructure.setSubject("Reschedule request : Rejected");
        emailStructure.setText("Dear " + interview.getEmployee().getFirstName() + ",\n your interview reschedule request has been rejected.\n The original schedule remains unchanged.");
        emailStructure.setSender(senderEmail);
        sendEmail(emailStructure);
    }

    public void sendEmailToEmployeeAboutReassign(Interview interview, Employee requestedEmployee) throws MessagingException {
        EmailStructure requestedEmployeeEmail = generateEmailStructure(requestedEmployee.getEmail(),
                "Reschedule request : Reassigned",
                "Dear " + requestedEmployee.getFirstName() + ",\n\nYour interview reschedule request has been reassigned.");
        sendEmail(requestedEmployeeEmail);

        EmailStructure assignedEmployeeEmail = generateEmailStructure(interview.getEmployee().getEmail(),
                "Reschedule request : Assigned",
                "Dear " + interview.getEmployee().getFirstName() + ",\n\nYou have been assigned to a rescheduled interview. \n Interview timing: " + interview.getInterviewTime().format(DateTimeFormatter.ofPattern("dd/MM/yy : HH:mm")));
        sendEmail(assignedEmployeeEmail);
    }

    public EmailStructure generateEmailStructure(String receiver, String subject, String text) {
        EmailStructure emailStructure = new EmailStructure();
        emailStructure.setSender(senderEmail);
        emailStructure.setReceiver(receiver);
        emailStructure.setSubject(subject);
        emailStructure.setText(text);
        return emailStructure;
    }


    public void sendEmailAboutInterviewResult(Rounds round) throws MessagingException {
        List<Interview> interviews = interviewRepository.getPassedContestants(round);
        int passCount = 0;
        for(Interview interview : interviews) {
            EmailStructure emailStructure = new EmailStructure();
            emailStructure.setReceiver(interview.getUser().getEmail());
            emailStructure.setSubject("Interview Result Notification");
            String text = templateEngine.process(interviewResultTemplate, getInterviewResultContext(interview, passCount));
            emailStructure.setText(text);
            emailStructure.setSender(senderEmail);
            sendEmail(emailStructure);
        }
        round.setPassCount(passCount);
        roundsRepository.save(round);
    }

    public Context getInterviewResultContext(Interview interview, int passCount){
        Context context = new Context();
        boolean isPassed = interview.getInterviewResult() == InterviewResult.SELECTED || interview.getInterviewResult() == InterviewResult.CAN_BE_CONSIDERATE;
        if(isPassed) {
            passCount++;
        }
        context.setVariable(Strings.IS_PASSED, isPassed);
        context.setVariable(Strings.NAME, interview.getUser().getName());
        return context;
    }

    public void emailToAdmin(String subject, String message) throws MessagingException {
        EmailStructure emailStructure = new EmailStructure();
        emailStructure.setReceiver(senderEmail);
        emailStructure.setSender(senderEmail);
        emailStructure.setSubject(subject);
        emailStructure.setText(message);
        sendEmail(emailStructure);
    }
}
