package com.example.springbootinit.aspect;

import com.example.springbootinit.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

// RestController까지 캐치 // RestAdvice와 분할해서 사용하는게 좋아보임. basePackages 패키지별로 선택가능
@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler(Exception.class)
    public String AuthenticationExceptionReturnResponseEntity(Exception e) {
        ErrorResponse response = ErrorResponse.builder()
                .message(e.getMessage())
                .status(HttpStatus.UNAUTHORIZED.value())
                .code("uniqueCode")
                .build();
        return "/error";
    }

//    @ExceptionHandler(AuthenticationException.class)
//    public ResponseEntity<ErrorResponse> AuthenticationExceptionReturnResponseEntity(AuthenticationException e) {
// // RestController 에서 사용할 예시
//        ErrorResponse response = ErrorResponse.builder()
//                .message(e.getMessage())
//                .status(HttpStatus.UNAUTHORIZED.value())
//                .code("uniqueCode")
//                .build();
//
//        return new ResponseEntity<ErrorResponse>(response, HttpStatus.FORBIDDEN);
//    }
}
