package Project03.SpringbootApplication.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.Query;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

@Service
public class UserService {

    @Autowired
    private Firestore firestore;

    // private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Create a new user with hashed password
     */
    public String createUser(String username, String password, String email) 
            throws ExecutionException, InterruptedException {
        
        // Check if username already exists
        if (usernameExists(username)) {
            throw new RuntimeException("Username already exists");
        }

        // Hash the password
        // String hashedPassword = passwordEncoder.encode(password);

        // Prepare user data
        Map<String, Object> userData = new HashMap<>();
        userData.put("username", username);
        userData.put("password", password);
        userData.put("email", email);
        // userData.put("createdAt", new Date());
        // userData.put("updatedAt", new Date());

        // Save to Firestore
        DocumentReference docRef = firestore.collection("users").document();
        ApiFuture<WriteResult> result = docRef.set(userData);
        result.get();

        return "User created with ID: " + docRef.getId();
    }

    /**
     * Check if username exists
     */
    private boolean usernameExists(String username) 
            throws ExecutionException, InterruptedException {
        
        Query query = firestore.collection("users")
                .whereEqualTo("username", username);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        
        return !querySnapshot.get().isEmpty();
    }

    /**
     * Get user by ID
     */
    public Map<String, Object> getUserById(String userId) 
            throws ExecutionException, InterruptedException {
        
        DocumentReference docRef = firestore.collection("users").document(userId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            Map<String, Object> user = document.getData();
            // Don't return the password!
            user.remove("password");
            user.put("id", document.getId());
            return user;
        } else {
            throw new RuntimeException("User not found");
        }
    }

    /**
     * Get user by username
     */
    public Map<String, Object> getUserByUsername(String username) 
            throws ExecutionException, InterruptedException {
        
        Query query = firestore.collection("users")
                .whereEqualTo("username", username);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            Map<String, Object> user = document.getData();
            user.remove("password");
            user.put("id", document.getId());
            return user;
        } else {
            throw new RuntimeException("User not found");
        }
    }

    /**
     * Validate user credentials (for login)
     */
    public boolean validateCredentials(String username, String password) 
            throws ExecutionException, InterruptedException {
        
        Query query = firestore.collection("users")
                .whereEqualTo("username", username);
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        if (!documents.isEmpty()) {
            DocumentSnapshot document = documents.get(0);
            String userPassword = document.getString("password");
            // return passwordEncoder.matches(password, hashedPassword);
            return password.equals(userPassword);
        }
        return false;
    }

    /**
     * Get all users (admin function - be careful!)
     */
    public List<Map<String, Object>> getAllUsers() 
            throws ExecutionException, InterruptedException {
        
        ApiFuture<QuerySnapshot> query = firestore.collection("users").get();
        QuerySnapshot querySnapshot = query.get();
        
        List<Map<String, Object>> users = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
            Map<String, Object> user = document.getData();
            // user.remove("password"); // Don't expose passwords!
            user.put("id", document.getId());
            users.add(user);
        }
        return users;
    }
}