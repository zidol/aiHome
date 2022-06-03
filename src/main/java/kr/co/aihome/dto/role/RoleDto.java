package kr.co.aihome.dto.role;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RoleDto {
	
	private String authority;
	
	private String description;
}
