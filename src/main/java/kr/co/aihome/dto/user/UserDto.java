package kr.co.aihome.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.aihome.entity.author.Authority;
import kr.co.aihome.entity.author.Gender;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

/**
 * 회원 가입용
 *
 */

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

	private Long id;

	@NotBlank
	@Size(min = 3, max = 50)
	private String username;

	@NotBlank
	private String name;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	@NotBlank
	@Size(min = 3, max = 100)
	private String password;

	@NotBlank
	@Size(min = 3, max = 50)
	private String email;

	@Min(value = 1)
	private int age;

	@NotNull
	private Gender gender;

	@NotNull
	@Min(value = 1)
	private Double weight;

	private List<Authority> authorities;

}