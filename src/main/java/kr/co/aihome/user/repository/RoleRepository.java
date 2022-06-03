package kr.co.aihome.user.repository;

import kr.co.aihome.dto.role.RoleDto;
import kr.co.aihome.entity.author.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {

	Role findByAuthority(String string);
	
	@Query(value = "select new kr.co.aihome.dto.role.RoleDto(r.authority, r.description)  from Role r")
	List<RoleDto> findAllRole();
}
