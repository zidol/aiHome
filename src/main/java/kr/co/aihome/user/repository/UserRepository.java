package kr.co.aihome.user.repository;


import kr.co.aihome.entity.author.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom, QuerydslPredicateExecutor<User> {


//    @Modifying(clearAutomatically = true)
//    @Query("update User set name=?2, updated=?3 where userId=?1")
//    void updateUserName(Long userId, String userName, LocalDateTime update);

    //Optional<User> findByEmail(String username);
    // fetch join으로 한번에 블러옴 :  쿼리메소드 사용시 지연로딩된 엔티티를 초기화하려고 해서 연관된  authority를 가져오기 위해 
    @Query("select u from User u join fetch u.authorities where u.username = :username")
    Optional<User> findByUsername(@Param("username") String username);
    
    @Query("select distinct u from User u join fetch u.authorities where u.userId = :userId")
    Optional<User> findById(@Param("userId") Long userId);


/*    @Query("select a from User a, Authority b where a.userId=b.userId and b.authority=?1")
    List<User> findAllByAuthoritiesIn(String authority);

    @Query("select a from User a, Authority b where a.userId=b.userId and b.authority=?1")
    Page<User> findAllByAuthoritiesIn(String authority, Pageable pageable);

    @Query("select a from User a, Authority b where a.school.schoolId=?1 and a.userId=b.userId and b.authority=?2")
    List<User> findAllBySchool(Long schoolId, String authority);

    @Query("select a from User a, User b where a.teacher.userId=b.userId and b.userId=?1")
    List<User> findAllByTeacherUserId(Long userId);

    @Query("select count(a) from User a, User b where a.teacher.userId=b.userId and b.userId=?1")
    Long countByAllTeacherUserId(Long userId);

    @Query("select count(a) from User a, Authority b where a.userId=b.userId and b.authority=?1")
    long countAllByAuthoritiesIn(String authority);

    @Query("select count(a) from User a, Authority b where a.school.schoolId=?1 and a.userId=b.userId and b.authority=?2")
    long countAllByAuthoritiesIn(long schoolId, String authority);*/

}
