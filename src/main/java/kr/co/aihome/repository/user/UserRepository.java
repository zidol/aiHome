package kr.co.aihome.repository.user;


import kr.co.aihome.entity.author.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom, QuerydslPredicateExecutor<User> {

    // fetch join으로 한번에 블러옴 :  쿼리메소드 사용시 지연로딩된 엔티티를 초기화하려고 해서 연관된  authority를 가져오기 위해 
    @Query("select u from User u join fetch u.authorities where u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);
    
    @Query("select distinct u from User u left join fetch u.authorities where u.userId = :userId")
    Optional<User> findById(@Param("userId") Long userId);



}
