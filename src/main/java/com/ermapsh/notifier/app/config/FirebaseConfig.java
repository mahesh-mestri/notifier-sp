package com.ermapsh.notifier.app.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
@Slf4j
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {

        if (!FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.getInstance();
        }

        ClassPathResource resource =
                new ClassPathResource("notify-firebase-adminsdk.json");

        try (var serviceAccount = resource.getInputStream()) {

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(
                            GoogleCredentials.fromStream(serviceAccount)
                    )
                    .build();

            return FirebaseApp.initializeApp(options);
        }
    }

    @Bean
    public ApplicationRunner firebaseStartupCheck(FirebaseApp firebaseApp) {
        return args -> log.info(
                "Firebase initialized successfully: {}",
                firebaseApp.getName()
        );
    }
}