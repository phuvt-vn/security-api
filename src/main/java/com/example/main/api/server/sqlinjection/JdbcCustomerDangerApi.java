package com.example.main.api.server.sqlinjection;

import java.util.List;

import javax.validation.Valid;

import com.example.main.api.request.sqlinjection.JdbcCustomerPatchRequest;
import com.example.main.entity.JdbcCustomer;
import com.example.main.repository.JdbcCustomerDangerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/sqlinjection/danger/v1")
@Validated
public class JdbcCustomerDangerApi {

    @Autowired
    private JdbcCustomerDangerRepository repository;

    @GetMapping(value = "/customer/{email}")
    public JdbcCustomer findCustomerByEmail(@PathVariable(required = true, name = "email") String email) {
        return repository.findCustomerByEmail(email);
    }

    @GetMapping(value = "/customer")
    public List<JdbcCustomer> findCustomersByGender(@RequestParam(required = true, name = "genderCode") String genderCode) {
        return repository.findCustomersByGender(genderCode);
    }

    @PostMapping(value = "/customer")
    public void createCustomer(@RequestBody(required = true) @Valid JdbcCustomer newCustomer) {
        repository.createCustomer(newCustomer);
    }

    @PatchMapping(value = "/customer/{customerId}")
    public void updateCustomerFullName(@PathVariable(required = true, name = "customerId") int customerId,
                                       @RequestBody(required = true) JdbcCustomerPatchRequest request) {
        repository.updateCustomerFullName(customerId, request.getNewFullName());
    }

}
