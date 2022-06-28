package kr.co.aihome.service;

import kr.co.aihome.dto.user.SignUpFormDto;
import kr.co.aihome.entity.author.Authority;
import kr.co.aihome.entity.author.Gender;
import kr.co.aihome.entity.author.Role;
import kr.co.aihome.entity.author.User;
import kr.co.aihome.repository.author.AuthorityRepository;
import kr.co.aihome.repository.role.RoleRepository;
import kr.co.aihome.repository.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class UserServiceTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입")
    void signupTest() {
        Role role = Role.builder()
                .authority("ROLE_USER")
                .description("사용자")
                .build();
        roleRepository.save(role);

        SignUpFormDto form = SignUpFormDto.builder()
                .username("ajh3166")
                .name("안지혁")
                .email("aaa@aaa.com")
                .password("1234")
                .weight(60.0)
                .age(35)
                .gender(Gender.MALE)
                .build();

        final User user = User.builder()
                .username(form.getUsername())
                .name(form.getName())
                .email(form.getEmail())
                .weight(form.getWeight())
                .age(form.getAge())
                .gender(form.getGender())
                .password(passwordEncoder.encode(form.getPassword()))
                .enabled(true).build();

        User resultUser = userRepository.save(user);

        Authority authority = Authority.builder().authority(role).user(resultUser).build();

        authorityRepository.save(authority);
        //TODO 회원 도메인에서 권한 등록 로직 짜기
    }
}