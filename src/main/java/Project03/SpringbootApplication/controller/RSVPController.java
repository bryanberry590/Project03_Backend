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

import Project03.SpringbootApplication.service.RSVPService;

@RestController
@RequestMapping("/api/rsvps")
public class RSVPController {

    @Autowired
    private RSVPService rsvpService;

    /**
     * POST /api/rsvps/invite - Send single invitation
     */
    @PostMapping("/invite")
    public Map<String, Object> sendInvitation(@RequestBody Map<String, String> data) {
        try {
            String eventId = data.get("eventId");
            String inviteRecipientId = data.get("inviteRecipientId");
            String eventOwnerId = data.get("eventOwnerId");
            
            return rsvpService.sendInvitation(eventId, inviteRecipientId, eventOwnerId);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * POST /api/rsvps/invite/bulk - Send multiple invitations
     */
    @PostMapping("/invite/bulk")
    public Map<String, Object> sendBulkInvitations(@RequestBody Map<String, Object> data) {
        try {
            String eventId = (String) data.get("eventId");
            @SuppressWarnings("unchecked")
            List<String> recipientIds = (List<String>) data.get("recipientIds");
            String eventOwnerId = (String) data.get("eventOwnerId");
            
            return rsvpService.sendBulkInvitations(eventId, recipientIds, eventOwnerId);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("success", false);
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * PUT /api/rsvps/{id}/status - Update RSVP status
     */
    @PutMapping("/{id}/status")
    public Map<String, Object> updateRSVPStatus(@PathVariable String id, 
                                                @RequestBody Map<String, String> data) {
        Map<String, Object> response = new HashMap<>();
        try {
            String status = data.get("status");
            String result = rsvpService.updateRSVPStatus(id, status);
            response.put("success", true);
            response.put("message", result);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

    /**
     * GET /api/rsvps/event/{eventId} - Get all RSVPs for an event
     */
    @GetMapping("/event/{eventId}")
    public List<Map<String, Object>> getRSVPsByEvent(@PathVariable String eventId) {
        try {
            return rsvpService.getRSVPsByEvent(eventId);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching RSVPs: " + e.getMessage());
        }
    }

    /**
     * GET /api/rsvps/event/{eventId}/status/{status} - Get RSVPs by status
     */
    @GetMapping("/event/{eventId}/status/{status}")
    public List<Map<String, Object>> getRSVPsByEventAndStatus(@PathVariable String eventId,
                                                              @PathVariable String status) {
        try {
            return rsvpService.getRSVPsByEventAndStatus(eventId, status);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching RSVPs: " + e.getMessage());
        }
    }

    /**
     * GET /api/rsvps/user/{userId} - Get all invitations for a user
     */
    @GetMapping("/user/{userId}")
    public List<Map<String, Object>> getUserInvitations(@PathVariable String userId) {
        try {
            return rsvpService.getUserInvitations(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching invitations: " + e.getMessage());
        }
    }

    /**
     * GET /api/rsvps/user/{userId}/pending - Get pending invitations
     */
    @GetMapping("/user/{userId}/pending")
    public List<Map<String, Object>> getPendingInvitations(@PathVariable String userId) {
        try {
            return rsvpService.getPendingInvitations(userId);
        } catch (Exception e) {
            throw new RuntimeException("Error fetching pending invitations: " + e.getMessage());
        }
    }

    /**
     * GET /api/rsvps/event/{eventId}/summary - Get RSVP summary
     */
    @GetMapping("/event/{eventId}/summary")
    public Map<String, Object> getEventRSVPSummary(@PathVariable String eventId) {
        try {
            return rsvpService.getEventRSVPSummary(eventId);
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }

    /**
     * DELETE /api/rsvps/{id} - Delete RSVP
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteRSVP(@PathVariable String id) {
        Map<String, Object> response = new HashMap<>();
        try {
            String result = rsvpService.deleteRSVP(id);
            response.put("success", true);
            response.put("message", result);
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", e.getMessage());
        }
        return response;
    }

        /**
     * GET /api/rsvps - Get all RSVPs (admin)
     * Returns all RSVP records regardless of event or user
     */
    @GetMapping
    public List<Map<String, Object>> getAllRSVPs() {
        try {
            return rsvpService.getAllRSVPs();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all RSVPs: " + e.getMessage());
        }
    }
}