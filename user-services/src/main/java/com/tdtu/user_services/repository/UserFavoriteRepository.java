package com.tdtu.user_services.repository;

import com.tdtu.user_services.model.User;
import com.tdtu.user_services.model.UserFavourite;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserFavoriteRepository extends JpaRepository<UserFavourite , String> {
    Boolean existsByName(String name);
    Optional<UserFavourite> findByNameAndUser(String name, User user);
}
