package kr.co.aihome.service.role.impl;

import kr.co.aihome.dto.role.RoleDto;
import kr.co.aihome.repository.role.RoleRepository;
import kr.co.aihome.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 권한 관련 service
 * @author sppartners
 *
 */

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;
	
	/**
	 * 권한 목록 조회(select box)
	 */
	@Override
	public List<RoleDto> findAllRole() {
		
		List<RoleDto> roleList =  roleRepository.findAllRole();
		return roleList;
	}

}
