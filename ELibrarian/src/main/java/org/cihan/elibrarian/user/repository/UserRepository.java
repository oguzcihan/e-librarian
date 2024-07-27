package org.cihan.elibrarian.user.repository;

import org.cihan.elibrarian.user.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserName(String username);

    Boolean existsByUserNameOrEmail(String username, String email);
}
