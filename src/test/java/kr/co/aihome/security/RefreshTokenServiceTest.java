package kr.co.aihome.security;

import kr.co.aihome.repository.refreshtoken.RefreshTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RefreshTokenServiceTest {
    //TODO 테스트 코드 짜기!!

    @Autowired
    RefreshTokenRepository refreshTokenService;

    @Test
    void getRefreshToken() {

    }
}