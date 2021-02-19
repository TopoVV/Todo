package com.topov.todo.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


@Component
public class PasswordEncoderImpl implements PasswordEncoder {
    private static final Logger log = LogManager.getLogger(PasswordEncoderImpl.class);

    private MessageDigest md5;
    public PasswordEncoderImpl() {
        try {
            this.md5 = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            log.error("Cannot create PasswordEncoder", e);
            throw new RuntimeException("Cannot create password encoder", e);
        }
    }


    @Override
    public String encodePassword(String password) {
        final byte[] digest = this.md5.digest(password.getBytes());
        return new String(digest);
    }
}
