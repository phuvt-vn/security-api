package com.example.main.api.server.auth.basic;

import com.example.main.api.request.auth.basic.BasicAuthCreateUserRequest;
import com.example.main.entity.BasicAuthUser;
import com.example.main.repository.BasicAuthRepository;
import com.example.main.util.EncryptDecryptUtil;
import com.example.main.util.HashUtil;
import com.example.main.util.SecureStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;

@RestController
@RequestMapping("/api/auth/basic/v1")
public class BasicAuthApi {

    public static final String SECRET_KEY = "TheSecretKey2468";

    @Autowired
    private BasicAuthRepository repository;

    @GetMapping(value = "/time", produces = MediaType.TEXT_PLAIN_VALUE)
    public String time(){
        return "Now is " + LocalTime.now();
    }

    @PostMapping(value = "/user", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createUser(@RequestBody(required = true) BasicAuthCreateUserRequest userRequest) throws Exception {
        var user = new BasicAuthUser();
        var encryptUserName = EncryptDecryptUtil.encryptAES(userRequest.getUsername(), SECRET_KEY);
        user.setUsername(encryptUserName);

        var salt= SecureStringUtil.randomString(16);
        user.setSalt(salt);

        var passworHash = HashUtil.bcrypt(userRequest.getPassword(), salt);
        user.setPasswordHash(passworHash);

        user.setDisplayName(userRequest.getDisplayName());

        var saved = repository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("New user created: "+saved.getDisplayName());
    }
}
