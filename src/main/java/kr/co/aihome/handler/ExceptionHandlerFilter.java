package kr.co.aihome.handler;

import com.auth0.jwt.exceptions.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.aihome.common.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * jwt 필터 에러 처리
 * @author sppartners
 *
 */
@Slf4j
@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (TokenExpiredException ex) {
			log.error("토큰의 유효기간이 만료 되었습니다.");
			log.error("message : ", ex);
			setErrorResponse(HttpStatus.UNAUTHORIZED, "TokenExpiredError", request, response, ex);
		} catch (JWTDecodeException ex) {
			log.error("토큰의 디코딩 실패하셨습니다.");
			log.error("message : ", ex);
			setErrorResponse(HttpStatus.UNAUTHORIZED, "WrongJWTDecodeError", request, response, ex);
		} catch (SignatureVerificationException ex) {
			log.error("토큰의 시그니쳐가 유효하지 않습니다.");
			log.error("message : ", ex);
			setErrorResponse(HttpStatus.UNAUTHORIZED, "SignatureVerificationError", request, response, ex);
		} catch (InvalidClaimException ex) {
			log.error("토큰의 클레임 정보가 유효하지 않습니다.");
			log.error("message : ", ex);
			setErrorResponse(HttpStatus.UNAUTHORIZED, "InvalidClaimError", request, response, ex);
		} catch (JWTVerificationException ex) {
			log.error("토큰이 잘못 되었습니다.");
			log.error("message : ", ex);
			setErrorResponse(HttpStatus.UNAUTHORIZED, "JWTVerificationError", request, response, ex);
		}
	}

	public void setErrorResponse(HttpStatus status, String message, HttpServletRequest request,
			HttpServletResponse response, Throwable ex) {
		ErrorResponse errorResponse = new ErrorResponse();

		errorResponse.setMessage(message);
		errorResponse.setPath(request.getRequestURI());
		errorResponse.setError(status.getReasonPhrase());
		errorResponse.setStatus(status.value());
		errorResponse.setTimestamp(LocalDateTime.now());

		// response에 넣기
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(status.value());
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			response.getOutputStream().write(objectMapper.writeValueAsBytes(errorResponse));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
