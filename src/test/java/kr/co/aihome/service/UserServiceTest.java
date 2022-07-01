package kr.co.aihome.service;

import kr.co.aihome.dto.user.SignUpFormDto;
import kr.co.aihome.entity.author.*;
import kr.co.aihome.exception.customException.NotFoundException;
import kr.co.aihome.repository.author.AuthorityRepository;
import kr.co.aihome.repository.role.RoleRepository;
import kr.co.aihome.repository.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.*;
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
        User user1 = userRepository.findById(resultUser.getUserId()).orElseThrow(() -> new NotFoundException("찾을수 없습니다."));

        assertThat(user1.getUserId()).isEqualTo(resultUser.getUserId());
        assertThat(user1.getUsername()).isEqualTo(resultUser.getUsername());

        Set<Authority> authorities = user1.getAuthorities();

        authorities.forEach(a -> assertThat(a.getAuthority()).contains("ROLE_USER"));
        //TODO ROLE Enum 타입으로 변경해보기
    }
}