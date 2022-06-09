package kr.co.aihome.dto;

import kr.co.aihome.entity.author.Gender;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class SignUpFormDto {
	
	@NotBlank(message = "아이디는 필수 입력 값입니다.")
	@Size(min = 3, max = 50)
	private String username;
	
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String name;
	
	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	@Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
    
    @NotBlank(message = "확인 비밀번호는 필수 입력 값입니다.")
    private String rePassword;

    private int age;

    private Gender gender;

    private Double weight;
    
    private List<String> authorities;

}
