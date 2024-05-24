package com.divum.hiring_platform.service.impl;

import com.divum.hiring_platform.dto.LoginDto;
import com.divum.hiring_platform.dto.LoginResponseDto;
import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.entity.*;
import com.divum.hiring_platform.repository.service.EmployeeRepositoryService;
import com.divum.hiring_platform.repository.service.EmployeeSessionRepositoryService;
import com.divum.hiring_platform.repository.service.UserRepositoryService;
import com.divum.hiring_platform.repository.service.UserSessionRepositoryService;
import com.divum.hiring_platform.service.AuthService;
import com.divum.hiring_platform.strings.Strings;
import com.divum.hiring_platform.util.JwtUtil;
import com.divum.hiring_platform.util.enums.ContestStatus;
import com.divum.hiring_platform.util.enums.Role;
import com.divum.hiring_platform.util.enums.RoundType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepositoryService userRepositoryService;
    private final JwtUtil jwtUtil;
    private final UserSessionRepositoryService userSessionRepositoryService;
    private final EmployeeSessionRepositoryService employeeSessionRepositoryService;
    private final PasswordEncoder passwordEncoder;
    private final EmployeeRepositoryService employeeRepositoryService;


    @Value("${jwt.employee-availability.expiration-time}")
    private Long expDuration;
    @Override
    public ResponseEntity<ResponseDto> login(LoginDto loginDto) {
        try {
            String clientType = loginDto.getClient();
            if (Strings.CONTESTANT.equals(clientType)) {
                return userAuth(loginDto);
            } else if (Strings.EMPLOYEE.equals(clientType)) {
                return employeeAuth(loginDto);
            } else {
                return ResponseEntity.ok().body(new ResponseDto(Strings.INVALID_CLIENT_TYPE, null));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage(), null));
        }
    }

    ResponseEntity<ResponseDto> employeeAuth(LoginDto loginDto) {
        Employee employee=employeeRepositoryService.findEmployeeByEmail(loginDto.getEmail());
        if(employee==null){
            return ResponseEntity.badRequest().body(new ResponseDto(Strings.EMPLOYEE_NOT_FOUND,loginDto.getEmail()));
        }

        String passwordFromFrontend = loginDto.getPassword();
        String hashedPasswordFromDatabase = employee.getPassword();
        boolean passwordsMatch = passwordEncoder.matches(passwordFromFrontend, hashedPasswordFromDatabase);
        if (passwordsMatch) {
            String uniqueId = UUID.randomUUID().toString();
            LocalDateTime expirationDateTime = LocalDateTime.now().plus(expDuration, ChronoUnit.MILLIS);
            String token = jwtUtil.generateToken(uniqueId, expirationDateTime, employee.getRole());
            EmployeeSession employeeSession = new EmployeeSession();
            employeeSession.setLoginTime(LocalDateTime.now());
            employeeSession.setEmail(loginDto.getEmail());
            employeeSession.setUniqueId(uniqueId);
            employeeSessionRepositoryService.save(employeeSession);
            return ResponseEntity.ok(new ResponseDto(Strings.LOGIN_SUCCESS, new LoginResponseDto("Bearer " + token,employee.getEmployeeId().toString(), employee.getRole().toString(),employee.getEmail(),employee.getFirstName(),employee.getLastName())));
        }
        return ResponseEntity.badRequest().body(new ResponseDto(Strings.WRONG_EMAIL_OR_PASSWORD, loginDto));
    }

    ResponseEntity<ResponseDto> userAuth(LoginDto loginDto){
        try {
            User user=userRepositoryService.findUserByEmail(loginDto.getEmail());
            if(user==null){
                return ResponseEntity.ok(new ResponseDto("user not found",loginDto.getEmail()));
            }
            String passwordFromFrontend = loginDto.getPassword();
            String hashedPasswordFromDatabase = user.getPassword();
            boolean passwordsMatch = passwordEncoder.matches(passwordFromFrontend, hashedPasswordFromDatabase);
            if (passwordsMatch && user.isPassed()) {
                Set<Contest> contests = user.getContest();
                Contest currentContest=getCurrentContest(contests);
                if(currentContest==null){
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto(Strings.NO_CURRENT_CONTEST,null));
                }
                List<Rounds> roundsList = currentContest.getRounds();
                Rounds currentRound=getCurrentRound(roundsList);
                if(currentRound==null){
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto(Strings.NO_CURRENT_ROUND, null));
                }
                int count;
                try {
                    List<UserSession> userSession = userSessionRepositoryService.findAllByRoundIdAndEmail(currentRound.getId(), loginDto.getEmail());
                    count=userSession.size();
                }catch (Exception e){
                    count=0;
                }
                if(count<300){
                    String uniqueId=UUID.randomUUID().toString();
                    UserSession userSession1=new UserSession();
                    userSession1.setEmail(loginDto.getEmail());
                    userSession1.setLoginTime(LocalDateTime.now());
                    userSession1.setUniqueId(uniqueId);
                    userSession1.setRoundId(currentRound.getId());
                    userSessionRepositoryService.save(userSession1);
                    String token="";
                    if(currentRound.getRoundType().equals(RoundType.TECHNICAL_INTERVIEW) || currentRound.getRoundType().equals(RoundType.PERSONAL_INTERVIEW )){
                        token = jwtUtil.generateToken(uniqueId, LocalDateTime.now().plusHours(2), Role.CONTESTANTS);
                    }
                    else {
                        token = jwtUtil.generateToken(uniqueId, currentRound.getEndTime(), Role.CONTESTANTS);
                    }
                    return ResponseEntity.ok(new ResponseDto(Strings.LOGIN_SUCCESS, new LoginResponseDto("Bearer " + token,user.getUserId(),Role.CONTESTANTS.name(),user.getEmail(),user.getName(),"")));
                }
                else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto(Strings.LOGIN_LIMIT_EXTENDED,loginDto.getEmail()));
                }
            }else{
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ResponseDto(Strings.WRONG_EMAIL_OR_PASSWORD,loginDto));
            }
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage(),loginDto));
        }
    }

    public Rounds getCurrentRound(List<Rounds> roundsList) {
        Rounds round=new Rounds();
        for (Rounds round1 : roundsList) {
            if (round1.getStartTime().isBefore(LocalDateTime.now()) && round1.getEndTime().isAfter(LocalDateTime.now())) {
                round = round1;
            }
        }
        return round;
    }

    public Contest getCurrentContest(Set<Contest> contests) {
        Contest currentContest=new Contest();
        for (Contest contest : contests) {
            if (contest.getContestStatus().equals(ContestStatus.CURRENT)) {
                currentContest = contest;
            }
        }
        return currentContest;
    }

    @Override
    public ResponseEntity<ResponseDto> logout() {
        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String role = authentication.getAuthorities().toString();
            role = role.substring(1,role.length()-1);
            String uniqueId = authentication.getPrincipal().toString();
            if(role.equals(Role.EMPLOYEE.toString())){
                EmployeeSession employeeSession=employeeSessionRepositoryService.findByUniqueId(uniqueId);
                if(employeeSession.getLogoutTime()==null){
                    employeeSession.setLogoutTime(LocalDateTime.now());
                    employeeSessionRepositoryService.save(employeeSession);
                    return ResponseEntity.ok(new ResponseDto(Strings.LOGGED_OUT,null));
                }
                else{
                    return ResponseEntity.internalServerError().body(new ResponseDto(Strings.SECURITY_ERROR,null));
                }
            }
            if(role.equals(Role.CONTESTANTS.toString())) {
                UserSession userSession = userSessionRepositoryService.findByUniqueId(uniqueId);
                if (userSession.getLogoutTime() == null) {
                    userSession.setLogoutTime(LocalDateTime.now());
                    userSessionRepositoryService.save(userSession);
                    return ResponseEntity.ok(new ResponseDto(Strings.LOGGED_OUT, null));
                } else {
                    return ResponseEntity.internalServerError().body(new ResponseDto(Strings.SECURITY_ERROR, null));
                }
            }
            return ResponseEntity.internalServerError().body(new ResponseDto(Strings.INVALID_ROLE, null));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new ResponseDto(e.getMessage(),null));
        }
    }
}