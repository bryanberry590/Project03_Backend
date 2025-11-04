package Project03.SpringbootApplication.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Project03.SpringbootApplication.service.EventService;

@RestController
@RequestMapping("/api/events")
public class EventController {

    @Autowired
    private EventService eventService;

    /**
     * POST /api/events - Create new event
     */
    @PostMapping
    public Map<String, Object> createEvent(@RequestBody Map<String, Object> eventData) {
        try {
            return eventService.createEvent(eventData);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * GET /api/events/{id} - Get event by ID
     */
    @GetMapping("/{id}")
    public Map<String, Object> getEventById(@PathVariable String id) {
        try {
            return eventService.getEventById(id);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * GET /api/events/user/{userId} - Get all events for a user
     */
    @GetMapping("/user/{userId}")
    public List<Map<String, Object>> getEventsByUser(@PathVariable String userId) {
        try {
            return eventService.getEventsByUser(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching events: " + e.getMessage());
        }
    }

    /**
     * GET /api/events/type/{isEvent} - Get events by type (true=event, false=free time)
     */
    @GetMapping("/type/{isEvent}")
    public List<Map<String, Object>> getEventsByType(@PathVariable boolean isEvent) {
        try {
            return eventService.getEventsByType(isEvent);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching events: " + e.getMessage());
        }
    }

    /**
     * GET /api/events - Get all events
     */
    @GetMapping
    public List<Map<String, Object>> getAllEvents() {
        try {
            return eventService.getAllEvents();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching events: " + e.getMessage());
        }
    }

    /**
     * PUT /api/events/{id} - Update event
     */
    @PutMapping("/{id}")
    public Map<String, Object> updateEvent(@PathVariable String id, 
                                          @RequestBody Map<String, Object> updates) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = eventService.updateEvent(id, updates);
            response.put("success", true);
            response.put("message", result);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

    /**
     * DELETE /api/events/{id} - Delete event
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteEvent(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = eventService.deleteEvent(id);
            response.put("success", true);
            response.put("message", result);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }
}