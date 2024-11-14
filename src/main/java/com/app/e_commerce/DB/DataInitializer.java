package com.app.e_commerce.DB;



import com.app.e_commerce.entity.Role;
import com.app.e_commerce.repository.RoleRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(RoleRepo roleRepository) {
        return args -> {
            List<String> roles = Arrays.asList("USER", "ADMIN");

            for (String roleName : roles) {
                if (roleRepository.findByName(roleName).isEmpty()) {
                    Role role = new Role();
                    role.setName(roleName);
                    roleRepository.save(role);
                }
            }
        };
    }
}
