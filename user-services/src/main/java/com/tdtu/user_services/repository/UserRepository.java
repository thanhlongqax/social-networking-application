package com.tdtu.user_services.repository;

import com.tdtu.user_services.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;
import java.util.Optional;
public interface UserRepository extends JpaRepository<User,String> {
    List<User> findByActive(boolean active);
    Optional<User> findByIdAndActive(String id, boolean active);
    List<User> findByIdInAndActive(List<String> id, boolean active);
    Boolean existsByEmailAndActive(String email, boolean active);
    Boolean existsByEmail(String email);
    Optional<User> findByEmailAndActive(String email, boolean active);
    @Query("SELECT u FROM User u WHERE LOWER(REPLACE(CONCAT(u.firstName, u.middleName, u.lastName), ' ', '')) LIKE %?1%")
    List<User> findByNamesContaining(String name);
}
