package kr.co.aihome.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@ToString
public class UserAuthorDto {

	private Long id;

	@NotNull
	@Size(min = 3, max = 50)
	private String username;

	@NotNull
	private String name;

	@NotNull
	@Size(min = 3, max = 100)
	@JsonIgnore
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
	
	private String createdBy;
	
	private LocalDateTime createdDate;

	//TODO @QueryProjection
	public UserAuthorDto(Long id, @NotNull @Size(min = 3, max = 50) String username, @NotNull String name,
                         @NotNull @Size(min = 3, max = 100) String password, @NotNull @Size(min = 3, max = 50) String email,
                         String dept, String position, String education, String officeNum, @NotNull String mobile, String createdBy,
                         LocalDateTime createdDate) {
		super();
		this.id = id;
		this.username = username;
		this.name = name;
		this.password = password;
		this.email = email;
		this.dept = dept;
		this.position = position;
		this.education = education;
		this.officeNum = officeNum;
		this.mobile = mobile;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
	}

	
	

}
