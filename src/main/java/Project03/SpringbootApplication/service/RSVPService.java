package Project03.SpringbootApplication.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
public class RSVPService {

    @Autowired
    private Firestore firestore;

    private final Set<String> VALID_STATUSES = new HashSet<>(
        Arrays.asList("yes", "no", "maybe", "no-reply")
    );

    /**
     * Send event invitation (create RSVP)
     */
    public Map<String, Object> sendInvitation(String eventId, String inviteRecipientId, 
                                             String eventOwnerId) 
            throws ExecutionException, InterruptedException {
        
        // Check if invitation already exists
        if (invitationExists(eventId, inviteRecipientId)) {
            throw new RuntimeException("Invitation already sent to this user");
        }

        // Create RSVP
        Map<String, Object> rsvpData = new HashMap<>();
        rsvpData.put("eventId", eventId);
        rsvpData.put("inviteRecipientId", inviteRecipientId);
        rsvpData.put("eventOwnerId", eventOwnerId);
        rsvpData.put("status", "no-reply");
        rsvpData.put("createdAt", new Date());
        rsvpData.put("updatedAt", new Date());

        DocumentReference docRef = firestore.collection("rsvps").document();
        ApiFuture<WriteResult> result = docRef.set(rsvpData);
        result.get();

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("rsvpId", docRef.getId());
        response.put("message", "Invitation sent successfully");
        
        return response;
    }

    /**
     * Send invitations to multiple users
     */
    public Map<String, Object> sendBulkInvitations(String eventId, List<String> recipientIds, 
                                                   String eventOwnerId) 
            throws ExecutionException, InterruptedException {
        
        List<String> successIds = new ArrayList<>();
        List<String> failedIds = new ArrayList<>();

        for (String recipientId : recipientIds) {
            try {
                sendInvitation(eventId, recipientId, eventOwnerId);
                successIds.add(recipientId);
            } catch (Exception e) {
                failedIds.add(recipientId);
            }
        }

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("totalSent", successIds.size());
        response.put("totalFailed", failedIds.size());
        response.put("successIds", successIds);
        response.put("failedIds", failedIds);
        
        return response;
    }

    /**
     * Update RSVP status
     */
    public String updateRSVPStatus(String rsvpId, String status) 
            throws ExecutionException, InterruptedException {
        
        // Validate status
        if (!VALID_STATUSES.contains(status.toLowerCase())) {
            throw new RuntimeException("Invalid status. Must be: yes, no, maybe, or no-reply");
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("status", status.toLowerCase());
        updates.put("updatedAt", new Date());

        DocumentReference docRef = firestore.collection("rsvps").document(rsvpId);
        ApiFuture<WriteResult> result = docRef.update(updates);
        WriteResult writeResult = result.get();

        return "RSVP status updated to '" + status + "' at: " + writeResult.getUpdateTime();
    }

    /**
     * Get all RSVPs for an event
     */
    public List<Map<String, Object>> getRSVPsByEvent(String eventId) 
            throws ExecutionException, InterruptedException {
        
        Query query = firestore.collection("rsvps")
                .whereEqualTo("eventId", eventId);
        
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        
        List<Map<String, Object>> rsvps = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Map<String, Object> rsvp = document.getData();
            rsvp.put("rsvpId", document.getId());
            rsvps.add(rsvp);
        }
        return rsvps;
    }

    /**
     * Get RSVPs by status for an event
     */
    public List<Map<String, Object>> getRSVPsByEventAndStatus(String eventId, String status) 
            throws ExecutionException, InterruptedException {
        
        Query query = firestore.collection("rsvps")
                .whereEqualTo("eventId", eventId)
                .whereEqualTo("status", status.toLowerCase());
        
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        
        List<Map<String, Object>> rsvps = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Map<String, Object> rsvp = document.getData();
            rsvp.put("rsvpId", document.getId());
            rsvps.add(rsvp);
        }
        return rsvps;
    }

    /**
     * Get all invitations for a user (events they're invited to)
     */
    public List<Map<String, Object>> getUserInvitations(String userId) 
            throws ExecutionException, InterruptedException {
        
        Query query = firestore.collection("rsvps")
                .whereEqualTo("inviteRecipientId", userId);
        
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        
        List<Map<String, Object>> invitations = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Map<String, Object> rsvp = document.getData();
            rsvp.put("rsvpId", document.getId());
            invitations.add(rsvp);
        }
        return invitations;
    }

    /**
     * Get pending invitations for a user
     */
    public List<Map<String, Object>> getPendingInvitations(String userId) 
            throws ExecutionException, InterruptedException {
        
        Query query = firestore.collection("rsvps")
                .whereEqualTo("inviteRecipientId", userId)
                .whereEqualTo("status", "no-reply");
        
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        
        List<Map<String, Object>> invitations = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Map<String, Object> rsvp = document.getData();
            rsvp.put("rsvpId", document.getId());
            invitations.add(rsvp);
        }
        return invitations;
    }

    /**
     * Check if invitation exists
     */
    private boolean invitationExists(String eventId, String recipientId) 
            throws ExecutionException, InterruptedException {
        
        Query query = firestore.collection("rsvps")
                .whereEqualTo("eventId", eventId)
                .whereEqualTo("inviteRecipientId", recipientId);
        
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        
        return !querySnapshot.get().isEmpty();
    }

    /**
     * Delete RSVP
     */
    public String deleteRSVP(String rsvpId) 
            throws ExecutionException, InterruptedException {
        
        DocumentReference docRef = firestore.collection("rsvps").document(rsvpId);
        ApiFuture<WriteResult> result = docRef.delete();
        result.get();

        return "RSVP deleted successfully";
    }

    /**
     * Get RSVP summary for an event (count by status)
     */
    public Map<String, Object> getEventRSVPSummary(String eventId) 
            throws ExecutionException, InterruptedException {
        
        List<Map<String, Object>> allRSVPs = getRSVPsByEvent(eventId);
        
        Map<String, Integer> statusCounts = new HashMap<>();
        statusCounts.put("yes", 0);
        statusCounts.put("no", 0);
        statusCounts.put("maybe", 0);
        statusCounts.put("no-reply", 0);
        
        for (Map<String, Object> rsvp : allRSVPs) {
            String status = (String) rsvp.get("status");
            statusCounts.put(status, statusCounts.get(status) + 1);
        }
        
        Map<String, Object> summary = new HashMap<>();
        summary.put("eventId", eventId);
        summary.put("totalInvited", allRSVPs.size());
        summary.put("statusCounts", statusCounts);
        
        return summary;
    }

        /**
     * Get all RSVPs (admin function)
     * Returns all RSVP records regardless of event or user
     */
    public List<Map<String, Object>> getAllRSVPs() throws ExecutionException, InterruptedException {
        
        ApiFuture<QuerySnapshot> query = firestore.collection("rsvps").get();
        QuerySnapshot querySnapshot = query.get();
        
        List<Map<String, Object>> rsvps = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
            Map<String, Object> rsvp = document.getData();
            rsvp.put("rsvpId", document.getId());
            rsvps.add(rsvp);
        }
        return rsvps;
    }

}