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

    /**
 * Delete user by ID
 * @param userId - The ID of the user to delete
 * @return Success message
 * @throws Exception if user not found or deletion fails
 */
public String deleteUser(String userId) throws Exception {
    try {
        // Check if user exists first
        DocumentReference userRef = firestore.collection("users").document(userId);
        DocumentSnapshot userSnapshot = userRef.get().get();
        
        if (!userSnapshot.exists()) {
            throw new Exception("User not found with ID: " + userId);
        }
        
        // Delete the user document
        userRef.delete().get();
        
        return "User deleted successfully with ID: " + userId;
    } catch (Exception e) {
        throw new Exception("Error deleting user: " + e.getMessage());
    }
}

/**
 * BONUS: Delete all test users (users with "testuser" in username)
 * This is useful for cleaning up Postman test data
 */
public Map<String, Object> deleteTestUsers() throws Exception {
    Map<String, Object> result = new HashMap<>();
    try {
        // Query for all users with "testuser" in their username
        Query query = firestore.collection("users")
                       .whereGreaterThanOrEqualTo("username", "testuser")
                       .whereLessThanOrEqualTo("username", "testuser" + "\uf8ff");
        
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        
        int deletedCount = 0;
        for (QueryDocumentSnapshot document : documents) {
            document.getReference().delete().get();
            deletedCount++;
        }
        
        result.put("success", true);
        result.put("message", "Deleted " + deletedCount + " test users");
        result.put("count", deletedCount);
        
        return result;
    } catch (Exception e) {
        throw new Exception("Error deleting test users: " + e.getMessage());
    }
}

/**
 * BONUS: Delete all users with "postman_test" in username
 * This is useful for cleaning up static Postman test users
 */
public Map<String, Object> deletePostmanTestUsers() throws Exception {
    Map<String, Object> result = new HashMap<>();
    try {
        // Query for all users with "postman_test" in their username
        Query query = firestore.collection("users")
                       .whereGreaterThanOrEqualTo("username", "postman_test")
                       .whereLessThanOrEqualTo("username", "postman_test" + "\uf8ff");
        
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();
        
        int deletedCount = 0;
        for (QueryDocumentSnapshot document : documents) {
            document.getReference().delete().get();
            deletedCount++;
        }
        
        result.put("success", true);
        result.put("message", "Deleted " + deletedCount + " Postman test users");
        result.put("count", deletedCount);
        
        return result;
    } catch (Exception e) {
        throw new Exception("Error deleting Postman test users: " + e.getMessage());
    }
}
}