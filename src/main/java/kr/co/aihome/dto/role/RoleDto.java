package kr.co.aihome.dto.role;

import kr.co.aihome.entity.author.ERole;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class RoleDto {
	
	private ERole authority;
	
	private String description;
}
