package kr.co.aihome.user.repository;

import kr.co.aihome.entity.author.Authority;
import kr.co.aihome.entity.author.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AuthorityRepository extends JpaRepository<Authority, Long>{

	List<Authority> findAllByUser(User user);

	void deleteByUser(User findUser);

}
