package com.DanielNavia.melody_generator.repository;

import com.DanielNavia.melody_generator.Entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {
}
