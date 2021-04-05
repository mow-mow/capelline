package info.caprese.capelline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import info.caprese.capelline.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

	@Query(value = "SELECT * FROM USERS WHERE username = :username", nativeQuery = true)
	User findByUsername(@Param("username") String username);
	@Query(value = "SELECT * FROM USERS WHERE userId = :userId", nativeQuery = true)
	Optional<User> findOne(String userId);
}