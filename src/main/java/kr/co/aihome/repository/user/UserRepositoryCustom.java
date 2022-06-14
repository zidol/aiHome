package kr.co.aihome.repository.user;

import kr.co.aihome.dto.user.UserAuthorDto;
import kr.co.aihome.dto.user.UserSearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

	Page<UserAuthorDto> findAllUser(UserSearchForm userSearchForm, Pageable pageable);
}
