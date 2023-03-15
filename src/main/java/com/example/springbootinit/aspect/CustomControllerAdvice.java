package com.example.springbootinit.aspect;

import com.example.springbootinit.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // RestController까지 캐치 // RestAdvice와 분할해서 사용하는게 좋아보임.
public class CustomControllerAdvice {

    @ExceptionHandler(AuthenticationException.class)
    public String AuthenticationExceptionReturnResponseEntity(AuthenticationException e) {
        ErrorResponse response = ErrorResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .code("uniqueCode")
                .build();
        System.out.println("오남?");
        return "error";
    }

//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<ErrorResponse> AuthenticationExceptionReturnResponseEntity(AuthenticationException e) {
//        ErrorResponse response = ErrorResponse.builder()
//                .message(e.getMessage())
//                .status(HttpStatus.UNAUTHORIZED.value())
//                .code("uniqueCode")
//                .build();
//
//        return new ResponseEntity<ErrorResponse>(response, HttpStatus.FORBIDDEN);
//    }
}
