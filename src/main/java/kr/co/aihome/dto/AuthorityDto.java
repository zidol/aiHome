package kr.co.aihome.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthorityDto {

	private String authority;

	public AuthorityDto(String authority) {
		this.authority = authority;
	}
	
}
