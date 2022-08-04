package kr.co.aihome.controller.auth;


import kr.co.aihome.config.JWTUtil;
import kr.co.aihome.dto.auth.LogOutDto;
import kr.co.aihome.dto.user.SignUpFormDto;
import kr.co.aihome.entity.author.RefreshToken;
import kr.co.aihome.exception.customException.IdDuplicationException;
import kr.co.aihome.exception.customException.PasswordDifferentException;
import kr.co.aihome.exception.customException.TokenRefreshException;
import kr.co.aihome.repository.author.AuthorityRepository;
import kr.co.aihome.repository.role.RoleRepository;
import kr.co.aihome.repository.user.UserRepository;
import kr.co.aihome.security.RefreshTokenService;
import kr.co.aihome.service.UserService;
import kr.co.aihome.utils.AES256Util;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Optional;

//@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

	private final UserService userService;
	private final UserRepository userRepository;
	private final RoleRepository roleRepository;
	private final PasswordEncoder passwordEncoder;
	private final RefreshTokenService refreshTokenService;
	private final JWTUtil jwtUtil;
	private final AuthorityRepository authorityRepository;

	@PostMapping(value = "/signup")
	public ResponseEntity<SignUpFormDto> singup(@RequestBody @Valid SignUpFormDto form) {
		boolean isPresent = userService.findByUsername(form.getUsername()).isPresent();
		if (isPresent) {
			throw new IdDuplicationException("존재하는 아이디입니다.");
		}
		
		if(!form.getPassword().equals(form.getRePassword())) {
			throw new PasswordDifferentException("입력한 비밀번호가 다릅니다.");
		}

		userService.signUp(form);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@PostMapping("/refreshToken")
	public ResponseEntity<?> refreshtoken(HttpServletRequest request) throws UnsupportedEncodingException, InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

		//헤더에 전송
//    String refreshToken = request.getHeader("ndrrtn");
//    return refreshTokenService.findByToken(refreshToken).map(refreshTokenService::verifyExpiration)
//			.map(RefreshToken::getUser).map(user -> {
//				String accessToken = JWTUtil.makeAuthToken(user);
//
//				return ResponseEntity.ok(accessToken);
//			})
//			.orElseThrow(() -> new TokenRefreshException("RefreshTokenEmptyError"));

		//쿠키에 있을때
		Optional<String> refreshToken = Arrays.stream(request.getCookies())
				.filter(cookie -> "ndrrtn".equals(cookie.getName())).map(Cookie::getValue).findAny();
		AES256Util aes256Util = new AES256Util();
		String decodeRefTokenId = "";
		decodeRefTokenId = aes256Util.aesDecode(refreshToken.get());
		return refreshTokenService.findById(Long.parseLong(decodeRefTokenId)).map(refreshTokenService::verifyExpiration)
				.map(RefreshToken::getUser).map(user -> {
					String accessToken = JWTUtil.makeAuthToken(user);

					return ResponseEntity.ok(accessToken);
				})
				.orElseThrow(() -> new TokenRefreshException("RefreshTokenEmptyError"));
	}

	@PostMapping("/logout")
	public ResponseEntity<?> logoutUser(@Valid @RequestBody LogOutDto logOutDto) {
		refreshTokenService.deleteByUserId(logOutDto.getUsername());
		return ResponseEntity.ok("Log out successful!");
	}
	
}
