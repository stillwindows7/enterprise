package io.github.enterprise.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.github.enterprise.model.User;

/**
 * Created by Sheldon on 2017/12/08
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
	
}
