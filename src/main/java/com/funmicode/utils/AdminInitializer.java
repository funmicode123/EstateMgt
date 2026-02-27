package com.funmicode.utils;

import com.funmicode.data.model.AccountStatus;
import com.funmicode.data.model.User;
import com.funmicode.data.model.UserRole;
import com.funmicode.data.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        String adminEmail = "goldenestate@gmail.com";
        
        if (userRepository.findByEmail(adminEmail).isEmpty()) {
            User admin = new User();
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("gestate2121"));
            admin.setFirstName("Golden");
            admin.setLastName("Estate");
            admin.setRole(UserRole.ADMIN);
            admin.setStatus(AccountStatus.ACTIVE);
            
            userRepository.save(admin);
            System.out.println("Default Admin account created successfully: " + adminEmail);
        } else {
            System.out.println("Admin account already exists: " + adminEmail);
        }
    }
}
