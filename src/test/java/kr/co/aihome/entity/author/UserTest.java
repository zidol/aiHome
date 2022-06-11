package kr.co.aihome.entity.author;

import kr.co.aihome.user.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserTest {

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("유저 생성 테스트")
    @Transactional
    void userSaveTest() throws Exception {
        User user = User.builder()
                .username("zidolee")
                .age(20)
                .name("지돌이")
                .email("aaa@aaa.com")
                .gender(Gender.MALE)
                .password("1234")
                .weight(70.0)
                .build();
        User savedUser = userRepository.save(user);

        User findUser = userRepository.findById(savedUser.getUserId()).orElseThrow(() -> new Exception("회원이 존재 하지 않습니다."));

        assertThat(findUser.getUserId()).isEqualTo(savedUser.getUserId());

    }
}