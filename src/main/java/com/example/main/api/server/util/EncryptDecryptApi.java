package com.example.main.api.server.util;

import com.example.main.api.request.util.OriginalStringRequest;
import com.example.main.util.EncryptDecryptUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class EncryptDecryptApi {

    private static final String SECRET_KEY  = "MySecretKey12345";

    @GetMapping(value = "/encrypt/aes", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public String encryptAES(@RequestBody(required = true) OriginalStringRequest original) throws Exception {
        return EncryptDecryptUtil.encryptAES(original.getText(),SECRET_KEY);
    }

    @GetMapping(value = "/decrypt/aes", produces = MediaType.TEXT_PLAIN_VALUE)
    public String decryptAES(@RequestParam(required = true, name = "encrypted") String encrypted) throws Exception {
        return EncryptDecryptUtil.decryptAES(encrypted,SECRET_KEY);
    }
}
