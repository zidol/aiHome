package kr.co.aihome.config;

import com.auth0.jwt.exceptions.*;
import kr.co.aihome.entity.author.User;
import kr.co.aihome.user.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTCheckFilter extends OncePerRequestFilter {

	private static final Logger logger = LoggerFactory.getLogger(JWTCheckFilter.class);
	private UserService userService;

	public JWTCheckFilter(UserService userService) {
//        super(authenticationManager);
		this.userService = userService;
	}

	// 토큰에 대한 검사
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
//        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
//
//        //리프레시 토큰 요청시 필터 넘어감
//        if(httpServletRequest.getRequestURI().contains("refreshToken")) {
//        	chain.doFilter(request, response);
//        	return;
//        }
//        String bearer = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
//        String requestURI = httpServletRequest.getRequestURI();
//        if(bearer == null || !bearer.startsWith("Bearer ")){
//            chain.doFilter(request, response);
//            return;
//        }
//        String token = bearer.substring("Bearer ".length());
////        VerifyResult result = JWTUtil.verify(token);
////        if(result.isSuccess()){
////            User user = (User) userService.loadUserByUsername(result.getUsername());
////            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
////                    user, null, user.getAuthorities()
////            );
////            SecurityContextHolder.getContext().setAuthentication(userToken);
////            chain.doFilter(request, response);
////        }else{
////            if (result.getException() instanceof TokenExpiredException) {
////                httpServletResponse.sendError(1000, "TokenExpiredError");
////            } else if (result.getException() instanceof JWTDecodeException) {
////            	httpServletResponse.sendError(2000, "WrongJWTDecodeError");
////            } else if (result.getException() instanceof SignatureVerificationException) {
////                httpServletResponse.sendError(3000, "SignatureVerificationError");
////            } else if (result.getException() instanceof InvalidClaimException) {
////                httpServletResponse.sendError(4000, "InvalidClaimError");
////            } else if (result.getException() instanceof JWTVerificationException) {
////                httpServletResponse.sendError(5000, "JWTVerificationError");
////            } 
////        }
//        try {
//            VerifyResult result = JWTUtil.verify(token);
//            User user = (User) userService.loadUserByUsername(result.getUsername());
//          UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
//                  user, null, user.getAuthorities()
//          );
//          SecurityContextHolder.getContext().setAuthentication(userToken);
//          chain.doFilter(request, response);
//		} catch (TokenExpiredException e) {
//			logger.info("토큰의 유효기간이 만료 되었습니다.");
//			request.setAttribute("exception", "TokenExpiredError");
//
//		}
//    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		// 리프레시 토큰 요청시 필터 넘어감
		if (httpServletRequest.getRequestURI().contains("refreshToken")) {
			chain.doFilter(request, response);
			return;
		}
		String bearer = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
		String requestURI = httpServletRequest.getRequestURI();
		if (bearer == null || !bearer.startsWith("Bearer ")) {
			chain.doFilter(request, response);
			return;
		}
		String token = bearer.substring("Bearer ".length());
		
//        VerifyResult result = JWTUtil.verify(token);
//        if(result.isSuccess()){
//            User user = (User) userService.loadUserByUsername(result.getUsername());
//            UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
//                    user, null, user.getAuthorities()
//            );
//            SecurityContextHolder.getContext().setAuthentication(userToken);
//            chain.doFilter(request, response);
//        }else{
//            if (result.getException() instanceof TokenExpiredException) {
//                httpServletResponse.sendError(1000, "TokenExpiredError");
//            } else if (result.getException() instanceof JWTDecodeException) {
//            	httpServletResponse.sendError(2000, "WrongJWTDecodeError");
//            } else if (result.getException() instanceof SignatureVerificationException) {
//                httpServletResponse.sendError(3000, "SignatureVerificationError");
//            } else if (result.getException() instanceof InvalidClaimException) {
//                httpServletResponse.sendError(4000, "InvalidClaimError");
//            } else if (result.getException() instanceof JWTVerificationException) {
//                httpServletResponse.sendError(5000, "JWTVerificationError");
//            } 
//        }
		
		//에러 핸들링은 ExceptionHandlerFilter.class 에서 함
		try {
			VerifyResult result = JWTUtil.verify(token);
			User user = (User) userService.loadUserByUsername(result.getUsername());
			UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(user, null,
					user.getAuthorities());
			SecurityContextHolder.getContext().setAuthentication(userToken);
			chain.doFilter(request, response);
		} catch (TokenExpiredException e) {
			throw new TokenExpiredException("TokenExpiredError");
		} catch (JWTDecodeException e) {
			throw new JWTDecodeException("WrongJWTDecodeError");
		} catch (SignatureVerificationException e) {
			throw new SignatureVerificationException(null);
		} catch (InvalidClaimException e) {
			throw new InvalidClaimException("InvalidClaimError");
		} catch (JWTVerificationException e) {
			throw new JWTVerificationException("JWTVerificationError");
		}

	}
}
