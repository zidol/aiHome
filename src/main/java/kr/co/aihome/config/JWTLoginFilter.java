package kr.co.aihome.config;


import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.aihome.common.dto.ErrorResponse;
import kr.co.aihome.dto.user.UserDto;
import kr.co.aihome.dto.user.UserLoginDto;
import kr.co.aihome.entity.author.User;
import kr.co.aihome.security.RefreshTokenService;
import kr.co.aihome.service.UserService;
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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
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

//        setFilterProcessesUrl("/login");//????????? url??????
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
        
        //ajax ???????????? ??????
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
                logger.debug("????????? JWT ????????? ????????????, uri: {}", requestURI);
                throw new JWTVerificationException("????????? ?????????????????????.");
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
        //?????? ????????? access ??????, refresh ?????? ?????????
        String accessToken = JWTUtil.makeAuthToken(user);
        String refreshToken = JWTUtil.makeRefreshToken(user);
//        response.setHeader("access_token", accessToken);
//        response.setHeader("refresh_token", refreshToken);
        
        //????????? ?????? refresh ?????? DB??? ??????
        refreshTokenService.createRefreshToken(user.getUserId(), refreshToken);
        response.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        Cookie cookie = new Cookie("ndrrtn", refreshToken);
        // expires in 7 days
        cookie.setMaxAge(7 * 24 * 60 * 60);	//????????? ???????????? ??????

        // optional properties
//        cookie.setSecure(true); 	//Secure : SSL ???????????? ?????? ????????? ????????? ??????????????? ??????
        cookie.setHttpOnly(true);	//?????? ?????????????????? ???????????? ???????????? ???????????? ??????
        cookie.setPath("/");

        // add cookie to response
        response.addCookie(cookie);

		String idString = "";
        try {
        	idString = AriaEncodeUtil.ariaEncrypt(user.getUsername());
		} catch (InvalidKeyException | UnsupportedEncodingException e) {
			log.error("message : ", e);
		}

        UserDto userDto = UserDto.builder()
        		.id(user.getUserId())
        		.username(user.getUsername())
                .name(user.getName())
                .authorities(user.getAuthorities())
                .build();

        //access token
        Map<String, Object> map = new HashMap<>();
        map.put("token", accessToken);
        map.put("user", userDto);
//        map.put("refreshToken", refreshToken);
        response.getOutputStream().write(objectMapper.writeValueAsBytes(map));
    }

    /**
     * ????????? ?????? ?????????
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    	ErrorResponse errorResponse = new ErrorResponse();

		errorResponse.setMessage("????????? ?????? ??????????????? ?????? ?????????????????????.");
		errorResponse.setPath(request.getRequestURI());
		errorResponse.setError(HttpStatus.UNAUTHORIZED.getReasonPhrase());
		errorResponse.setStatus(HttpStatus.UNAUTHORIZED.value());
		errorResponse.setTimestamp(LocalDateTime.now());

        //response??? ??????
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(HttpStatus.UNAUTHORIZED.value());
		ObjectMapper objectMapper = new ObjectMapper();
		response.getOutputStream().write(objectMapper.writeValueAsBytes(errorResponse));
    }
    
    //Ajax ?????? ????????? ???????????? ???????????? 
    private boolean isAjax(HttpServletRequest request) {

    	//????????? ?????? ?????? ?????? ????????? ????????? ????????????????????? ???????????? ?????? ????????? ???????????? ??????. 
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
        
        //?????? ?????? ???????????? ?????????
    }
}
