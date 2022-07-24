package kr.co.aihome.controller.user;

import kr.co.aihome.config.SecurityConfig;
import kr.co.aihome.dto.user.UserDetailDto;
import kr.co.aihome.entity.author.Authority;
import kr.co.aihome.entity.author.Gender;
import kr.co.aihome.entity.author.User;
import kr.co.aihome.security.RefreshTokenService;
import kr.co.aihome.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/**
 * 유저 컨트롤러 테스트
 */
//@SpringBootTest
@WebMvcTest(UserController.class)
@ComponentScan(basePackages = {"kr.co.aihome"})
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @MockBean
    SecurityConfig securityConfig;

    @MockBean
    RefreshTokenService refreshTokenService;

    @MockBean
    UserController userController;


    @Test
    void getUserDetailTest() throws Exception {
        //TODO controller tes
        List<Authority> authorities = new ArrayList<>();
        User user = User.builder()
                .username("ajh3166")
                .gender(Gender.MALE)
                .name("안지혁")
                .age(30)
                .email("aaa@aaa.com")
                .password("1234")
                .weight(60.0)
                .authorities(authorities)
                .build();

        UserDetailDto userDto = new UserDetailDto(user);
        given(userService.findUserById(1L)).willReturn(userDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(status().isOk());

    }
}