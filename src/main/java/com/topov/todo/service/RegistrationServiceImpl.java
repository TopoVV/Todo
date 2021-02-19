package com.topov.todo.service;

import com.topov.todo.dto.request.RegistrationData;
import com.topov.todo.model.User;
import com.topov.todo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public RegistrationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public boolean registerUser(RegistrationData registrationData) {
        final String encodedPassword = this.passwordEncoder.encodePassword(registrationData.getPassword());
        final User user = new User(registrationData.getUsername(), encodedPassword);

        if (this.userRepository.existsByUsername(user.getUsername())) {
            return false;
        }

        this.userRepository.save(user);
        return true;
    }
}
