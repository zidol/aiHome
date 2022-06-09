package kr.co.aihome.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.querydsl.core.annotations.QueryProjection;
import kr.co.aihome.entity.author.Gender;
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

	private int age;

	private Gender gender;

	private Double weight;
	
	private String createdBy;
	
	private LocalDateTime createdDate;

	@QueryProjection
	public UserAuthorDto(Long id, String username, String name, String password, String email, int age, Gender gender, Double weight, String createdBy, LocalDateTime createdDate) {
		this.id = id;
		this.username = username;
		this.name = name;
		this.password = password;
		this.email = email;
		this.age = age;
		this.gender = gender;
		this.weight = weight;
		this.createdBy = createdBy;
		this.createdDate = createdDate;
	}
}
