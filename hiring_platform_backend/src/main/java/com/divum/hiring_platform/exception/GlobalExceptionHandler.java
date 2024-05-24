package com.divum.hiring_platform.exception;


import com.divum.hiring_platform.dto.ResponseDto;
import com.divum.hiring_platform.util.EmailSender;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final EmailSender emailSender;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDto> errorHandle(ResourceNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ResponseDto(e.getMessage(), null));
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ResponseDto> invalidDataExceptionHandler(InvalidDataException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDto(e.getMessage(), null));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> exceptionHandler(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseDto(e.getMessage(), null));
    }

    @ExceptionHandler(MessagingException.class)
    public void mailExceptionHandler(MessagingException e) throws MessagingException {
        String errorMessage = "An error occurred while sending an email: " + e.getMessage();
        emailSender.emailToAdmin("Error Sending Email", errorMessage);
    }



}
