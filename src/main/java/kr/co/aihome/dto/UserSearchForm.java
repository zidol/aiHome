package kr.co.aihome.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * query dsl 사용시 where 절 조건에 들어갈 파라미터
 *
 */
@Getter
@Setter
public class UserSearchForm {

	private String username;
	private String name;
}
