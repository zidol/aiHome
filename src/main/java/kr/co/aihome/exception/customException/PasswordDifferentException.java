package kr.co.aihome.exception.customException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Internal Server Error")
public class PasswordDifferentException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public PasswordDifferentException(String message) {
		super(message);
	}
}
