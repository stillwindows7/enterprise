package io.github.enterprise.repository;

import io.github.enterprise.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Sheldon on 2017/12/08
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
}
