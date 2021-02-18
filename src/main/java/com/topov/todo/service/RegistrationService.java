package com.topov.todo.service;

import com.topov.todo.dto.RegistrationData;

public interface RegistrationService {
    boolean registerUser(RegistrationData registrationData);
}
