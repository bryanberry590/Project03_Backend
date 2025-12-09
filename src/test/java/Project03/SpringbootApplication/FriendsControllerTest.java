package Project03.SpringbootApplication;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import Project03.SpringbootApplication.controller.FriendsController;
import Project03.SpringbootApplication.service.FriendsService;

@WebMvcTest(FriendsController.class)
public class FriendsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private FriendsService friendsService;

    private Map<String, String> friendRequestData;
    private Map<String, Object> friendshipResponse;

    @BeforeEach
    void setUp() {
        friendRequestData = new HashMap<>();
        friendRequestData.put("userId", "user-123");
        friendRequestData.put("friendId", "user-456");

        friendshipResponse = new HashMap<>();
        friendshipResponse.put("id", "friendship-789");
        friendshipResponse.put("userId", "user-123");
        friendshipResponse.put("friendId", "user-456");
        friendshipResponse.put("status", "pending");
    }

    @Test
    void testSendFriendRequest_Success() throws Exception {
        when(friendsService.sendFriendRequest(anyString(), anyString()))
            .thenReturn("Friend request sent! ID: friendship-789");

        mockMvc.perform(post("/api/friends/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(friendRequestData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testSendFriendRequest_AlreadyExists() throws Exception {
        when(friendsService.sendFriendRequest(anyString(), anyString()))
            .thenThrow(new RuntimeException("Friendship already exists"));

        mockMvc.perform(post("/api/friends/request")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(friendRequestData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Friendship already exists"));
    }

    @Test
    void testAcceptFriendRequest_Success() throws Exception {
        when(friendsService.acceptFriendRequest("friendship-789"))
            .thenReturn("Friend request accepted!");

        mockMvc.perform(put("/api/friends/friendship-789/accept"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Friend request accepted!"));
    }

    @Test
    void testAcceptFriendRequest_NotFound() throws Exception {
        when(friendsService.acceptFriendRequest("nonexistent"))
            .thenThrow(new RuntimeException("Friendship not found"));

        mockMvc.perform(put("/api/friends/nonexistent/accept"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("Friendship not found"));
    }

    @Test
    void testGetUserFriends_Success() throws Exception {
        List<Map<String, Object>> friends = new ArrayList<>();
        Map<String, Object> friend = new HashMap<>();
        friend.put("id", "friendship-1");
        friend.put("userId", "user-123");
        friend.put("friendId", "user-456");
        friend.put("status", "accepted");
        friends.add(friend);

        when(friendsService.getUserFriends("user-123"))
            .thenReturn(friends);

        mockMvc.perform(get("/api/friends/user/user-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    void testGetUserFriends_EmptyList() throws Exception {
        when(friendsService.getUserFriends("user-123"))
            .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/friends/user/user-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetPendingFriendRequests_Success() throws Exception {
        List<Map<String, Object>> pendingRequests = new ArrayList<>();
        pendingRequests.add(friendshipResponse);

        when(friendsService.getPendingRequests("user-456"))
            .thenReturn(pendingRequests);

        mockMvc.perform(get("/api/friends/pending/user-456"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value("pending"));
    }

    @Test
    void testGetPendingFriendRequests_EmptyList() throws Exception {
        when(friendsService.getPendingRequests("user-123"))
            .thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/friends/pending/user-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testRemoveFriend_Success() throws Exception {
        when(friendsService.removeFriend("friendship-789"))
            .thenReturn("Friend removed successfully");

        mockMvc.perform(delete("/api/friends/friendship-789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Friend removed successfully"));
    }

    @Test
    void testRemoveFriend_NotFound() throws Exception {
        when(friendsService.removeFriend("nonexistent"))
            .thenThrow(new RuntimeException("Friendship not found"));

        mockMvc.perform(delete("/api/friends/nonexistent"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Friendship not found"));
    }
}