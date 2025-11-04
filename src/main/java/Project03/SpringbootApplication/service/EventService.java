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
import com.google.cloud.firestore.QuerySnapshot;
import com.google.cloud.firestore.WriteResult;

@Service
public class EventService {

    @Autowired
    private Firestore firestore;

    /**
     * Create a new event
     */
    public Map<String, Object> createEvent(Map<String, Object> eventData) 
            throws ExecutionException, InterruptedException {
        
        // Add timestamps
        // eventData.put("createdAt", new Date());
        // eventData.put("updatedAt", new Date());

        // Create event in Firestore
        DocumentReference docRef = firestore.collection("events").document();
        ApiFuture<WriteResult> result = docRef.set(eventData);
        result.get();

        String eventId = docRef.getId();

        // Return response with event ID
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("eventId", eventId);
        response.put("message", "Event created successfully");
        response.putAll(eventData);
        
        return response;
    }

    /**
     * Get event by ID
     */
    public Map<String, Object> getEventById(String eventId) 
            throws ExecutionException, InterruptedException {
        
        DocumentReference docRef = firestore.collection("events").document(eventId);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();

        if (document.exists()) {
            Map<String, Object> event = document.getData();
            event.put("eventId", document.getId());
            return event;
        } else {
            throw new RuntimeException("Event not found");
        }
    }

    /**
     * Get all events for a user (created by them)
     */
    public List<Map<String, Object>> getEventsByUser(String userId) 
            throws ExecutionException, InterruptedException {
        
        Query query = firestore.collection("events")
                .whereEqualTo("userId", userId);
        
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        
        List<Map<String, Object>> events = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Map<String, Object> event = document.getData();
            event.put("eventId", document.getId());
            events.add(event);
        }
        return events;
    }

    /**
     * Get all events (filter by type: event or free time)
     */
    public List<Map<String, Object>> getEventsByType(boolean isEvent) 
            throws ExecutionException, InterruptedException {
        
        Query query = firestore.collection("events")
                .whereEqualTo("isEvent", isEvent);
        
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        
        List<Map<String, Object>> events = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Map<String, Object> event = document.getData();
            event.put("eventId", document.getId());
            events.add(event);
        }
        return events;
    }

    /**
     * Get events by date range
     */
    public List<Map<String, Object>> getEventsByDateRange(Date startDate, Date endDate) 
            throws ExecutionException, InterruptedException {
        
        Query query = firestore.collection("events")
                .whereGreaterThanOrEqualTo("date", startDate)
                .whereLessThanOrEqualTo("date", endDate)
                .orderBy("date");
        
        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        
        List<Map<String, Object>> events = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.get().getDocuments()) {
            Map<String, Object> event = document.getData();
            event.put("eventId", document.getId());
            events.add(event);
        }
        return events;
    }

    /**
     * Update an event
     */
    public String updateEvent(String eventId, Map<String, Object> updates) 
            throws ExecutionException, InterruptedException {
        
        // Add updated timestamp
        updates.put("updatedAt", new Date());
        
        DocumentReference docRef = firestore.collection("events").document(eventId);
        ApiFuture<WriteResult> result = docRef.update(updates);
        WriteResult writeResult = result.get();

        return "Event updated at: " + writeResult.getUpdateTime();
    }

    /**
     * Delete an event
     */
    public String deleteEvent(String eventId) 
            throws ExecutionException, InterruptedException {
        
        // Also delete all RSVPs for this event
        Query rsvpQuery = firestore.collection("rsvps")
                .whereEqualTo("eventId", eventId);
        ApiFuture<QuerySnapshot> rsvpSnapshot = rsvpQuery.get();
        
        for (DocumentSnapshot rsvpDoc : rsvpSnapshot.get().getDocuments()) {
            rsvpDoc.getReference().delete();
        }
        
        // Delete the event
        DocumentReference docRef = firestore.collection("events").document(eventId);
        ApiFuture<WriteResult> result = docRef.delete();
        result.get();

        return "Event and associated RSVPs deleted successfully";
    }

    /**
     * Get all events (admin function)
     */
    public List<Map<String, Object>> getAllEvents() 
            throws ExecutionException, InterruptedException {
        
        ApiFuture<QuerySnapshot> query = firestore.collection("events").get();
        QuerySnapshot querySnapshot = query.get();
        
        List<Map<String, Object>> events = new ArrayList<>();
        for (DocumentSnapshot document : querySnapshot.getDocuments()) {
            Map<String, Object> event = document.getData();
            event.put("eventId", document.getId());
            events.add(event);
        }
        return events;
    }
}