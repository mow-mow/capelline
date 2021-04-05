package info.caprese.capelline.repository;

import info.caprese.capelline.entity.User;
import info.caprese.capelline.entity.UserConnection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserConnectionRepository extends JpaRepository<UserConnection, String> {

}