package com.foro.foro.Utils;

import com.foro.foro.Model.Profile;
import com.foro.foro.Repository.ProfileRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initializeProfiles(ProfileRepository profileRepository) {
        return args -> {
            String defaultProfileName = "USER";

            // Verifica si ya existe el perfil USER
            if (profileRepository.findByName(defaultProfileName).isEmpty()) {
                Profile userProfile = new Profile();
                userProfile.setName(defaultProfileName);
                profileRepository.save(userProfile);
                System.out.println("profile created.");
            } else {
                System.out.println("profile already exist.");
            }
        };
    }
}