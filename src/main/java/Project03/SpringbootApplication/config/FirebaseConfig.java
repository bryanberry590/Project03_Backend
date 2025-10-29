package Project03.SpringbootApplication.config;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

import jakarta.annotation.PostConstruct;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.config.path:firebase-config.json}")
    private String firebaseConfigPath;

    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccount;
            
            // Check for environment variable first (Heroku)
            String firebaseConfigEnv = System.getenv("FIREBASE_CONFIG");
            if (firebaseConfigEnv != null && !firebaseConfigEnv.isEmpty()) {
                serviceAccount = new ByteArrayInputStream(
                    firebaseConfigEnv.getBytes(StandardCharsets.UTF_8)
                );
            } else {
                // Local file system or classpath
                try {
                    serviceAccount = new FileInputStream(firebaseConfigPath);
                } catch (Exception e) {
                    serviceAccount = getClass().getClassLoader()
                        .getResourceAsStream(firebaseConfigPath);
                }
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize Firebase", e);
        }
    }

    @Bean
    public Firestore firestore() {
        return FirestoreClient.getFirestore();
    }
}