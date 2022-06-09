package kr.co.aihome.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import kr.co.aihome.entity.author.User;
import lombok.*;

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
