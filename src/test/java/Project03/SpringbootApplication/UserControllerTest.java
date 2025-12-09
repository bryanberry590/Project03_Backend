package Project03.SpringbootApplication;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;

import Project03.SpringbootApplication.controller.UserController;
import Project03.SpringbootApplication.service.UserService;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private Map<String, String> testUserData;
    private Map<String, Object> testUserResponse;

    @BeforeEach
    void setUp() {
        testUserData = new HashMap<>();
        testUserData.put("username", "testuser");
        testUserData.put("password", "password123");
        testUserData.put("email", "test@example.com");

        testUserResponse = new HashMap<>();
        testUserResponse.put("id", "test-user-id-123");
        testUserResponse.put("username", "testuser");
        testUserResponse.put("email", "test@example.com");
    }

    @Test
    void testCreateUser_Success() throws Exception {
        when(userService.createUser(anyString(), anyString(), anyString()))
            .thenReturn("User created with ID: test-user-id-123");

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testCreateUser_ServiceException() throws Exception {
        when(userService.createUser(anyString(), anyString(), anyString()))
            .thenThrow(new RuntimeException("Username already exists"));

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testUserData)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").value("Username already exists"));
    }

    @Test
    void testGetUserById_Success() throws Exception {
        when(userService.getUserById("test-user-id-123"))
            .thenReturn(testUserResponse);

        mockMvc.perform(get("/api/users/test-user-id-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("test-user-id-123"))
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void testGetUserById_NotFound() throws Exception {
        when(userService.getUserById("nonexistent-id"))
            .thenThrow(new RuntimeException("User not found"));

        mockMvc.perform(get("/api/users/nonexistent-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.error").value("User not found"));
    }

    @Test
    void testGetUserByUsername_Success() throws Exception {
        when(userService.getUserByUsername("testuser"))
            .thenReturn(testUserResponse);

        mockMvc.perform(get("/api/users/username/testuser"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void testLogin_Success() throws Exception {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "testuser");
        credentials.put("password", "password123");

        when(userService.validateCredentials("testuser", "password123"))
            .thenReturn(true);

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("Login successful"));
    }

    @Test
    void testLogin_InvalidCredentials() throws Exception {
        Map<String, String> credentials = new HashMap<>();
        credentials.put("username", "testuser");
        credentials.put("password", "wrongpassword");

        when(userService.validateCredentials("testuser", "wrongpassword"))
            .thenReturn(false);

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(credentials)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }

    @Test
    void testGetAllUsers_Success() throws Exception {
        List<Map<String, Object>> users = new ArrayList<>();
        users.add(testUserResponse);

        Map<String, Object> user2 = new HashMap<>();
        user2.put("id", "user-2");
        user2.put("username", "user2");
        user2.put("email", "user2@example.com");
        users.add(user2);

        when(userService.getAllUsers()).thenReturn(users);

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("testuser"))
                .andExpect(jsonPath("$[1].username").value("user2"));
    }

    @Test
    void testGetAllUsers_EmptyList() throws Exception {
        when(userService.getAllUsers()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        when(userService.deleteUser("test-user-id-123"))
            .thenReturn("User deleted successfully with ID: test-user-id-123");

        mockMvc.perform(delete("/api/users/test-user-id-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void testDeleteUser_NotFound() throws Exception {
        when(userService.deleteUser("nonexistent-id"))
            .thenThrow(new Exception("User not found with ID: nonexistent-id"));

        mockMvc.perform(delete("/api/users/nonexistent-id"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error").exists());
    }
}