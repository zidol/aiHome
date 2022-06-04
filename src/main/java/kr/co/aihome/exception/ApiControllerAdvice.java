package kr.co.aihome.exception;


import kr.co.aihome.common.dto.Error;
import kr.co.aihome.common.dto.ErrorResponse;
import kr.co.aihome.exception.customException.IdDuplicationException;
import kr.co.aihome.exception.customException.PasswordDifferentException;
import kr.co.aihome.exception.customException.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


@RestControllerAdvice//(basePackageClasses = ApiController.class)//특정 클래스에서만 적용 할때
public class ApiControllerAdvice {
	
	private static final Logger logger = LoggerFactory.getLogger(ApiControllerAdvice.class);

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> exception(Exception e , HttpServletRequest request) {
//        System.out.println();
    	e.printStackTrace();
        ErrorResponse errorResponse = new ErrorResponse();
        
        errorResponse.setMessage(e.getMessage());
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setTimestamp(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> methodArgumentNotValidException(MethodArgumentNotValidException e
    		, HttpServletRequest request){

        List<Error> errorList = new ArrayList<>();

        BindingResult bindingResult = e.getBindingResult();

        bindingResult.getAllErrors().forEach(error -> {
            FieldError field = (FieldError) error;

            String fieldName = field.getField();
            String message = field.getDefaultMessage();
            String value = field.getRejectedValue().toString();

            Error errorMessage = new Error();
            errorMessage.setField(fieldName);
            errorMessage.setMessage(message);
            errorMessage.setInvalidValue(value);
            errorList.add(errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorList(errorList);
        errorResponse.setMessage("");
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    @ExceptionHandler(value = BindException.class)
    public ResponseEntity<ErrorResponse> bindException(BindException e
    		, HttpServletRequest request){

        List<Error> errorList = new ArrayList<>();

        BindingResult bindingResult = e.getBindingResult();

        bindingResult.getAllErrors().forEach(error -> {
            FieldError field = (FieldError) error;

            String fieldName = field.getField();
            String message = field.getDefaultMessage();
            String value = field.getRejectedValue().toString();

            Error errorMessage = new Error();
            errorMessage.setField(fieldName);
            errorMessage.setMessage(message);
            errorMessage.setInvalidValue(value);
            errorList.add(errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorList(errorList);
        errorResponse.setMessage("");
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity constraintValidationException (ConstraintViolationException e, HttpServletRequest request) {
        List<Error> errorList = new ArrayList<>();


        e.getConstraintViolations().forEach(error -> {

            Stream<Path.Node> stream = StreamSupport.stream(error.getPropertyPath().spliterator(), false);

            List<Path.Node> list = stream.collect(Collectors.toList());

            String field = list.get(list.size() -1).getName();
            String message = error.getMessage();
            String invalidValue = error.getInvalidValue().toString();


            Error errorMessage = new Error();
            errorMessage.setField(field);
            errorMessage.setMessage(message);
            errorMessage.setInvalidValue(invalidValue);
            errorList.add(errorMessage);
        });

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorList(errorList);
        errorResponse.setMessage("");
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setError(HttpStatus.BAD_REQUEST.toString());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity missingServletRequestParameterException(MissingServletRequestParameterException e
    		, HttpServletRequest request) {

        List<Error> errorList = new ArrayList<>();

        String fieldName = e.getParameterName();
        String message = e.getMessage();

        Error errorMessage = new Error();
        errorMessage.setField(fieldName);
        errorMessage.setMessage(message);
        errorList.add(errorMessage);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorList(errorList);
        errorResponse.setMessage(message);
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setTimestamp(LocalDateTime.now());
    

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }
    
    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity tokenExpiredException(NotFoundException e
    		, HttpServletRequest request) {
  
    	logger.info("요청하신 url은 없는 url입니다.");
        List<Error> errorList = new ArrayList<>();

        String message = e.getMessage();

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorList(errorList);
        errorResponse.setMessage("잘못된 url요청입니다.");
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    
    @ExceptionHandler(value = UnauthorizedException.class)
    public ResponseEntity unauthorizedException(UnauthorizedException e
    		, HttpServletRequest request) {
  
    	logger.info("요청하신 url은 없는 url입니다.");
        List<Error> errorList = new ArrayList<>();

        String message = e.getMessage();

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorList(errorList);
        errorResponse.setMessage("아이디 혹은 비밀번호가 틀렸습니다.");
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
        errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
        errorResponse.setTimestamp(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }
    
    
    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public ResponseEntity maxUploadSizeExceededException(MaxUploadSizeExceededException e
    		, HttpServletRequest request) {

        List<Error> errorList = new ArrayList<>();

        String message = e.getMessage();

        Error errorMessage = new Error();
        errorMessage.setMessage(message);
        errorList.add(errorMessage);

        ErrorResponse errorResponse = new ErrorResponse();
//        errorResponse.setErrorList(errorList);
        errorResponse.setMessage("파일 업로드는 100MB 이하만 가능합니다");
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setError("MaxUploadSizeExceeded");
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setTimestamp(LocalDateTime.now());
    

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    
    @ExceptionHandler(value = IdDuplicationException.class)
    public ResponseEntity idDuplicationException(IdDuplicationException e
    		, HttpServletRequest request) {

        List<Error> errorList = new ArrayList<>();

        String message = e.getMessage();

        Error errorMessage = new Error();
        errorMessage.setMessage(message);
        errorList.add(errorMessage);

        ErrorResponse errorResponse = new ErrorResponse();
//        errorResponse.setErrorList(errorList);
        errorResponse.setMessage("존재하는 아이디입니다.");
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setError("IdDuplication");
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setTimestamp(LocalDateTime.now());
    

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
    
    @ExceptionHandler(value = PasswordDifferentException.class)
    public ResponseEntity passwordDifferentException(PasswordDifferentException e
    		, HttpServletRequest request) {

        List<Error> errorList = new ArrayList<>();

        String message = e.getMessage();

        Error errorMessage = new Error();
        errorMessage.setMessage(message);
        errorList.add(errorMessage);

        ErrorResponse errorResponse = new ErrorResponse();
//        errorResponse.setErrorList(errorList);
        errorResponse.setMessage("입력하신 비밀번호가 다릅니다.");
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setError("PasswordDifferent");
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.setTimestamp(LocalDateTime.now());
    

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}


