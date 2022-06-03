package kr.co.aihome.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.aihome.common.dto.ErrorResponse;
import kr.co.aihome.config.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler  {
	
//	private static ExceptionResponse exceptionResponse = 
//            new ExceptionResponse(HttpStatus.FORBIDDEN.value(), "Forbidden!!!", null);


	private static final Logger logger = LoggerFactory.getLogger(JWTUtil.class);
	
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		
		ErrorResponse errorResponse = new ErrorResponse();
		
		errorResponse.setMessage(accessDeniedException.getMessage());
        errorResponse.setPath(request.getRequestURI());
        errorResponse.setError(HttpStatus.FORBIDDEN.getReasonPhrase());
        errorResponse.setStatus(HttpStatus.FORBIDDEN.value());
        errorResponse.setTimestamp(LocalDateTime.now());
       
//        //response에 넣기
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.FORBIDDEN.value());
		ObjectMapper objectMapper = new ObjectMapper();
		response.getOutputStream().write(objectMapper.writeValueAsBytes(errorResponse));
//        try (OutputStream os = response.getOutputStream()) {
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.writeValue(os, errorResponse);
//            os.flush();
//        }
		
	}

}
