package kr.co.aihome.service.role;

import kr.co.aihome.dto.role.RoleDto;

import java.util.List;

/**
 * 권한 service interface
 * @author sppartners
 *
 */
public interface RoleService {

	List<RoleDto> findAllRole();

}
