package Project03.SpringbootApplication.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.config.path:firebase-config.json}")
    private String firebaseConfigPath;

    @PostConstruct
    public void initialize() {
        try {
            InputStream serviceAccount;
            
            // Try to load from file system first (for Docker/local)
            try {
                serviceAccount = new FileInputStream(firebaseConfigPath);
            } catch (Exception e) {
                // Fall back to classpath (for Heroku)
                serviceAccount = getClass().getClassLoader()
                    .getResourceAsStream(firebaseConfigPath);
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