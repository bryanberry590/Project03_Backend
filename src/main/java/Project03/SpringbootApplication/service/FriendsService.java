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
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

@Service
public class FriendsService {

    @Autowired
    private Firestore firestore;

    /**
     * Send friend request
     */
    public String sendFriendRequest(String userId, String friendId) 
            throws ExecutionException, InterruptedException {
        

        System.out.println("=== Starting friend request ===");
        System.out.println("UserId: " + userId);
        System.out.println("FriendId: " + friendId);

        // Check if friendship already exists
        if (friendshipExists(userId, friendId)) {
            throw new RuntimeException("Friendship already exists");
        }

        System.out.println("Friendship does not exist, proceeding...");


        // Create friend request
        Map<String, Object> friendData = new HashMap<>();
        friendData.put("userId", userId);
        friendData.put("friendId", friendId);
        friendData.put("status", "pending");
        // friendData.put("createdAt", new Date());

        System.out.println("Creating friend document...");

        DocumentReference docRef = firestore.collection("friends").document();
        ApiFuture<WriteResult> result = docRef.set(friendData);
        result.get();

        return "Friend request sent! ID: " + docRef.getId();
    }

    /**
     * Accept friend request
     */
    public String acceptFriendRequest(String friendshipId) 
            throws ExecutionException, InterruptedException {
        
        DocumentReference docRef = firestore.collection("friends").document(friendshipId);
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", "accepted");
        // updates.put("updatedAt", new Date());
        
        ApiFuture<WriteResult> result = docRef.update(updates);
        result.get();

        return "Friend request accepted!";
    }

    /**
     * Get all friends for a user
     */
    public List<Map<String, Object>> getUserFriends(String userId) 
            throws ExecutionException, InterruptedException {
        
        List<Map<String, Object>> friends = new ArrayList<>();

        // Query where user is userId
        Query query1 = firestore.collection("friends")
                .whereEqualTo("userId", userId)
                .whereEqualTo("status", "accepted");
        
        // Query where user is friendId
        Query query2 = firestore.collection("friends")
                .whereEqualTo("friendId", userId)
                .whereEqualTo("status", "accepted");

        // Execute both queries
        ApiFuture<QuerySnapshot> future1 = query1.get();
        ApiFuture<QuerySnapshot> future2 = query2.get();

        // Process results
        for (DocumentSnapshot doc : future1.get().getDocuments()) {
            Map<String, Object> friendship = doc.getData();
            friendship.put("id", doc.getId());
            friends.add(friendship);
        }

        for (DocumentSnapshot doc : future2.get().getDocuments()) {
            Map<String, Object> friendship = doc.getData();
            friendship.put("id", doc.getId());
            friends.add(friendship);
        }

        return friends;
    }

    /**
     * Get pending friend requests for a user
     */
    public List<Map<String, Object>> getPendingRequests(String userId) 
            throws ExecutionException, InterruptedException {
        
        Query query = firestore.collection("friends")
                .whereEqualTo("friendId", userId)
                .whereEqualTo("status", "pending");
        
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        
        List<Map<String, Object>> requests = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Map<String, Object> request = document.getData();
            request.put("id", document.getId());
            requests.add(request);
        }
        return requests;
    }

    /**
     * Check if friendship exists
     */
    private boolean friendshipExists(String userId, String friendId) throws ExecutionException, InterruptedException {
           
        System.out.println("Running friendshipExists query...");

        Query query = firestore.collection("friends")
                .whereEqualTo("userId", userId)
                .whereEqualTo("friendId", friendId);
        
        System.out.println("Query created, executing...");
        
        ApiFuture<QuerySnapshot> querySnapshot = query.get();

        System.out.println("Waiting for query result...");
        
        boolean isEmpty = querySnapshot.get().isEmpty();
        System.out.println("Query completed. Result: " + (isEmpty ? "No existing friendship" : "Friendship exists"));

        
        return !isEmpty;
    }

    /**
     * Remove friend
     */
    public String removeFriend(String friendshipId) 
            throws ExecutionException, InterruptedException {
        
        DocumentReference docRef = firestore.collection("friends").document(friendshipId);
        ApiFuture<WriteResult> result = docRef.delete();
        result.get();

        return "Friend removed successfully";
    }
}