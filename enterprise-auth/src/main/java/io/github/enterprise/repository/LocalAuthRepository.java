package io.github.enterprise.repository;

import io.github.enterprise.model.LocalAuth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Sheldon on 2017/12/08
 */
@Repository
public interface LocalAuthRepository extends JpaRepository<LocalAuth, String> {
}
