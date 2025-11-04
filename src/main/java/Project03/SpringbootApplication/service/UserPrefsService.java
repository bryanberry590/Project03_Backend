package Project03.SpringbootApplication.service;

import java.util.ArrayList;
import java.util.Date;
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
public class UserPrefsService {

    @Autowired
    private Firestore firestore;

    /**
     * Create user preferences
     * Typically called when a new user is created
     */
    public Map<String, Object> createUserPrefs(String userId, Integer theme, 
                                               Boolean notificationEnabled, Integer colorScheme) 
            throws ExecutionException, InterruptedException {
        
        // Check if preferences already exist for this user
        if (userPrefsExist(userId)) {
            throw new RuntimeException("Preferences already exist for this user");
        }

        // Set defaults if not provided
        int finalTheme = (theme != null) ? theme : 0;
        boolean finalNotificationEnabled = (notificationEnabled != null) ? notificationEnabled : true;
        int finalColorScheme = (colorScheme != null) ? colorScheme : 0;

        // Create preferences
        Map<String, Object> prefsData = new HashMap<>();
        prefsData.put("userId", userId);
        prefsData.put("theme", finalTheme);
        prefsData.put("notificationEnabled", finalNotificationEnabled);
        prefsData.put("colorScheme", finalColorScheme);
        // prefsData.put("createdAt", new Date());
        // prefsData.put("updatedAt", new Date());

        DocumentReference docRef = firestore.collection("user_prefs").document();
        ApiFuture<WriteResult> result = docRef.set(prefsData);
        result.get();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("id", docRef.getId());
        response.put("userId", userId);
        response.put("theme", finalTheme);
        response.put("notificationEnabled", finalNotificationEnabled);
        response.put("colorScheme", finalColorScheme);
        
        return response;
    }

    /**
     * Create user preferences with default values
     */
    public Map<String, Object> createDefaultUserPrefs(String userId) 
            throws ExecutionException, InterruptedException {
        return createUserPrefs(userId, 0, true, 0);
    }

    /**
     * Get user preferences by user ID
     */
    public Map<String, Object> getUserPrefs(String userId) 
            throws ExecutionException, InterruptedException {
        
        Query query = firestore.collection("user_prefs")
                .whereEqualTo("userId", userId);
        
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        if (documents.isEmpty()) {
            throw new RuntimeException("Preferences not found for user");
        }

        DocumentSnapshot document = documents.get(0);
        Map<String, Object> prefs = document.getData();
        prefs.put("id", document.getId());
        
        return prefs;
    }

    /**
     * Get user preferences by preference ID
     */
    public Map<String, Object> getUserPrefsById(String prefsId) 
            throws ExecutionException, InterruptedException {
        
        DocumentReference docRef = firestore.collection("user_prefs").document(prefsId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            Map<String, Object> prefs = document.getData();
            prefs.put("id", document.getId());
            return prefs;
        } else {
            throw new RuntimeException("Preferences not found");
        }
    }

    /**
     * Update user preferences
     */
    public Map<String, Object> updateUserPrefs(String userId, Map<String, Object> updates) 
            throws ExecutionException, InterruptedException {
        
        // Find the preferences document for this user
        Query query = firestore.collection("user_prefs")
                .whereEqualTo("userId", userId);
        
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        if (documents.isEmpty()) {
            throw new RuntimeException("Preferences not found for user");
        }

        DocumentSnapshot document = documents.get(0);
        String prefsId = document.getId();

        // Add updated timestamp
        updates.put("updatedAt", new Date());

        // Update the document
        DocumentReference docRef = firestore.collection("user_prefs").document(prefsId);
        ApiFuture<WriteResult> result = docRef.update(updates);
        result.get();

        // Return updated preferences
        return getUserPrefs(userId);
    }

    /**
     * Update specific preference fields
     */
    public Map<String, Object> updateTheme(String userId, int theme) 
            throws ExecutionException, InterruptedException {
        Map<String, Object> updates = new HashMap<>();
        updates.put("theme", theme);
        return updateUserPrefs(userId, updates);
    }

    public Map<String, Object> updateNotificationEnabled(String userId, boolean enabled) 
            throws ExecutionException, InterruptedException {
        Map<String, Object> updates = new HashMap<>();
        updates.put("notificationEnabled", enabled);
        return updateUserPrefs(userId, updates);
    }

    public Map<String, Object> updateColorScheme(String userId, int colorScheme) 
            throws ExecutionException, InterruptedException {
        Map<String, Object> updates = new HashMap<>();
        updates.put("colorScheme", colorScheme);
        return updateUserPrefs(userId, updates);
    }

    /**
     * Delete user preferences
     */
    public String deleteUserPrefs(String userId) 
            throws ExecutionException, InterruptedException {
        
        Query query = firestore.collection("user_prefs")
                .whereEqualTo("userId", userId);
        
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        if (documents.isEmpty()) {
            throw new RuntimeException("Preferences not found for user");
        }

        for (QueryDocumentSnapshot document : documents) {
            document.getReference().delete();
        }

        return "User preferences deleted successfully";
    }

    /**
     * Check if user preferences exist
     */
    private boolean userPrefsExist(String userId) 
            throws ExecutionException, InterruptedException {
        
        Query query = firestore.collection("user_prefs")
                .whereEqualTo("userId", userId);
        
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        
        return !querySnapshot.get().isEmpty();
    }

    /**
     * Get all user preferences (admin function)
     */
    public List<Map<String, Object>> getAllUserPrefs() 
            throws ExecutionException, InterruptedException {
        
        ApiFuture<QuerySnapshot> query = firestore.collection("user_prefs").get();
        QuerySnapshot querySnapshot = query.get();
        
        List<Map<String, Object>> allPrefs = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
            Map<String, Object> prefs = document.getData();
            prefs.put("id", document.getId());
            allPrefs.add(prefs);
        }
        return allPrefs;
    }
}