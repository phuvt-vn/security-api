package com.example.main.repository;

import java.util.List;

import com.example.main.entity.JdbcCustomer;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface JdbcCustomerCrudRepository extends CrudRepository<JdbcCustomer, Integer> {

    List<JdbcCustomer> findByEmail(String email);

    List<JdbcCustomer> findByGender(String gender);

    @Query(value = "CALL update_jdbc_customer(:customerId, :newFullName)", nativeQuery = true)
    void updateCustomerFullName(@Param("customerId") int customerId,
                                @Param("newFullName") String newFullName);

}