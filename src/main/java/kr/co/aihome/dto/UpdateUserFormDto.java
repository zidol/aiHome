package kr.co.aihome.dto;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateUserFormDto {
	@NotBlank(message = "아이디는 필수 입력 값입니다.")
	@Size(min = 3, max = 50)
	private String username;
	
	@NotBlank(message = "이름은 필수 입력 값입니다.")
	private String name;
	
	@NotBlank(message = "이메일은 필수 입력 값입니다.")
	@Email(message = "이메일 형식에 맞지 않습니다.")
    private String email;

    private String password;
    
    private String rePassword;
    
    private String dept;
    
    private String position;
    
    private String education;
    
    private String officeNum;
    
    @NotBlank(message = "전화번호는 필수 입력 값입니다.")
    private String mobile;
    
//    @NotNull(message = "권한은 1개 이상 등록되어야 합니다(1)")
//    @Size(min = 1, message = "권한은 1개 이상 등록되어야 합니다(2)")
    private String authorities;
}
