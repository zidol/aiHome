package kr.co.aihome.entity.author;

import kr.co.aihome.exception.customException.NotFoundException;
import kr.co.aihome.repository.role.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RoleTest {

    @Autowired
    RoleRepository roleRepository;

    @Test
    @Transactional
    void roleTest() {
        Role role = Role.builder()
                .authority(ERole.ROLE_ADMIN)
                .description(ERole.ROLE_ADMIN.getDesc())
                .build();

        Role save = roleRepository.save(role);

        Role foundRole = roleRepository.findById(save.getAuthority()).orElseThrow(() -> new NotFoundException("찾을 수 없습니다"));

        System.out.println("foundRole = " + foundRole.getAuthority().toString());
    }

}