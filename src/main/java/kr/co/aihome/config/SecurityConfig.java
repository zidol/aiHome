package kr.co.aihome.config;

import kr.co.aihome.handler.CustomAccessDeniedHandler;
import kr.co.aihome.handler.ExceptionHandlerFilter;
import kr.co.aihome.security.RefreshTokenService;
import kr.co.aihome.security.SecurityResourceService;
import kr.co.aihome.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

//    @Override //테스트용 인메모리에 저장
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser(
//                        User.withDefaultPasswordEncoder()
//                                .username("user1")
//                                .password("1111")
//                                .roles("USER")
//                                .build()
//                );
//    }

    private final CorsFilter corsFilter;
    private final UserService userService;
    private final RefreshTokenService refreshTokenService;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final SecurityResourceService securityResourceService;
    private final CommonConfig commonConfig;
//    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
//    private final ExceptionHandlerFilter exceptionHandlerFilter;

//    @Bean
//    PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }

    /****************** url resourece start *********************/
    @Bean
    public FilterSecurityInterceptor customFilterSecurityInterceptor() throws Exception {
        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
        filterSecurityInterceptor.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource());   // 권한정보 셋팅
        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());
        filterSecurityInterceptor.setAuthenticationManager(authenticationManagerBean());    // 인증매니저
        return filterSecurityInterceptor;
    }
    
    private AccessDecisionManager affirmativeBased() {
        AffirmativeBased affirmativeBased = new AffirmativeBased(getAccessDecisionVoters());
        return affirmativeBased;
    }


    private List<AccessDecisionVoter<? extends Object>> getAccessDecisionVoters() {
        return Arrays.asList(new RoleVoter());
    }


    @Bean
    public FilterInvocationSecurityMetadataSource urlFilterInvocationSecurityMetadataSource() throws Exception {
        return new UrlFilterInvocationSecurityMetadataSource(urlResourcesMapFactoryBean().getObject());
    }

    private UrlResourcesMapFactoryBean urlResourcesMapFactoryBean() {
        UrlResourcesMapFactoryBean urlResourcesMapFactoryBean = new UrlResourcesMapFactoryBean();
        urlResourcesMapFactoryBean.setSecurityResourceService(securityResourceService);
        return urlResourcesMapFactoryBean;
    }

    // 인증 매니저
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    
    
    /****************** url resourece end *********************/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService)
                .passwordEncoder(commonConfig.passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	//TODO: csrf
        JWTLoginFilter loginFilter = new JWTLoginFilter(authenticationManagerBean(), userService, refreshTokenService);
        JWTCheckFilter checkFilter = new JWTCheckFilter(userService);
        ExceptionHandlerFilter exceptionHandlerFilter = new ExceptionHandlerFilter();

        http
        		.cors()
        		.and()
                .csrf().disable()
                .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class)//url 리소스 권한 DB관리
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling( exeption -> {
                	exeption.accessDeniedHandler(accessDeniedHandler);
                })
//                .authorizeRequests(config ->{
//                	config.antMatchers("/api/posts/*").permitAll()
//                	.antMatchers("/api/posts").permitAll();
//                })
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)//로그인 필터
                .addFilterAt(checkFilter, BasicAuthenticationFilter.class)// 토큰 검증 필터
                .addFilterBefore(exceptionHandlerFilter, checkFilter.getClass())
                ;
//        http
//        .headers()
//    	.xssProtection()
//    	.and()
//    	.contentSecurityPolicy("script-src 'self'");

    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(
                        "/h2-console/**"
                        ,"/favicon.ico"
                        ,"/error"
                );
    }
}
