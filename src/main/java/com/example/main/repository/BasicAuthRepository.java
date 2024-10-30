package com.example.main.repository;

import com.example.main.entity.BasicAuthUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BasicAuthRepository extends CrudRepository<BasicAuthUser,Integer> {

    Optional<BasicAuthUser> findByUsername(String encryptedUsername);
}
