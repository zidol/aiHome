package kr.co.aihome.entity.author;

import kr.co.aihome.exception.customException.NotFoundException;
import kr.co.aihome.repository.author.AuthorityRepository;
import kr.co.aihome.repository.role.RoleRepository;
import kr.co.aihome.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class UserTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Test
    @DisplayName("유저 생성 테스트")
    @Transactional
    void userSaveTest() throws Exception {
        Role role = Role.builder()
                .authority(ERole.ROLE_USER)
                .description("사용자")
                .build();
        roleRepository.save(role);

        User user = User.builder()
                .username("zidolee")
                .age(20)
                .name("지돌이")
                .email("aaa@aaa.com")
                .gender(Gender.MALE)
                .password("1234")
                .weight(70.0)
                .enabled(true)
                .build();
        User savedUser = userRepository.save(user);

        Authority authority = Authority.builder().authority(role).user(savedUser).build();

        authorityRepository.save(authority);

        User findUser = userRepository.findById(savedUser.getUserId()).orElseThrow(() -> new Exception("회원이 존재 하지 않습니다."));

        assertThat(findUser.getUserId()).isEqualTo(savedUser.getUserId());

    }

    @Test
    @DisplayName("enable 값을 false로 변경하여 회원을 탈퇴 처리 한다.")
    @Transactional
    void secessionId() throws Exception {
        //given
        User user = User.builder()
                .username("zidolee")
                .age(20)
                .name("지돌이")
                .email("aaa@aaa.com")
                .gender(Gender.MALE)
                .password("1234")
                .weight(70.0)
                .enabled(true)
                .build();
        User savedUser = userRepository.save(user);
        System.out.println(savedUser.getUserId());

        //when 탈퇴
        User findUser = userRepository.findById(savedUser.getUserId()).orElseThrow(() -> new NotFoundException("회원이 존재 하지 않습니다."));
        findUser.setEnabled(false);

        //then 탈퇴한 회원조회
        assertThrows(NotFoundException.class, () -> {
            userRepository.findById(savedUser.getUserId()).orElseThrow(() -> new NotFoundException("회원이 존재 하지 않습니다."));
        });
    }
}