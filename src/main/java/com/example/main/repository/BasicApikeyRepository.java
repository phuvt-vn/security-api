package com.example.main.repository;

import java.time.LocalDateTime;
import java.util.Optional;

import com.example.main.entity.BasicApikey;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BasicApikeyRepository extends CrudRepository<BasicApikey, Integer> {

	Optional<BasicApikey> findByApikeyAndExpiredAtAfter(String apikey, LocalDateTime now);

}
