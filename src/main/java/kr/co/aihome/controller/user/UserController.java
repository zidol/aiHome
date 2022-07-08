package kr.co.aihome.controller.user;

import kr.co.aihome.common.dto.ResponseData;
import kr.co.aihome.common.dto.StatusEnum;
import kr.co.aihome.dto.role.RoleDto;
import kr.co.aihome.dto.user.*;
import kr.co.aihome.exception.customException.IdDuplicationException;
import kr.co.aihome.exception.customException.PasswordDifferentException;
import kr.co.aihome.service.UserService;
import kr.co.aihome.service.role.RoleService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 관리자 사용자 관리 api
 *
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	private final UserService userService;
	
	private final RoleService roleService;

	/**
	 * 유저 목록 페이징
	 * @param userSearchForm
	 * @param pageable
	 * @param request
	 * @return
	 */
	@GetMapping("/users")
	public ResponseEntity<ResponseData> findAllUser(UserSearchForm userSearchForm, Pageable pageable, HttpServletRequest request) {
		
		Page<UserAuthorDto> users = userService.findAllUser(userSearchForm, pageable);

		ResponseData message = new ResponseData();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		message.setStatus(StatusEnum.OK);
		message.setMessage("정상적으로 조회 하셨습니다.");
		message.setData(users);
		return new ResponseEntity<>(message, headers, HttpStatus.OK);
	}
	
	/**
	 * 관리자 회원 등록
	 * @param form
	 * @return
	 */
	@PostMapping("/users")
	public ResponseEntity<ResponseData> insertUser(@RequestBody @Valid SignUpFormDto form) {
		boolean isPresent = userService.findByUsername(form.getUsername()).isPresent();
		if (isPresent) {
			throw new IdDuplicationException("존재하는 아이디입니다.");
		}
		
		if(!form.getPassword().equals(form.getRePassword())) {
			throw new PasswordDifferentException("입력한 비밀번호가 다릅니다.");
		}

		
		userService.insertUser(form);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@PutMapping("/users/{id}")
	public ResponseEntity<?> updateUser(@PathVariable Long id,@RequestBody @Valid UpdateUserFormDto form) throws Exception {
		
		if(form.getPassword() != null && !form.getPassword().equals("")) {
			if(!form.getPassword().equals(form.getRePassword())) {
				throw new PasswordDifferentException("입력한 비밀번호가 다릅니다.");
			}
		}

		
		userService.updateUser(id, form);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	@GetMapping("/users/{id}")
	public ResponseEntity<ResponseData> findUserById(@PathVariable Long id) throws Exception {
			
		UserDetailDto user =  userService.findUserById(id);
		
		ResponseData message = new ResponseData();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		message.setStatus(StatusEnum.OK);
		message.setMessage("정상적으로 조회 하셨습니다.");
		message.setData(user);
		return new ResponseEntity<>(message, headers, HttpStatus.OK);
	}
	
	@DeleteMapping("/users/{id}")
	public ResponseEntity<?> deleteUser(@PathVariable Long id) throws Exception {
		
		
		userService.deleteUser(id);
		
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	/**
	 * 권한 목록(select용)
	 * @return
	 */
	@GetMapping("/roles")
	public ResponseEntity<ResponseData> findAllRoles() {
		List<RoleDto> roleList = roleService.findAllRole();
		
		ResponseData message = new ResponseData();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
		message.setStatus(StatusEnum.OK);
		message.setMessage("정상적으로 조회 하셨습니다.");
		message.setData(roleList);
		return new ResponseEntity<>(message, headers, HttpStatus.OK);
	}
}
