package kr.co.aihome.user.repository.impl;


import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.aihome.dto.QUserAuthorDto;
import kr.co.aihome.dto.UserAuthorDto;
import kr.co.aihome.dto.UserSearchForm;
import kr.co.aihome.user.repository.UserRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

import static kr.co.aihome.entity.author.QUser.user;


public class UserRepositoryImpl implements UserRepositoryCustom {
	
	private final JPAQueryFactory queryFactory;

	public UserRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }
	//TODO 에러 수정해야함
	@Override
	public Page<UserAuthorDto> findAllUser(UserSearchForm userSearchForm, Pageable pageable) {
		QueryResults<UserAuthorDto> results = queryFactory
				.select(new QUserAuthorDto(
						user.userId,
						user.username,
						user.name,
						user.password,
						user.email,
						user.age,
						user.gender,
						user.weight,
						user.createdBy,
						user.createdDate))
				.from(user)
				.where(
						usernameEq(userSearchForm.getUsername()),
						nameEq(userSearchForm.getName()),
						user.enabled.eq(true)
						)
				.orderBy(user.createdDate.desc())
				.offset(pageable.getOffset())
				.limit(pageable.getPageSize())
				.fetchResults();
		List<UserAuthorDto> content =  results.getResults();
		long total = results.getTotal();
		return new PageImpl<UserAuthorDto>(content, pageable, total);
	}
	
	//아이디 검색
	private BooleanExpression usernameEq(String username) {
		return StringUtils.hasText(username) ? user.username.contains(username) : null;
	}
	
	//이름 검색
	private BooleanExpression nameEq(String name) {
		return StringUtils.hasText(name) ? user.name.contains(name) : null;
	}

}
