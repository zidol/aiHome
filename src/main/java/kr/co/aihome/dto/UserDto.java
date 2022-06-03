package kr.co.aihome.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.aihome.entity.author.Authority;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

/**
 * 회원 가입용
 * @author sppartners
 *
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

	private Long id;

	@NotNull
	@Size(min = 3, max = 50)
	private String username;

	@NotNull
	private String name;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotNull
	@Size(min = 3, max = 100)
	private String password;

	@NotNull
	@Size(min = 3, max = 50)
	private String email;

	private String dept;

	private String position;

	private String education;

	private String officeNum;

	@NotNull
	private String mobile;

	private Set<Authority> authorities;
	
	private String ssoId;

}