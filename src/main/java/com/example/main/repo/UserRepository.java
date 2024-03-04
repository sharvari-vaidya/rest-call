package com.example.main.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.main.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String>{

}
