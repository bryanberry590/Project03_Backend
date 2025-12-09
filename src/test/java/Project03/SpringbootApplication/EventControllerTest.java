package Project03.SpringbootApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import Project03.SpringbootApplication.controller.EventController;
import Project03.SpringbootApplication.service.EventService;

@WebMvcTest(EventController.class)
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private EventService eventService;

    private Map<String, Object> testEventData;
    private Map<String, Object> testEventResponse;

    @BeforeEach
    void setUp() {
        testEventData = new HashMap<>();
        testEventData.put("eventTitle", "Team Meeting");
        testEventData.put("description", "Weekly sync");
        testEventData.put("userId", "user-123");
        testEventData.put("isEvent", true);
        testEventData.put("startTime", "2024-12-15T10:00:00");
        testEventData.put("endTime", "2024-12-15T11:00:00");

        testEventResponse = new HashMap<>();
        testEventResponse.put("success", true);
        testEventResponse.put("eventId", "event-123");
        testEventResponse.put("message", "Event created successfully");
        testEventResponse.put("eventTitle", "Team Meeting");
        testEventResponse.put("userId", "user-123");
        testEventResponse.put("isEvent", true);
    }

    @Test
    void testCreateEvent_Success() throws Exception {
        when(eventService.createEvent(any()))
            .thenReturn(testEventResponse);

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testEventData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.eventId").value("event-123"));
    }

    @Test
    void testCreateEvent_ServiceException() throws Exception {
        when(eventService.createEvent(any()))
            .thenThrow(new RuntimeException("Event title is required"));

        mockMvc.perform(post("/api/events")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testEventData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void testGetEventById_Success() throws Exception {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("eventId", "event-123");
        eventData.put("eventTitle", "Team Meeting");
        
        when(eventService.getEventById("event-123"))
            .thenReturn(eventData);

        mockMvc.perform(get("/api/events/event-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.eventId").value("event-123"))
                .andExpect(jsonPath("$.eventTitle").value("Team Meeting"));
    }

    @Test
    void testGetEventById_NotFound() throws Exception {
        when(eventService.getEventById("nonexistent"))
            .thenThrow(new RuntimeException("Event not found"));

        mockMvc.perform(get("/api/events/nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Event not found"));
    }

    @Test
    void testGetEventsByUserId_Success() throws Exception {
        List<Map<String, Object>> events = new ArrayList<>();
        events.add(testEventResponse);

        when(eventService.getEventsByUser("user-123"))
            .thenReturn(events);

        mockMvc.perform(get("/api/events/user/user-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetEventsByType_Events() throws Exception {
        List<Map<String, Object>> events = new ArrayList<>();
        events.add(testEventResponse);

        when(eventService.getEventsByType(true))
            .thenReturn(events);

        mockMvc.perform(get("/api/events/type/true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetEventsByType_FreeTime() throws Exception {
        Map<String, Object> freeTime = new HashMap<>(testEventResponse);
        freeTime.put("isEvent", false);
        List<Map<String, Object>> freeTimes = new ArrayList<>();
        freeTimes.add(freeTime);

        when(eventService.getEventsByType(false))
            .thenReturn(freeTimes);

        mockMvc.perform(get("/api/events/type/false"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetAllEvents_Success() throws Exception {
        List<Map<String, Object>> events = new ArrayList<>();
        events.add(testEventResponse);

        when(eventService.getAllEvents())
            .thenReturn(events);

        mockMvc.perform(get("/api/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testUpdateEvent_Success() throws Exception {
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("eventTitle", "Updated Meeting");
        updateData.put("description", "New description");

        when(eventService.updateEvent(eq("event-123"), any()))
            .thenReturn("Event updated successfully");

        mockMvc.perform(put("/api/events/event-123")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testUpdateEvent_NotFound() throws Exception {
        Map<String, Object> updateData = new HashMap<>();
        updateData.put("eventTitle", "Updated Meeting");

        when(eventService.updateEvent(eq("nonexistent"), any()))
            .thenThrow(new RuntimeException("Event not found"));

        mockMvc.perform(put("/api/events/nonexistent")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void testDeleteEvent_Success() throws Exception {
        when(eventService.deleteEvent("event-123"))
            .thenReturn("Event deleted successfully");

        mockMvc.perform(delete("/api/events/event-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));
    }

    @Test
    void testDeleteEvent_NotFound() throws Exception {
        when(eventService.deleteEvent("nonexistent"))
            .thenThrow(new RuntimeException("Event not found"));

        mockMvc.perform(delete("/api/events/nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false));
    }
}