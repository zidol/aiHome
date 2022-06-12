package kr.co.aihome.repository.user;

import kr.co.aihome.dto.UserAuthorDto;
import kr.co.aihome.dto.UserSearchForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepositoryCustom {

	Page<UserAuthorDto> findAllUser(UserSearchForm userSearchForm, Pageable pageable);
}
