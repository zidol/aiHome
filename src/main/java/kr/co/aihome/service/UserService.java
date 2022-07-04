package kr.co.aihome.service;

import kr.co.aihome.dto.user.*;
import kr.co.aihome.entity.author.Authority;
import kr.co.aihome.entity.author.Role;
import kr.co.aihome.entity.author.User;
import kr.co.aihome.exception.customException.NotFoundException;
import kr.co.aihome.repository.author.AuthorityRepository;
import kr.co.aihome.repository.role.RoleRepository;
import kr.co.aihome.repository.user.UserRepository;
import kr.co.aihome.utils.Seed;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

	
    private final PasswordEncoder passwordEncoder;
    
    private final UserRepository userRepository;
    
    private final RoleRepository roleRepository;
    
    private final AuthorityRepository authorityRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }

    public Optional<User> findUser(String email) {
        return userRepository.findByUsername(email);
    }

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * @param form
     * 
     * 유저 회원가입
     */
    @Transactional
    public void signUp(SignUpFormDto form) {
    	Role role = roleRepository.findByAuthority("ROLE_WAIT");

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
    }

    public void addAuthority(User resultUser, Role authority){
        userRepository.findById(resultUser.getUserId()).ifPresent(user->{
//            Authority newRole = new Authority(user, authority);
        	Authority newRole = Authority.builder()
        			.user(user)
        			.authority(authority)
        			.build();
            if(user.getAuthorities() == null){
                HashSet<Authority> authorities = new HashSet<>();
                authorities.add(newRole);
                user.setAuthorities(authorities);
                save(user);
            }else if(!user.getAuthorities().contains(newRole)){
                HashSet<Authority> authorities = new HashSet<>();
                authorities.addAll(user.getAuthorities());
                authorities.add(newRole);
                user.setAuthorities(authorities);
                save(user);
            }
        });
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

//    public void updateRefreshToken(Long id, String refreshToken) {
//        userRepository.findById(id).ifPresent(user -> {
//            user.setRefreshToken(refreshToken);
//            user.setUpdatedDate(LocalDateTime.now());
//            user.setUpdatedBy(user.getEmail());
//        });
//    }
    
    /**
     * 사용자 목록 페이징
     * @param userSearchForm
     * @param pageable
     * @return
     */
    public Page<UserAuthorDto> findAllUser(UserSearchForm userSearchForm, Pageable pageable) {
		Page<UserAuthorDto> resultPage = userRepository.findAllUser(userSearchForm, pageable);
		return resultPage;
	}
    
    /**
     * @param form
     * 
     * 관리자 회원 등록
     */
    @Transactional
    public void insertUser(SignUpFormDto form) {
//    	Role role = roleRepository.findByAuthority(form.getAuthority());

		final User user = User.builder()
				.username(form.getUsername())
				.name(form.getName())
				.email(form.getEmail())
				.age(form.getAge())
				.gender(form.getGender())
				.weight(form.getWeight())
				.password(passwordEncoder.encode(form.getPassword()))
				.enabled(true).build();
		
		User resultUser = userRepository.save(user);

		List<String> authorities =  form.getAuthorities();
		for (String author : authorities) {
			Role role = roleRepository.findByAuthority(author);

			Authority authority = Authority.builder().authority(role).user(resultUser).build();
			authorityRepository.save(authority);
		}
    }
    
    @Transactional
    public void updateUser(Long id, UpdateUserFormDto form) throws Exception {
    	User findUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("찾으신 결과가 없습니다."));
    	
		findUser.setName(form.getName());
		findUser.setEmail(form.getEmail());
		if (!form.getPassword().equals("") && form.getPassword() != null) {
			findUser.setPassword(passwordEncoder.encode(form.getPassword()));
		}
		findUser.setAge(form.getAge());
		findUser.setWeight(form.getWeight());
		findUser.setGender(form.getGender());
		
		//기존 권한
		authorityRepository.deleteByUser(findUser);
		
		List<String> authorities = new ArrayList<String>();
		authorities = form.getAuthorities();
		Authority authority = null;
		for (String addAuthor : authorities) {
			Role role = roleRepository.findByAuthority(addAuthor);
			authority = Authority.builder().authority(role).user(findUser).build();
			authorityRepository.save(authority);
		}
		    	
    }

	public UserDetailDto findUserById(Long id) throws Exception {
	
		User findUser = userRepository.findById(id).orElseThrow(() -> new Exception("찾으신 결과가 없습니다."));
		
		UserDetailDto userDto = new UserDetailDto(findUser);

//		if(userDto.getMobile() != null) {
//			userDto.setMobile(Seed.decrypt(userDto.getMobile()));
//		}
		return userDto;
	}
	
	@Transactional
	public void deleteUser(Long id) {
		userRepository.findById(id).ifPresent(user -> {
			user.setEnabled(false);
			
		});
		
	}
	
	@Transactional
	public void updateMy(Long id, UpdateUserFormDto form) throws Exception {
		User findUser = userRepository.findById(id).orElseThrow(() -> new NotFoundException("찾으신 결과가 없습니다."));

		findUser.setName(form.getName());
		findUser.setEmail(form.getEmail());
		if (!form.getPassword().equals("") && form.getPassword() != null) {
			findUser.setPassword(passwordEncoder.encode(form.getPassword()));
		}
		findUser.setAge(form.getAge());
		findUser.setWeight(form.getWeight());
		findUser.setGender(form.getGender());

	}
}
