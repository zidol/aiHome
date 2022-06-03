package kr.co.aihome.config;


import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.aihome.common.dto.ErrorResponse;
import kr.co.aihome.dto.UserDto;
import kr.co.aihome.dto.UserLoginDto;
import kr.co.aihome.entity.author.RefreshToken;
import kr.co.aihome.entity.author.User;
import kr.co.aihome.security.RefreshTokenService;
import kr.co.aihome.user.service.UserService;
import kr.co.aihome.utils.AES256Util;
import kr.co.aihome.utils.AriaEncodeUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class JWTLoginFilter extends UsernamePasswordAuthenticationFilter {


    private static final Logger logger = LoggerFactory.getLogger(JWTLoginFilter.class);

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private ObjectMapper objectMapper = new ObjectMapper();

    public JWTLoginFilter(AuthenticationManager authenticationManager, UserService userService, RefreshTokenService refreshTokenService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.refreshTokenService = refreshTokenService;

//        setFilterProcessesUrl("/login");//로그인 url설정
        this.setAuthenticationSuccessHandler(new LoginSuccessHandler());
        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/api/login","POST"));
    }

    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException
    {
        UserLoginDto userLogin = objectMapper.readValue(request.getInputStream(), UserLoginDto.class);
        String requestURI = request.getRequestURI();
        
        //ajax 통신인지 점검
//        if(!isAjax(request)){
//            throw  new IllegalStateException("Authentication in not supported");
//        }

        if(userLogin.getRefreshToken() == null) {
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    userLogin.getUsername(), userLogin.getPassword(), null
            );
            // user details...
            return authenticationManager.authenticate(token);
        } else {
            VerifyResult verify = JWTUtil.verify(userLogin.getRefreshToken());
            if(verify.isSuccess()){
                User user = (User) userService.loadUserByUsername(verify.getUsername());
                UserDto userDto = UserDto.builder()
                		.username(user.getUsername())
                		.name(user.getName())
                		.email(user.getEmail())
                		.authorities(user.getAuthorities())
                		.build();
                return new UsernamePasswordAuthenticationToken(
                		userDto, userDto.getAuthorities()
                );
            } else {
                logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
                throw new JWTVerificationException("토큰이 잘못되었습니다.");
            }
        }
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException, ServletException
    {
        User user = (User) authResult.getPrincipal();
        //인증 성공시 access 토큰, refresh 토큰 재발급
        String accessToken = JWTUtil.makeAuthToken(user);
        String refreshToken = JWTUtil.makeRefreshToken(user);
//        response.setHeader("access_token", accessToken);
//        response.setHeader("refresh_token", refreshToken);
        
        //재발급 받은 refresh 토큰 DB에 저장
        RefreshToken reToken = refreshTokenService.createRefreshToken(user.getUserId(), refreshToken);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        
        AES256Util aes256Util = new AES256Util();

		String encodeRefTokenId = "";
        try {
        	encodeRefTokenId = aes256Util.aesEncode(Long.toString(reToken.getId()));
		} catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException | NoSuchPaddingException
				| InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
			log.error("message : ", e);
			e.printStackTrace();
		}
        Cookie cookie = new Cookie("ndrrtn", encodeRefTokenId);
        // expires in 7 days
        cookie.setMaxAge(7 * 24 * 60 * 60);	//쿠키의 유효기간 설정

        // optional properties
//        cookie.setSecure(true); 	//Secure : SSL 통신채널 연결 시에만 쿠키를 전송하도록 설정
        cookie.setHttpOnly(true);	//자바 스크립트에서 쿠키값을 읽어가지 못하도록 설정
        cookie.setPath("/");

        // add cookie to response
        response.addCookie(cookie);

		String idString = "";
        try {
        	idString = AriaEncodeUtil.ariaEncrypt(user.getUsername());
		} catch (InvalidKeyException e) {
			log.error("message : ", e);
		} catch (UnsupportedEncodingException e) {
			log.error("message : ", e);
		}

        UserDto userDto = UserDto.builder()
        		.id(user.getUserId())
        		.username(user.getUsername())
                .name(user.getName())
//                .email(user.getEmail())
//                .mobile(user.getMobile())
                .authorities(user.getAuthorities())
                .ssoId(idString)
                .build();

        //access token
        Map<String, Object> map = new HashMap<>();
        map.put("token", accessToken);
        map.put("user", userDto);
//        map.put("refreshToken", refreshToken);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(map));
    }

    /**
     * 로그인 실패 핸들러
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    	ErrorResponse errorResponse = new ErrorResponse();

		errorResponse.setMessage("아이디 혹은 비밀번호를 잘못 입력하셨습니다.");
		errorResponse.setPath(request.getRequestURI());
		errorResponse.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
		errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
		errorResponse.setTimestamp(LocalDateTime.now());

        //response에 넣기
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		ObjectMapper objectMapper = new ObjectMapper();
		response.getOutputStream().write(objectMapper.writeValueAsBytes(errorResponse));
    }
    
    //Ajax 인지 아닌지 기준점을 정해준다 
    private boolean isAjax(HttpServletRequest request) {

    	//정보에 담겨 있는 값과 같은지 아닌지 판별해야하는데 서버에서 미리 약속을 정해줄수 있다. 
        if ( "XMLHttpRequest".equals(request.getHeader("X-Requested-With")) ){
        	return true;
        }
        
        //위에 값과 일치하지 않으면 
        return false;
    }
}
