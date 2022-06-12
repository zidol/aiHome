package kr.co.aihome.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import kr.co.aihome.entity.author.RefreshToken;
import kr.co.aihome.exception.customException.TokenRefreshException;
import kr.co.aihome.repository.refreshtoken.RefreshTokenRepository;
import kr.co.aihome.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
//  @Value("${bezkoder.app.jwtRefreshExpirationMs}")
//  private Long refreshTokenDurationMs;

  private final RefreshTokenRepository refreshTokenRepository;
  private final UserRepository userRepository;

  @Value("${jwt.secret}")
  private String SECRET;

  public Optional<RefreshToken> findByToken(String token) {
    return refreshTokenRepository.findByToken(token);
  }
  
  public Optional<RefreshToken> findById(Long id) {
	    return refreshTokenRepository.findById(id);
	  }

  public Algorithm algorithm(String secret) {
    return Algorithm.HMAC512(secret);
  }
  public RefreshToken createRefreshToken(Long userId, String token) {
    RefreshToken refreshToken = new RefreshToken();

    DecodedJWT verify = JWT.require(algorithm(SECRET)).build().verify(token);
    Date expiresAt = verify.getExpiresAt();

    refreshToken.setUser(userRepository.findById(userId).get());

    refreshToken.setExpiryDate(expiresAt.toInstant());
    refreshToken.setToken(token);

    refreshToken = refreshTokenRepository.save(refreshToken);

    return refreshToken;
  }

  public RefreshToken verifyExpiration(RefreshToken token) {
    if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
      refreshTokenRepository.delete(token);
      throw new TokenRefreshException("RefreshTokenExpiredError");
    }

    return token;
  }

  @Transactional
  public int deleteByUserId(String username) {
    return refreshTokenRepository.deleteByUser(userRepository.findByUsername(username).get());
  }
}
