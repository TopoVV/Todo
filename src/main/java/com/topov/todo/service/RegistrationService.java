package com.topov.todo.service;

import com.topov.todo.dto.request.RegistrationData;

public interface RegistrationService {
    boolean registerUser(RegistrationData registrationData);
}
