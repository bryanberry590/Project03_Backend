package Project03.SpringbootApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import Project03.SpringbootApplication.controller.RSVPController;
import Project03.SpringbootApplication.service.RSVPService;

@WebMvcTest(RSVPController.class)
public class RSVPControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RSVPService rsvpService;

    private Map<String, String> singleInviteData;
    private Map<String, Object> bulkInviteData;
    private Map<String, Object> rsvpResponse;

    @BeforeEach
    void setUp() {
        singleInviteData = new HashMap<>();
        singleInviteData.put("eventId", "event-123");
        singleInviteData.put("userId", "user-456");
        singleInviteData.put("eventOwnerId", "user-789");

        bulkInviteData = new HashMap<>();
        bulkInviteData.put("eventId", "event-123");
        List<String> userIds = new ArrayList<>();
        userIds.add("user-456");
        userIds.add("user-789");
        bulkInviteData.put("userIds", userIds);
        bulkInviteData.put("eventOwnerId", "owner-123");

        rsvpResponse = new HashMap<>();
        rsvpResponse.put("success", true);
        rsvpResponse.put("rsvpId", "rsvp-123");
        rsvpResponse.put("message", "Invitation sent successfully");
    }

    // @Test
    // void testSendSingleInvitation_Success() throws Exception {
    //     when(rsvpService.sendInvitation(anyString(), anyString(), anyString()))
    //         .thenReturn(rsvpResponse);

    //     mockMvc.perform(post("/api/rsvps/invite")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(singleInviteData)))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.success").value(true))
    //             .andExpect(jsonPath("$.rsvpId").value("rsvp-123"));
    // }

    // @Test
    // void testSendSingleInvitation_AlreadyExists() throws Exception {
    //     when(rsvpService.sendInvitation(anyString(), anyString(), anyString()))
    //         .thenThrow(new RuntimeException("Invitation already sent to this user"));

    //     mockMvc.perform(post("/api/rsvps/invite")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(singleInviteData)))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.error").exists());
    // }

    // @Test
    // void testSendBulkInvitations_Success() throws Exception {
    //     Map<String, Object> bulkResponse = new HashMap<>();
    //     bulkResponse.put("success", true);
    //     bulkResponse.put("totalSent", 2);
    //     bulkResponse.put("totalFailed", 0);

    //     when(rsvpService.sendBulkInvitations(anyString(), anyList(), anyString()))
    //         .thenReturn(bulkResponse);

    //     mockMvc.perform(post("/api/rsvps/invite/bulk")
    //             .contentType(MediaType.APPLICATION_JSON)
    //             .content(objectMapper.writeValueAsString(bulkInviteData)))
    //             .andExpect(status().isOk())
    //             .andExpect(jsonPath("$.success").value(true))
    //             .andExpect(jsonPath("$.totalSent").value(2));
    // }

    @Test
    void testUpdateRSVPStatus_Success() throws Exception {
        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", "yes");

        when(rsvpService.updateRSVPStatus(eq("rsvp-123"), eq("yes")))
            .thenReturn("RSVP status updated to 'yes'");

        mockMvc.perform(put("/api/rsvps/rsvp-123/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testUpdateRSVPStatus_InvalidStatus() throws Exception {
        Map<String, String> statusUpdate = new HashMap<>();
        statusUpdate.put("status", "invalid");

        when(rsvpService.updateRSVPStatus(eq("rsvp-123"), eq("invalid")))
            .thenThrow(new RuntimeException("Invalid status"));

        mockMvc.perform(put("/api/rsvps/rsvp-123/status")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(statusUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void testGetRSVPsForEvent_Success() throws Exception {
        List<Map<String, Object>> rsvps = new ArrayList<>();
        Map<String, Object> rsvp = new HashMap<>();
        rsvp.put("rsvpId", "rsvp-123");
        rsvp.put("eventId", "event-123");
        rsvp.put("status", "yes");
        rsvps.add(rsvp);

        when(rsvpService.getRSVPsByEvent("event-123"))
            .thenReturn(rsvps);

        mockMvc.perform(get("/api/rsvps/event/event-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetRSVPsByStatus_Success() throws Exception {
        List<Map<String, Object>> rsvps = new ArrayList<>();
        Map<String, Object> rsvp = new HashMap<>();
        rsvp.put("rsvpId", "rsvp-123");
        rsvp.put("eventId", "event-123");
        rsvp.put("status", "yes");
        rsvps.add(rsvp);

        when(rsvpService.getRSVPsByEventAndStatus("event-123", "yes"))
            .thenReturn(rsvps);

        mockMvc.perform(get("/api/rsvps/event/event-123/status/yes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].status").value("yes"));
    }

    @Test
    void testGetUserInvitations_Success() throws Exception {
        List<Map<String, Object>> invitations = new ArrayList<>();
        Map<String, Object> invitation = new HashMap<>();
        invitation.put("rsvpId", "rsvp-123");
        invitation.put("eventId", "event-123");
        invitations.add(invitation);

        when(rsvpService.getUserInvitations("user-456"))
            .thenReturn(invitations);

        mockMvc.perform(get("/api/rsvps/user/user-456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetPendingInvitations_Success() throws Exception {
        List<Map<String, Object>> pendingInvitations = new ArrayList<>();
        Map<String, Object> invitation = new HashMap<>();
        invitation.put("rsvpId", "rsvp-123");
        invitation.put("status", "no-reply");
        pendingInvitations.add(invitation);

        when(rsvpService.getPendingInvitations("user-456"))
            .thenReturn(pendingInvitations);

        mockMvc.perform(get("/api/rsvps/user/user-456/pending"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].status").value("no-reply"));
    }

    @Test
    void testGetRSVPSummary_Success() throws Exception {
        Map<String, Object> summary = new HashMap<>();
        summary.put("eventId", "event-123");
        summary.put("totalInvited", 20);
        
        Map<String, Integer> statusCounts = new HashMap<>();
        statusCounts.put("yes", 5);
        statusCounts.put("no", 2);
        statusCounts.put("maybe", 3);
        statusCounts.put("no-reply", 10);
        summary.put("statusCounts", statusCounts);

        when(rsvpService.getEventRSVPSummary("event-123"))
            .thenReturn(summary);

        mockMvc.perform(get("/api/rsvps/event/event-123/summary"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value("event-123"))
                .andExpect(jsonPath("$.totalInvited").value(20));
    }

    @Test
    void testDeleteRSVP_Success() throws Exception {
        when(rsvpService.deleteRSVP("rsvp-123"))
            .thenReturn("RSVP deleted successfully");

        mockMvc.perform(delete("/api/rsvps/rsvp-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testDeleteRSVP_NotFound() throws Exception {
        when(rsvpService.deleteRSVP("nonexistent"))
            .thenThrow(new RuntimeException("RSVP not found"));

        mockMvc.perform(delete("/api/rsvps/nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }
}