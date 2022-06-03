package kr.co.aihome.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ErrorResponse {

    int status;
    String error;
    String message;
    String path;
    LocalDateTime timestamp;
    List<Error> errorList;

  
}
