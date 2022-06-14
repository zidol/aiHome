package kr.co.aihome.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.aihome.dto.AuthorityDto;
import kr.co.aihome.entity.author.Gender;
import kr.co.aihome.entity.author.User;
import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDto {

	private Long id;

	@NotNull
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

	private List<AuthorityDto> authorities;
	
	public UserDetailDto(User user) {
		this.id = user.getUserId();
		this.username = user.getUsername();
		this.name = user.getName();
		this.email = user.getEmail();

		this.authorities = user.getAuthorities().stream()
			.map(authority -> new AuthorityDto(authority.getAuthority())).collect(Collectors.<AuthorityDto>toList());
	}
}
